package com.example.jila;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Answer;
import model.Question;

public class QuizActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private TextView questionTextView;
    private RadioGroup answerGroup;
    private Button nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);
        Log.d("QuizActivity", "go to onCreate");

        questionTextView = findViewById(R.id.questionText);
        answerGroup = findViewById(R.id.answerGroup);
        nextButton = findViewById(R.id.nextButton);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        loadQuiz();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questionList.size() - 1) {
                    currentQuestionIndex++;
                    showNextQuestion();
                } else {
                    Toast.makeText(QuizActivity.this, "Quiz Completed", Toast.LENGTH_SHORT).show();
                }
            }
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
                    String answerId = document.getId();
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

            if (currentQuestion.getAnswers().get(i).isIs_correct()) {
                radioButton.setBackgroundColor(Color.GREEN);
            }
            answerGroup.addView(radioButton);
        }
    }
}
