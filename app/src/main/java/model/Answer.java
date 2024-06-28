package model;

public class Answer {
    private Long answer_id;
    private String answer_text;
    private boolean is_correct;


    public Answer(Long answer_id, String answer_text, boolean is_correct) {
        this.answer_id = answer_id;
        this.answer_text = answer_text;
        this.is_correct = is_correct;
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

}
