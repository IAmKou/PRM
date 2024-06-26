package com.example.jila;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.Question;

public class CreateQuizActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Question> questionList = new ArrayList<>();

    private EditText quizTitleEditText;
    private Button addQuestionButton;
    private Button saveQuizButton;
    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;
    private Spinner quizTypeSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        Log.d("CreateQuizActivity", "onCreate called");

        quizTitleEditText = findViewById(R.id.quizTitle);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        saveQuizButton = findViewById(R.id.saveQuizButton);
        questionRecyclerView = findViewById(R.id.questionRecyclerView);
        quizTypeSpinner = findViewById(R.id.quizTypeSpinner);

        db = FirebaseFirestore.getInstance();

        questionAdapter = new QuestionAdapter(questionList);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionRecyclerView.setAdapter(questionAdapter);

        // Fetch quiz types from Firestore
        fetchQuizTypes();

        addQuestionButton.setOnClickListener(v -> showAddQuestionDialog());

        saveQuizButton.setOnClickListener(v -> saveQuiz());
    }

    private void fetchQuizTypes() {
        CollectionReference quizTypesRef = db.collection("type");
        quizTypesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<String> quizTypes = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    String quizType = document.getString("type_name");
                    if (quizType != null) {
                        quizTypes.add(quizType);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, quizTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                quizTypeSpinner.setAdapter(adapter);
            } else {
                Log.e("CreateQuizActivity", "Error getting quiz types", task.getException());
                Toast.makeText(this, "Failed to load quiz types", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddQuestionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_quiz);

        EditText questionTextEditText = dialog.findViewById(R.id.questionText);
        EditText answer1EditText = dialog.findViewById(R.id.answer1);
        EditText answer2EditText = dialog.findViewById(R.id.answer2);
        EditText answer3EditText = dialog.findViewById(R.id.answer3);
        EditText answer4EditText = dialog.findViewById(R.id.answer4);
        RadioGroup correctAnswerGroup = dialog.findViewById(R.id.correctAnswerGroup);
        Button addQuestionDialogButton = dialog.findViewById(R.id.addQuestionDialogButton);

        addQuestionDialogButton.setOnClickListener(v -> {
            String questionText = questionTextEditText.getText().toString().trim();
            String answer1 = answer1EditText.getText().toString().trim();
            String answer2 = answer2EditText.getText().toString().trim();
            String answer3 = answer3EditText.getText().toString().trim();
            String answer4 = answer4EditText.getText().toString().trim();

            int correctAnswerId = correctAnswerGroup.getCheckedRadioButtonId();
            boolean isAnswer1Correct = (correctAnswerId == R.id.correctAnswer1);
            boolean isAnswer2Correct = (correctAnswerId == R.id.correctAnswer2);
            boolean isAnswer3Correct = (correctAnswerId == R.id.correctAnswer3);
            boolean isAnswer4Correct = (correctAnswerId == R.id.correctAnswer4);

            List<Answer> answers = new ArrayList<>();
            answers.add(new Answer(answer1, isAnswer1Correct));
            answers.add(new Answer(answer2, isAnswer2Correct));
            answers.add(new Answer(answer3, isAnswer3Correct));
            answers.add(new Answer(answer4, isAnswer4Correct));

            questionList.add(new Question(questionText, answers));
            questionAdapter.notifyDataSetChanged();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveQuiz() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Failed to get user details", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = user.getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String username = document.getString("name");

                    String quizTitle = quizTitleEditText.getText().toString().trim();
                    if (quizTitle.isEmpty()) {
                        Toast.makeText(this, "Quiz title is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String quizType = quizTypeSpinner.getSelectedItem().toString();

                    // Save the quiz
                    CollectionReference quizRef = db.collection("quiz");
                    Map<String, Object> quizData = new HashMap<>();
                    quizData.put("title", quizTitle);
                    quizData.put("createdBy", username);
                    quizData.put("type", quizType);

                    quizRef.add(quizData).addOnSuccessListener(documentReference -> {
                        String quizId = documentReference.getId();

                        // Save the questions
                        for (Question question : questionList) {
                            CollectionReference questionRef = db.collection("question");
                            Map<String, Object> questionData = new HashMap<>();
                            questionData.put("questionText", question.getQuestion());
                            questionData.put("quizId", db.document("quiz/" + quizId));

                            questionRef.add(questionData).addOnSuccessListener(questionDocRef -> {
                                String questionId = questionDocRef.getId();

                                // Save the answers
                                for (Answer answer : question.getAnswers()) {
                                    CollectionReference answerRef = db.collection("answer");
                                    Map<String, Object> answerData = new HashMap<>();
                                    answerData.put("answerText", answer.getAnswer_text());
                                    answerData.put("isCorrect", answer.isIs_correct());
                                    answerData.put("questionId", db.document("question/" + questionId));

                                    answerRef.add(answerData).addOnFailureListener(e -> {
                                        Log.e("CreateQuizActivity", "Error adding answer", e);
                                        Toast.makeText(this, "Failed to save answer", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }).addOnFailureListener(e -> {
                                Log.e("CreateQuizActivity", "Error adding question", e);
                                Toast.makeText(this, "Failed to save question", Toast.LENGTH_SHORT).show();
                            });
                        }

                        Toast.makeText(this, "Quiz saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Log.e("CreateQuizActivity", "Error adding quiz", e);
                        Toast.makeText(this, "Failed to save quiz", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.e("CreateQuizActivity", "User document does not exist");
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("CreateQuizActivity", "Error getting user document", task.getException());
                Toast.makeText(this, "Failed to get user details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
