//package com.example.jila;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.jila.databinding.ActivityMainBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class MainActivity extends AppCompatActivity {
//    ActivityMainBinding binding;
//    FirebaseAuth auth;
//
//    @Override
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        auth = FirebaseAuth.getInstance();
//
//        setContentView(R.layout.activity_main);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Button b1 = findViewById(R.id.button);
//        Button b2 = findViewById(R.id.btn2);
//        Button changePassButton = findViewById(R.id.changePass);
//
//        changePassButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ChangeActivity.class));
//            }
//        });
//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ReportListActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CreateQuizActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    public void openExam(View view) {
//        startActivity(new Intent(this, ExamActivity.class));
//    }
//
//    public void openQuizList(View view) {
//        startActivity(new Intent(this, QuizListActivity.class));
//    }
//
//    public void openLearning(View view) {
//        startActivity(new Intent(this, LearningActivity.class));
//    }
//
//    public void openUserList(View view) {
//        startActivity(new Intent(this, UserListActivity.class));
//    }
//}
//
package com.example.jila;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jila.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.util.Log;
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangeActivity.class));
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReportListActivity.class);
                startActivity(intent);
            }
        });

        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQuizActivity.class);
                startActivity(intent);
            }
        });

        binding.ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Setting.class));
            }
        });

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = binding.etSearch.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            Log.d("performSearch", "Search term is empty");
            return;
        }

        Log.d("performSearch", "Searching for: " + query);
        db.collection("quiz")
                .whereEqualTo("title", query)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Quiz quiz = document.toObject(Quiz.class);
                                // Handle the quiz object here, for example, show in a list or start a new activity
                                // Here we just show a toast message with the quiz title
                                Toast.makeText(MainActivity.this, "Found: " + quiz.getTitle(), Toast.LENGTH_SHORT).show();
                                Log.d("performSearch", "Found quiz: " + quiz.getTitle());
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No quizzes found", Toast.LENGTH_SHORT).show();
                            Log.d("performSearch", "No quizzes found for query: " + query);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                        Log.e("performSearch", "Search failed", task.getException());
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
