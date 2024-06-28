package com.example.jila;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import model.Report;

public class ReportListActivity extends AppCompatActivity {
    private RecyclerView rv1;
    private ReportAdapter ra;
    private List<Report> reportList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        Log.d("ReportListActivity", "go to onCreate");

        db = FirebaseFirestore.getInstance();
        DocumentReference orderRef = db.collection("report").document("ZiiWidLCaWjPnp40wBIp");

        rv1 = findViewById(R.id.recyclerViewReports);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        ra = new ReportAdapter(reportList);
        rv1.setAdapter(ra);

        orderRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Long report_id = documentSnapshot.getLong("report_id");
                    Timestamp report_time = documentSnapshot.getTimestamp("time");

                    DocumentReference userRef = db.collection("user").document("4ssHDLTX3Hmcog4CVpWR");
                    DocumentReference typeRef = db.collection("report_type").document("qK4dMJsM7SRMyQO4b1pd");
                    DocumentReference quizRef = db.collection("quiz").document("oH4sUjoOfnpanOouOMG8");

                    fetchReferenceData(userRef, typeRef, quizRef, report_id, report_time);
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        });
    }

    private void fetchReferenceData(DocumentReference userRef, DocumentReference typeRef, DocumentReference quizRef, Long report_id, Timestamp report_time) {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult();
                    if (userDoc.exists()) {
                        String reporter = userDoc.getString("name");
                        typeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot typeDoc = task.getResult();
                                    if (typeDoc.exists()) {
                                        String report_type = typeDoc.getString("type_name");
                                        quizRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot quizDoc = task.getResult();
                                                    if (quizDoc.exists()) {
                                                        Long quiz_id = quizDoc.getLong("quiz_id");

                                                        // Creating ReportItem and adding to the list
                                                        Report reportItem = new Report(report_id, report_time, reporter, quiz_id, report_type);
                                                        reportList.add(reportItem);

                                                        // Notifying adapter about data change
                                                        ra.notifyDataSetChanged();
                                                    } else {
                                                        Log.d(TAG, "No document named quiz found");
                                                    }
                                                } else {
                                                    Log.d(TAG, "Failed to get quiz: ", task.getException());
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "No document named type found");
                                    }
                                } else {
                                    Log.d(TAG, "Failed to get type: ", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "No document named user found");
                    }
                } else {
                    Log.d(TAG, "Failed to get user: ", task.getException());
                }
            }
        });
    }
}
