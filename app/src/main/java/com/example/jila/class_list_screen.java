package com.example.jila;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class class_list_screen extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<Class> classList = new ArrayList<>();
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_list_screen);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.RecyclerViewClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        quizAdapter = new QuizAdapter(this, classList);
//        recyclerView.setAdapter(quizAdapter);
//
//        backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> finish());
    }
}