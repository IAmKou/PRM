package com.example.jila;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import model.Report;

public class ReportDetailActivity extends AppCompatActivity {
    public static final String EXTRA_REPORT = "com.example.jila.REPORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);

        Report report = getIntent().getParcelableExtra(EXTRA_REPORT);

        if (report != null) {
            TextView reporterTextView = findViewById(R.id.reporter);
            TextView typeIdTextView = findViewById(R.id.type_id);
            TextView quizIdTextView = findViewById(R.id.quiz_id);
            TextView reportTimeTextView = findViewById(R.id.report_time);

            reporterTextView.setText(report.getReporter());
            typeIdTextView.setText(report.getTypeName());
            quizIdTextView.setText(report.getQuizName());
            reportTimeTextView.setText(report.getReport_time().toDate().toString());
        }
    }
}
