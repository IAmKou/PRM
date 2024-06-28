package model;

import java.util.List;

public class Question {
    private String question;
    private Long question_id;
    private List<Answer> answers;


    public Question(String question, Long question_id, List<Answer> answers) {
        this.question = question;
        this.question_id = question_id;
        this.answers = answers;

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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
