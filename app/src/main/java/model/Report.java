package model;

import android.os.Parcelable;
import android.os.Parcel;
import com.google.firebase.Timestamp;

public class Report  implements Parcelable{
    private Timestamp report_time;
    private String reporter;
    private String report_type;
    private String quizTitle;
    private String questionText;

    public Report(Timestamp report_time, String reporter, String report_type, String quizTitle, String questionText) {
        this.report_time = report_time;
        this.reporter = reporter;
        this.report_type = report_type;
        this.quizTitle = quizTitle;
        this.questionText = questionText;
    }
    protected Report(Parcel in) {
        reporter = in.readString();
        report_type = in.readString();
        quizTitle = in.readString();
        questionText = in.readString();
        report_time = new Timestamp(in.readLong(), in.readInt());
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reporter);
        dest.writeString(report_type);
        dest.writeString(quizTitle);
        dest.writeString(questionText);
        dest.writeLong(report_time.getSeconds());
        dest.writeInt(report_time.getNanoseconds());
    }

    public Timestamp getReport_time() {
        return report_time;
    }

    public void setReport_time(Timestamp report_time) {
        this.report_time = report_time;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
