package com.project.diary.Model.Lock;

public class SecurityPasswordGson {
    private String QUESTION;
    private String ANSWER;

    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }

    public SecurityPasswordGson() {
    }

    public SecurityPasswordGson(String QUESTION, String ANSWER) {
        this.QUESTION = QUESTION;
        this.ANSWER = ANSWER;
    }
}
