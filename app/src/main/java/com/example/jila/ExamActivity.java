package com.example.jila;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private TextView scoreTextView;
    private Button submitAnswerButton;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        db = FirebaseFirestore.getInstance();
        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        scoreTextView = findViewById(R.id.scoreTextView);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);

        loadQuestions();

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void loadQuestions() {
        db.collection("quizzes").document("quizId") // Thay "quizId" bằng ID của quiz bạn muốn tải
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Quiz quiz = task.getResult().toObject(Quiz.class);
                        questionList.addAll(quiz.getQuestions());
                        displayNextQuestion();
                    }
                });
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestion());
            List<String> options = currentQuestion.getOptions();
            option1RadioButton.setText(options.get(0));
            option2RadioButton.setText(options.get(1));
            option3RadioButton.setText(options.get(2));
            option4RadioButton.setText(options.get(3));
        } else {
            Toast.makeText(this, "Quiz Finished! Your score is " + score, Toast.LENGTH_LONG).show();
            submitAnswerButton.setEnabled(false);
        }
    }

    private void checkAnswer() {
        int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();

        Question currentQuestion = questionList.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            score++;
        }
        scoreTextView.setText("Score: " + score);

        currentQuestionIndex++;
        displayNextQuestion();
    }
}
