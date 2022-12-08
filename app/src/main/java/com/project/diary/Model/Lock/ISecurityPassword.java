package com.project.diary.Model.Lock;

public interface ISecurityPassword {
    public final static SecurityPassword NON_VALUE = null;

    public boolean isHasPassword();

    public void setHasPassword(boolean hasPassword);

    public String getQUESTION();

    public void setQUESTION(String QUESTION);

    public String getANSWER();

    public void setANSWER(String ANSWER);

    public void initQuestionAndPassword(String QUESTION, String ANSWER);

    public void saveQuestionAndPassword(ISecurityPassword securityPassword);
}
