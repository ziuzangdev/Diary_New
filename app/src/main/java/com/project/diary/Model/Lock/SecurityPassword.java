package com.project.diary.Model.Lock;

import android.content.Context;
import android.util.Log;

import com.project.diary.Control.Lock.SecurityPasswordControl;

public class SecurityPassword implements ISecurityPassword{
    private String QUESTION;
    private String ANSWER;
    private SecurityPasswordControl control;

    private boolean hasPassword;

    @Override
    public boolean isHasPassword() {
        return hasPassword;
    }
    @Override
    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    @Override
    public String getQUESTION() {
        return QUESTION;
    }

    @Override
    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    @Override
    public String getANSWER() {
        return ANSWER;
    }

    @Override
    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }

    public SecurityPassword() {
    }

    public SecurityPassword(Context context) {
        control = new SecurityPasswordControl(context);
        ISecurityPassword securityPassword = control.getSqLite().getSqLiteControl().getSecurityPassword();
        if(securityPassword != NON_VALUE){
            initQuestionAndPassword(securityPassword.getQUESTION(), securityPassword.getANSWER());
            hasPassword = true;
        }else{
            hasPassword = false;
            Log.i("SecurityPassword:", "Non Value");
        }
    }


    @Override
    public void initQuestionAndPassword(String QUESTION, String ANSWER) {
        this.QUESTION = QUESTION;
        this.ANSWER = ANSWER;
        hasPassword = true;
    }

    @Override
    public void saveQuestionAndPassword(ISecurityPassword securityPassword) {
        control.getSqLite().getSqLiteControl().updateSecurityPassword(securityPassword);
        hasPassword = true;
    }
}
