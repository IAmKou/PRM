package com.example.jila;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReportDetail extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);

        TextView reportIdTextView = findViewById(R.id.rid);
        TextView reportTimeTextView = findViewById(R.id.rt);
        TextView reporterTextView = findViewById(R.id.reid);
        TextView quizIdTextView = findViewById(R.id.qid);
        TextView reportTypeTextView = findViewById(R.id.tid);

        // Get the data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            int reportId = intent.getIntExtra("report_id", -1); // Default to -1 if not found
            String reportTime = intent.getStringExtra("report_time");
            String reporter = intent.getStringExtra("reporter");
            int quizId = intent.getIntExtra("quiz_id", -1); // Default to -1 if not found
            String reportType = intent.getStringExtra("report_type");

            // Set the retrieved data to the TextViews
            reportIdTextView.setText(String.valueOf(reportId));
            reportTimeTextView.setText(reportTime);
            reporterTextView.setText(reporter);
            quizIdTextView.setText(String.valueOf(quizId));
            reportTypeTextView.setText(reportType);
        }
    }
}
