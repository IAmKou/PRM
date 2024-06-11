package com.example.jila;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jila.Adapter.ReportAdapter;
import com.example.jila.model.Report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportListActivity extends AppCompatActivity {
    private RecyclerView rv1;
    private ReportAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        rv1 = findViewById(R.id.recyclerViewReports);
        rv1.setLayoutManager(new LinearLayoutManager(this));

        List<Report> reportList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date reportDate = dateFormat.parse("25-11-2022");
            reportList.add(new Report(1, reportDate, 1, 1, 1));

        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        ra = new ReportAdapter(reportList);
        rv1.setAdapter(ra);
    }
}