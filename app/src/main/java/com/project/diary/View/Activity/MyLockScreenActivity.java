package com.project.diary.View.Activity;

import android.os.Bundle;

import com.project.diary.Control.Activity.MainActivityControl;

import p32929.easypasscodelock.Activities.LockscreenActivity;

public class MyLockScreenActivity extends LockscreenActivity {
    MainActivityControl control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new MainActivityControl(MyLockScreenActivity.this);
        control.showCustomUI();
    }
}
