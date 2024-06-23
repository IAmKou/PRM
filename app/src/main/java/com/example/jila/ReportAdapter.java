package com.example.jila;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;
    private SimpleDateFormat dateFormat;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.reportId.setText(String.valueOf(report.getReport_id()));

        // Convert Timestamp to Date and format it
        Timestamp timestamp = report.getReport_time();
        Date date = timestamp.toDate();
        holder.reportDate.setText(dateFormat.format(date));

        holder.reporter.setText(report.getReporter());
        holder.quizId.setText(String.valueOf(report.getQuiz_id()));
        holder.reportText.setText(report.getReport_type());
    }
    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView reportId;
        TextView reportDate;
        TextView reporter;
        TextView quizId;
        TextView reportText;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportId = itemView.findViewById(R.id.textViewReportId);
            reportDate = itemView.findViewById(R.id.textViewReportDate);
            reporter = itemView.findViewById(R.id.textViewReporter);
            quizId = itemView.findViewById(R.id.textViewQuizId);
            reportText = itemView.findViewById(R.id.textViewReportText); // Initialize the reportText TextView
        }
    }
}
