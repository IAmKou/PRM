package com.example.jila;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.Question;

public class LearningActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private TextView frontTextView;
    private TextView backTextView;
    private ViewFlipper viewFlipper;
    private GestureDetector gestureDetector;
    private Button finishButton;
    private Button nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        Log.d("LearningActivity", "onCreate called");

        frontTextView = findViewById(R.id.frontTextView);
        backTextView = findViewById(R.id.backTextView);
        viewFlipper = findViewById(R.id.viewFlipper);
        finishButton = findViewById(R.id.finishButton);
        nextButton = findViewById(R.id.nextButton);

        db = FirebaseFirestore.getInstance();

        gestureDetector = new GestureDetector(this, new GestureListener());

        loadQuiz();

        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(LearningActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questionList.size()) {
                showNextQuestion();
            } else {
                Intent intent = new Intent(LearningActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewFlipper.setOnClickListener(v -> {
            viewFlipper.showNext();
        });
    }

//    private void loadQuiz() {
//        String quizId = "XeydVVPwCG6hxLIKKWRa";
//
//        CollectionReference questionRef = db.collection("question");
//        questionRef.whereEqualTo("quizId", db.document("quiz/" + quizId)).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    String questionText = document.getString("questionText");
//                    List<Answer> answers = new ArrayList<>();
//                    List<Map<String, Object>> answersData = (List<Map<String, Object>>) document.get("answers");
//
//                    if (answersData != null) {
//                        for (Map<String, Object> answerData : answersData) {
//                            String answerText = (String) answerData.get("answerText");
//                            Boolean isCorrect = (Boolean) answerData.get("isCorrect");
//                            if (answerText != null && isCorrect != null) {
//                                answers.add(new Answer(answerText, isCorrect));
//                            }
//                        }
//                    }
//
//                    if (questionText != null && !answers.isEmpty()) {
//                        questionList.add(new Question(questionText, answers));
//                    }
//                }
//                if (!questionList.isEmpty()) {
//                    showNextQuestion();
//                } else {
//                    Toast.makeText(this, "No questions found.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void loadQuiz() {
        Log.d("LoadQuiz", "loadQuiz called");
        String quizId = "XeydVVPwCG6hxLIKKWRa";

        CollectionReference questionRef = db.collection("question");
        questionRef.whereEqualTo("quizId", db.document("quiz/" + quizId)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("LoadQuiz", "Task successful");

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String questionId = document.getId();
                    String questionText = document.getString("questionText");

                    if (questionText != null) {
                        loadAnswers(questionId, questionText);
                    } else {
                        Log.d("LoadQuiz", "Question text is null for document: " + questionId);
                    }
                }
            } else {
                Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                Log.e("LoadQuiz", "Error getting documents: ", task.getException());
            }
        });
    }

    private void loadAnswers(String questionId, String questionText) {
        CollectionReference answerRef = db.collection("answer");
        answerRef.whereEqualTo("questionId", db.document("question/" + questionId)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Answer> answers = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String answerText = document.getString("answerText");
                    Boolean isCorrect = document.getBoolean("isCorrect");

                    if (answerText != null && isCorrect != null) {
                        answers.add(new Answer(answerText, isCorrect));
                    } else {
                        Log.d("LoadQuiz", "Answer text or isCorrect is null for document: " + document.getId());
                    }
                }

                if (!answers.isEmpty()) {
                    questionList.add(new Question(questionText, answers));
                    Log.d("LoadQuiz", "Added question: " + questionText);

                    if (questionList.size() == 1) {
                        showNextQuestion();
                    }
                } else {
                    Log.d("LoadQuiz", "No answers found for question: " + questionId);
                }
            } else {
                Toast.makeText(this, "Failed to load answers.", Toast.LENGTH_SHORT).show();
                Log.e("LoadQuiz", "Error getting documents: ", task.getException());
            }

            if (questionList.isEmpty()) {
                Toast.makeText(this, "No questions found.", Toast.LENGTH_SHORT).show();
                Log.d("LoadQuiz", "questionList is empty");
            }
        });
    }



    private void showNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);
            frontTextView.setText(currentQuestion.getQuestion());
            backTextView.setText(getCorrectAnswer(currentQuestion));
            currentQuestionIndex++;
            viewFlipper.setDisplayedChild(0); // Show front side of the card
        } else {
            finishButton.setVisibility(View.VISIBLE);
            nextButton.setText("Home");
        }
    }

    private String getCorrectAnswer(Question question) {
        for (Answer answer : question.getAnswers()) {
            if (answer.isIs_correct()) {
                return answer.getAnswer_text();
            }
        }
        return "No correct answer found";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    private void onSwipeLeft() {
        if (currentQuestionIndex < questionList.size()) {
            showNextQuestion();
        }
    }

    private void onSwipeRight() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 2; // Decrease by 2 to show the previous question correctly
            showNextQuestion();
        }
    }
}

