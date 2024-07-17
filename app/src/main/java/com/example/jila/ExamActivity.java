package com.example.jila;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Answer;
import model.Question;

public class ExamActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView questionTextView;
    private RadioGroup answerGroup;
    private Button nextButton;
    private Button finishButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Log.d("ExamActivity", "onCreate called");

        questionTextView = findViewById(R.id.questionText);
        answerGroup = findViewById(R.id.answerGroup);
        nextButton = findViewById(R.id.nextButton);
        finishButton = findViewById(R.id.finishButton);

        db = FirebaseFirestore.getInstance();

        loadQuiz();

        nextButton.setOnClickListener(v -> {
            checkAnswer();
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                showNextQuestion();
            } else {
                nextButton.setVisibility(View.GONE);
                finishButton.setVisibility(View.VISIBLE);
            }
        });

        finishButton.setOnClickListener(v -> {
            checkAnswer();
            showScore();
        });
    }

    private void loadQuiz() {
        // Hardcoding quizId
        String quizId = "XeydVVPwCG6hxLIKKWRa";

        CollectionReference questionRef = db.collection("question");
        questionRef.whereEqualTo("quizId", db.document("quiz/" + quizId)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String questionId = document.getId();
                    String questionText = document.getString("questionText");
                    getAnswerForQuestion(questionId, questionText);
                }
            } else {
                Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAnswerForQuestion(String questionId, String questionText) {
        CollectionReference answerRef = db.collection("answer");
        answerRef.whereEqualTo("questionId", db.document("question/" + questionId)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Answer> answers = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String answerText = document.getString("answerText");
                    boolean isCorrect = document.getBoolean("isCorrect");

                    answers.add(new Answer(answerText, isCorrect));
                }
                questionList.add(new Question(questionText, answers));
                if (questionList.size() == 1) {
                    showNextQuestion();
                }
            } else {
                Toast.makeText(this, "Failed to load answers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNextQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getQuestion());
        answerGroup.removeAllViews();

        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(currentQuestion.getAnswers().get(i).getAnswer_text());
            radioButton.setId(i);
            answerGroup.addView(radioButton);
        }
    }

    private void checkAnswer() {
        int selectedId = answerGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            if (questionList.get(currentQuestionIndex).isCorrectAnswer(selectedAnswer)) {
                score++;
                Log.d("ExamActivity", "Correct answer! Current score: " + score);
            }
        }
    }

    private void showScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Completed");
        builder.setMessage("Your score is: " + score + " out of " + questionList.size());
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            navigateToHome();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                navigateToHome();
            }
        }, 3000);
    }

    private void navigateToHome() {
        Intent intent = new Intent(ExamActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
