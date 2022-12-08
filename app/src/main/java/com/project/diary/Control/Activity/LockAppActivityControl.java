package com.project.diary.Control.Activity;

import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.Lock.ISecurityPassword;
import com.project.diary.Model.Lock.SecurityPassword;
import com.project.diary.Model.SQLite.SQLite;

public class LockAppActivityControl extends RootControl {
    private boolean isLockMode;

    private SQLite sqLite;

    private ISecurityPassword iSecurityPassword;

    public ISecurityPassword getiSecurityPassword() {
        return iSecurityPassword;
    }

    public void setiSecurityPassword(ISecurityPassword iSecurityPassword) {
        this.iSecurityPassword = iSecurityPassword;
    }

    public LockAppActivityControl(Context context) {
        super(context);
        sqLite = new SQLite(context);
        isLockMode = sqLite.getSqLiteControl().isLockMode();
        iSecurityPassword = new SecurityPassword(getContext());
    }

    public boolean isLockMode() {
        return isLockMode;
    }

    public void setLockMode(boolean lockMode) {
        isLockMode = lockMode;
        if(lockMode){
            sqLite.getSqLiteControl().updateLockMode(1);
        }else{
            sqLite.getSqLiteControl().updateLockMode(0);
        }
    }
}
