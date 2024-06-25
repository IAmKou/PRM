package model;

public class Quiz {
    private Long creator_id;
    private Long quiz_id;
    private String quiz_name;
    private String type;

    public Quiz(Long creator_id, Long quiz_id, String quiz_name, String type) {
        this.creator_id = creator_id;
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.type = type;
    }

    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }

    public void setQuiz_id(Long quiz_id) {
        this.quiz_id = quiz_id;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreator_id() {
        return creator_id;
    }

    public Long getQuiz_id() {
        return quiz_id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public String getType() {
        return type;
    }
}
