package com.project.diary.Model.Lock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.project.diary.View.Activity.MyLockScreenActivity;

import p32929.easypasscodelock.Activities.LockscreenActivity;
import p32929.easypasscodelock.Interfaces.ActivityChanger;
import p32929.easypasscodelock.Utils.EasyLock;
import p32929.easypasscodelock.Utils.FayazSP;

public class MyLock extends EasyLock {
    private static ActivityChanger activityChanger;
    public static int backgroundColor = Color.parseColor("#019689");
    public static View.OnClickListener onClickListener;

    public static final String FORGOT_PASS_MODE = "FORGOT_PASS_MODE";
    public static boolean hasPassword;

    private static void init(Context context) {
        FayazSP.init(context);
        if (activityChanger == null) {
            activityChanger = new MyLockScreenActivity();
        }
    }

    public static void setPassword(Context context, Class activityClassToGo) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, MyLockScreenActivity.class);
        intent.putExtra("passStatus", "set");
        context.startActivity(intent);
    }

    public static void setPassword(Context context) {
        init(context);
        Intent intent = new Intent(context, MyLockScreenActivity.class);
        intent.putExtra("passStatus", "set");
        context.startActivity(intent);
    }
    public static void changePassword(Context context, Class activityClassToGo) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, MyLockScreenActivity.class);
        intent.putExtra("passStatus", "change");
        context.startActivity(intent);
    }

    public static void changePassword(Context context) {
        init(context);
        Intent intent = new Intent(context, MyLockScreenActivity.class);
        intent.putExtra("passStatus", "change");
        context.startActivity(intent);
    }

    public static void disablePassword(Context context, Class activityClassToGo) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, MyLockScreenActivity.class);
        intent.putExtra("passStatus", "disable");
        context.startActivity(intent);
    }

    public static void checkPassword(Context context) {
        init(context);
        if (FayazSP.getString("password", null) != null) {
            Intent intent = new Intent(context, MyLockScreenActivity.class);
            intent.putExtra("passStatus", "check");
            context.startActivity(intent);
            hasPassword = true;
        }else{
            hasPassword = false;
        }
    }
    public static void checkPasswordNoTask(Context context) {
        init(context);
        if (FayazSP.getString("password", null) != null) {
            hasPassword = true;
        }else{
            hasPassword = false;
        }
    }



    public static void setBackgroundColor(int backgroundColor) {
        backgroundColor = backgroundColor;
    }

    public static void forgotPassword(View.OnClickListener onClickListener) {
        EasyLock.onClickListener = onClickListener;
    }

}
