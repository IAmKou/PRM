package com.example.jila;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import model.Report;

public class ReportDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);

        Report report = getIntent().getParcelableExtra("report");
        // Use the report object to populate your views
        if (report != null) {

            TextView reporterTextView = findViewById(R.id.reporter);
            TextView reportTypeTextView = findViewById(R.id.type_name);
            TextView quizTitleTextView = findViewById(R.id.quiz_name);
            TextView questionTextView = findViewById(R.id.question_text);
            TextView timeTextView = findViewById(R.id.report_time);

            reporterTextView.setText(report.getReporter());
            reportTypeTextView.setText(report.getReport_type());
            quizTitleTextView.setText(report.getQuizTitle());
            questionTextView.setText(report.getQuestionText());
            timeTextView.setText(report.getReport_time().toDate().toString());
        }
    }
}
