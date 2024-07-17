package com.example.jila;

import static com.example.jila.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        Button uList = findViewById(R.id.UserList_btn);
        Button quizList = findViewById(R.id.Q_btn);
        Button reportList = findViewById(R.id.rp_btn);


        uList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, UserListActivity.class);
                startActivity(intent);
            }
        });

        quizList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, QuizListActivity.class);
                startActivity(intent);
            }
        });

        reportList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, ReportListActivity.class);
                startActivity(intent);
            }
        });
    }
}