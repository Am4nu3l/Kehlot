package com.example.examlab.Objects;

public class Question {
    private String usrName;
    private String usrId;
    private String questionId;
    private String content;

    public Question(String usrName, String usrId, String questionId, String content) {
        this.usrName = usrName;
        this.usrId = usrId;
        this.questionId = questionId;
        this.content = content;
    }

    public String getUsrName() {
        return usrName;
    }

    public String getUsrId() {
        return usrId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
