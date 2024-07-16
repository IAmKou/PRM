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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jila.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                startActivity(new Intent(MainActivity.this, Setting_screen.class));
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

