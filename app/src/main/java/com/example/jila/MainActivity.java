package com.example.jila;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button b = findViewById(R.id.btn2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReportListActivity.class);
                startActivity(intent);

            }
        });

    }
    public void openExam(View view) {
        startActivity(new Intent(this, ExamActivity.class));
    }

    public void openQuizList(View view) {
        startActivity(new Intent(this, QuizListActivity.class));
    }

    public void openLearning(View view) {
        startActivity(new Intent(this, LearningActivity.class));
    }

    public void openUserList(View view) {
        startActivity(new Intent(this, UserListActivity.class));
    }
}
