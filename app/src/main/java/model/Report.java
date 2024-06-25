package model;
//alo
import com.google.firebase.Timestamp;

import java.util.Date;

public class Report {
    private Long report_id;
    private Timestamp report_time;
    private String reporter;
    private Long quiz_id;
    private String report_type;

    public Report(Long report_id, Timestamp report_time, String reporter, Long quiz_id,  String report_type) {
        this.report_id = report_id;
        this.report_time = report_time;
        this.reporter = reporter;
        this.quiz_id = quiz_id;
        this.report_type = report_type;
    }

    public void setReport_id(Long report_id) {
        this.report_id = report_id;
    }

    public void setReport_time(Timestamp report_time) {
        this.report_time = report_time;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setQuiz_id(Long quiz_id) {
        this.quiz_id = quiz_id;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public Long getReport_id() {
        return report_id;
    }

    public Timestamp getReport_time() {
        return report_time;
    }

    public String getReporter() {
        return reporter;
    }

    public Long getQuiz_id() {
        return quiz_id;
    }

    public String getReport_type() {
        return report_type;
    }
}