package model;

import java.util.List;

public class Question {
    private String question;
    private List<Answer> answers;


    public Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isCorrectAnswer(String selectedAnswer) {
        for (Answer answer : answers) {
            if (answer.getAnswer_text().equals(selectedAnswer) && answer.isIs_correct()) {
                return true;
            }
        }
        return false;
    }
}
