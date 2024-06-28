package model;

public class Question {
    private String question;
    private Long question_id;
    private Long quiz_id;

    public Question(String question, Long question_id, Long quiz_id) {
        this.question = question;
        this.question_id = question_id;
        this.quiz_id = quiz_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public Long getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Long quiz_id) {
        this.quiz_id = quiz_id;
    }
}
