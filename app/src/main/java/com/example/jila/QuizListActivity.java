package com.example.jila;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class QuizListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList = new ArrayList<>();
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewQuizzes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter = new QuizAdapter(this, quizList);
        recyclerView.setAdapter(quizAdapter);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        db.collection("quiz")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Quiz quiz = documentSnapshot.toObject(Quiz.class);
                        quiz.setId(documentSnapshot.getId());
                        quizList.add(quiz);
                    }
                    quizAdapter.notifyDataSetChanged();
                });
    }
}
