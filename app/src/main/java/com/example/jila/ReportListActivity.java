package com.example.jila;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Report;

public class ReportListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Report> reportList;
    private ReportAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        db = FirebaseFirestore.getInstance();

        reportList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewReports);
        adapter = new ReportAdapter(reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getDataFromFirebase();



    }
    private void getDataFromFirebase(){
        db.collection("report").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String reporterField = document.getString("reporter");
                        String reportTypeField = document.getString("report_type");
                        String quizTitleField = document.getString("quizTitle");
                        Timestamp timestamp = document.getTimestamp("time");
                        String questionTextField = document.getString("questionText");

                        Report report = new Report(timestamp, reporterField, reportTypeField, quizTitleField, questionTextField);
                        reportList.add(report);

                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Log.d("ReportListActivity", "Error getting documents: ", task.getException());
                }

            }
        });


    }




}
