package com.example.jila;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.Question;

public class QuizActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private TextView questionTextView;
    private RadioGroup answerGroup;
    private Button nextButton;
    private Button finishButton;
    private Button reportButton;

    private String currentQuizTitle; // Variable to hold the current quiz title

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);
        Log.d("QuizActivity", "onCreate called");

        questionTextView = findViewById(R.id.questionText);
        answerGroup = findViewById(R.id.answerGroup);
        nextButton = findViewById(R.id.nextButton);
        finishButton = findViewById(R.id.finishButton);
        reportButton = findViewById(R.id.reportButton);


        db = FirebaseFirestore.getInstance();


        loadQuiz();

        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                showNextQuestion();
            } else {
                Toast.makeText(QuizActivity.this, "Quiz Completed", Toast.LENGTH_SHORT).show();
                nextButton.setVisibility(View.GONE);
                finishButton.setVisibility(View.VISIBLE);
            }
        });

        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        reportButton.setOnClickListener(v -> {
            showReportDialog();
        });
    }

    private void loadQuiz() {
        // Hardcoding quizId
        String quizId = "XeydVVPwCG6hxLIKKWRa";

        CollectionReference quizRef = db.collection("quiz");
        quizRef.document(quizId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                currentQuizTitle = documentSnapshot.getString("title");
            } else {
                Toast.makeText(this, "Quiz not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load quiz", Toast.LENGTH_SHORT).show();
            Log.e("QuizActivity", "Error loading quiz", e);
        });

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

    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.report_dialog, null);
        builder.setView(dialogView);

        Spinner reportSpinner = dialogView.findViewById(R.id.reportSpinner);
        Button submitReportButton = dialogView.findViewById(R.id.submitReportButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.report_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        submitReportButton.setOnClickListener(v -> {
            String selectedOption = reportSpinner.getSelectedItem().toString();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DocumentReference userRef = db.collection("users").document(userId);
                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String reporterName = document.getString("name");
                            Date currentTime = new Date();
                            saveReport(selectedOption, currentQuizTitle, questionList.get(currentQuestionIndex).getQuestion(), reporterName, currentTime);
                        }

                        Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void saveReport(String reportOption, String quizTitle, String questionText, String reporterName, Date timestamp) {

        CollectionReference reportsRef = db.collection("report");


        Map<String, Object> report = new HashMap<>();
        report.put("report_type", reportOption);
        report.put("quizTitle", quizTitle);
        report.put("questionText", questionText);
        report.put("reporter", reporterName);
        report.put("timestamp", timestamp);

        reportsRef.add(report)
                .addOnSuccessListener(documentReference -> Log.d("QuizActivity", "Report added"))
                .addOnFailureListener(e -> Log.e("QuizActivity", "Error adding report", e));
    }
}
