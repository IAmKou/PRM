package com.example.jila.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jila.R;
import com.example.jila.model.Report;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;
    private SimpleDateFormat dateFormat;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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
        holder.reportDate.setText(dateFormat.format(report.getReport_time()));
        holder.reporter.setText(String.valueOf(report.getReporter()));
        holder.quizId.setText(String.valueOf(report.getQuiz_id()));
        holder.reportText.setText(String.valueOf(report.getReport_text()));
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
            reportText = itemView.findViewById(R.id.textViewReportText);
        }
    }
}
