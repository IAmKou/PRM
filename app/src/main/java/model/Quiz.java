package model;

public class Quiz {
    private String creator_name;
    private String quiz_name;
    private String type;



    public Quiz(String creator_name, String quiz_name, String type) {
        this.creator_name = creator_name;
        this.quiz_name = quiz_name;
        this.type = type;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
