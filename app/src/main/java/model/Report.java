package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Report implements Parcelable {
    private Timestamp report_time;
    private String reporter;
    private String typeName;
    private String quizName;

    public Report(Timestamp report_time, String reporter, String typeName, String quizName) {
        this.report_time = report_time;
        this.reporter = reporter;
        this.typeName = typeName;
        this.quizName = quizName;
    }

    protected Report(Parcel in) {
        report_time = in.readParcelable(Timestamp.class.getClassLoader());
        reporter = in.readString();
        typeName = in.readString();
        quizName = in.readString();
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

    public Timestamp getReport_time() {
        return report_time;
    }

    public String getReporter() {
        return reporter;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getQuizName() {
        return quizName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(report_time, flags);
        dest.writeString(reporter);
        dest.writeString(typeName);
        dest.writeString(quizName);
    }
}
