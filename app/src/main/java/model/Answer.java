package model;

public class Answer {
    private Long answer_id;
    private String answer_text;
    private boolean is_correct;
    private Long question_id;

    public Answer(Long answer_id, String answer_text, boolean is_correct, Long question_id) {
        this.answer_id = answer_id;
        this.answer_text = answer_text;
        this.is_correct = is_correct;
        this.question_id = question_id;
    }

    public Long getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Long answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public boolean isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }
}
