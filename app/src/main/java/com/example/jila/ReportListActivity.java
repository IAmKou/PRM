package com.example.jila;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize reportList
        reportList = new ArrayList<>();

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewReports);
        adapter = new ReportAdapter(reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchReports();
    }

    private void fetchReports() {
        db.collection("report")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp report_time = document.getTimestamp("time");
                                Object reporterField = document.get("reporter");
                                Object reportTypeField = document.get("report_type");
                                Object quizIdField = document.get("quiz_id");

                                if (reporterField instanceof DocumentReference && reportTypeField instanceof DocumentReference && quizIdField instanceof DocumentReference) {
                                    DocumentReference reporterRef = (DocumentReference) reporterField;
                                    DocumentReference reportTypeRef = (DocumentReference) reportTypeField;
                                    DocumentReference quizRef = (DocumentReference) quizIdField;

                                    fetchReferencedFields(report_time, reporterRef, reportTypeRef, quizRef);
                                } else {
                                    Log.d("ReportListActivity", "Invalid reference types");
                                }
                            }
                        } else {
                            Log.d("ReportListActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void fetchReferencedFields(Timestamp report_time, DocumentReference reporterRef, DocumentReference reportTypeRef, DocumentReference quizRef) {
        reporterRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");

                        fetchTypeAndQuizFields(report_time, name, reportTypeRef, quizRef);
                    } else {
                        Log.d("ReportListActivity", "No user document found for reporter: " + reporterRef.getId());
                    }
                } else {
                    Log.d("ReportListActivity", "Error getting reporter: ", task.getException());
                }
            }
        });
    }

    private void fetchTypeAndQuizFields(Timestamp report_time, String name, DocumentReference typeRef, DocumentReference quizRef) {
        typeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String type_name = document.getString("type_name");

                        quizRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String quiz_name = document.getString("title");

                                        Report report = new Report(report_time, name, type_name, quiz_name);
                                        reportList.add(report);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("ReportListActivity", "No quiz document found for quizId: " + quizRef.getId());
                                    }
                                } else {
                                    Log.d("ReportListActivity", "Error getting quiz: ", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.d("ReportListActivity", "No report type document found for typeId: " + typeRef.getId());
                    }
                } else {
                    Log.d("ReportListActivity", "Error getting report type: ", task.getException());
                }
            }
        });
    }
}
