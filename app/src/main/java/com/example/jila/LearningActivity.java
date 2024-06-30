package com.example.jila;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LearningActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Flashcard> flashcardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        db = FirebaseFirestore.getInstance();

        db.collection("quizzes").document("quizId")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> questions = (List<Map<String, Object>>) documentSnapshot.get("questions");
                        for (Map<String, Object> question : questions) {
                            flashcardList.add(new Flashcard(question.get("question").toString(), question.get("answer").toString()));
                        }
                        // Hiển thị các flashcards
                    }
                });
    }
}
