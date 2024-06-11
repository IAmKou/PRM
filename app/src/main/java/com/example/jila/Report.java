package com.example.jila;
//alo ok
import java.util.Date;

public class Report {
    private int report_id;
    private Date report_time;
    private int reporter;
    private int quiz_id;
    private int report_text;

    public Report(int report_id, Date report_time, int reporter, int quiz_id, int report_text){
        this.report_id = report_id;
        this.report_time = report_time;
        this.reporter = reporter;
        this.quiz_id = quiz_id;
        this.report_text = report_text;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public Date getReport_time() {
        return report_time;
    }

    public void setReport_time(Date report_time) {
        this.report_time = report_time;
    }

    public int getReporter() {
        return reporter;
    }

    public void setReporter(int reporter) {
        this.reporter = reporter;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getReport_text() {
        return report_text;
    }

    public void setReport_text(int report_text) {
        this.report_text = report_text;
    }
}
