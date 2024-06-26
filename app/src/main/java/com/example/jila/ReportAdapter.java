package com.example.jila;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.Report;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private static List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
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
        holder.bind(report);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView reporterTextView;
        private TextView typeNameTextView;
        private TextView quizNameTextView;
        private Button button;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reporterTextView = itemView.findViewById(R.id.reporter);
            typeNameTextView = itemView.findViewById(R.id.type_name);
            quizNameTextView = itemView.findViewById(R.id.quiz_name);
            button = itemView.findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Report report = reportList.get(position);
                        Intent intent = new Intent(v.getContext(), ReportDetailActivity.class);
                        intent.putExtra("report", report);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(Report report) {
            reporterTextView.setText(report.getReporter());
            typeNameTextView.setText(report.getReport_type());
            quizNameTextView.setText(report.getQuizTitle());
        }
    }
}
