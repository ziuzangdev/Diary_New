package com.project.diary.View.Activity;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.R;

import p32929.easypasscodelock.Activities.LockscreenActivity;
import p32929.easypasscodelock.Interfaces.ActivityChanger;
import p32929.easypasscodelock.Utils.EasyLock;
import p32929.easypasscodelock.Utils.FayazSP;
import p32929.easypasscodelock.Utils.LockscreenHandler;

public class MyLockScreenActivity extends LockscreenHandler implements ActivityChanger, IThemeManager {
    private static Class classToGoAfter;
    String tempPass = "";
    private int[] passButtonIds = {
            R.id.lbtn1,
            R.id.lbtn2,
            R.id.lbtn3,
            R.id.lbtn4,
            R.id.lbtn5,
            R.id.lbtn6,
            R.id.lbtn7,
            R.id.lbtn8,
            R.id.lbtn9,
            R.id.lbtn0
    };
    private TextView textViewDot, textViewHAHA, textViewForgotPassword;
    private Button buttonTick;
    private ImageButton imageButtonDelete;
    private RelativeLayout relativeLayoutBackground;

    private String passString = "", realPass = "";
    private String status = "";
    //
    private String checkStatus = "check";
    private String setStatus = "set";
    private String setStatus1 = "set1";
    private String disableStatus = "disable";
    private String changeStatus = "change";
    private String changeStatus1 = "change1";
    private String changeStatus2 = "change2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        setContentView(R.layout.activity_easy_lockscreen);
        FayazSP.init(this);
        realPass = getPassword();
        initViews();
        initTheme();
        status = getIntent().getExtras().getString("passStatus", "check");
        if (status.equals(setStatus))
            textViewHAHA.setText("Enter a New Password");
        if (status.equals(disableStatus)) {
            FayazSP.put("password", null);
            Toast.makeText(this, "Password Disabled", Toast.LENGTH_SHORT).show();
            gotoActivity();
        }

    }

    private void initViews() {
        textViewHAHA = findViewById(R.id.haha_text);
        textViewDot = findViewById(R.id.dotText);
        textViewForgotPassword = findViewById(R.id.forgot_pass_textview);
        buttonTick = findViewById(R.id.lbtnTick);
        imageButtonDelete = findViewById(R.id.lbtnDelete);
        relativeLayoutBackground = findViewById(R.id.background_layout);
        relativeLayoutBackground.setBackgroundColor(EasyLock.backgroundColor);

        textViewForgotPassword.setOnClickListener(EasyLock.onClickListener);

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passString.length() > 0)
                    passString = passString.substring(0, passString.length() - 1);
                textViewDot.setText(passString);
            }
        });

        buttonTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                if (status.equals(checkStatus)) {
                    if (passString.equals(realPass)) {
                        finish();
                    } else {
                        passString = "";
                        textViewDot.setText(passString);
                        Toast.makeText(MyLockScreenActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }

                //
                else if (status.equals(setStatus)) {
                    //
                    tempPass = passString;
                    passString = "";
                    status = setStatus1;

                    textViewHAHA.setText("Confirm Password");
                    textViewDot.setText(passString);
                }

                //
                else if (status.equals(setStatus1)) {
                    //
                    if (passString.equals(tempPass)) {
                        FayazSP.put("password", passString);
                        Toast.makeText(MyLockScreenActivity.this, "Password is set", Toast.LENGTH_SHORT).show();
                        gotoActivity();
                    } else {

                        tempPass = passString;
                        passString = "";
                        tempPass = "";
                        status = setStatus;

                        textViewDot.setText(passString);
                        textViewHAHA.setText("Enter a New Password");
                        Toast.makeText(MyLockScreenActivity.this, "Please Enter a New Password Again", Toast.LENGTH_SHORT).show();
                    }
                }

                //
                else if (status.equals(changeStatus)) {
                    if (passString.equals(realPass)) {
                        tempPass = passString;
                        passString = "";
                        tempPass = "";
                        status = changeStatus1;
                        textViewHAHA.setText("Enter a New Password");
                        textViewDot.setText(passString);
                    } else {
                        passString = "";
                        textViewDot.setText(passString);
                        Toast.makeText(MyLockScreenActivity.this, "Please Enter Current Password", Toast.LENGTH_SHORT).show();
                    }
                }

                //
                else if (status.equals(changeStatus1)) {
                    tempPass = passString;
                    passString = "";
                    status = changeStatus2;

                    textViewHAHA.setText("Confirm Password");
                    textViewDot.setText(passString);
                }

                //
                else if (status.equals(changeStatus2)) {
                    if (passString.equals(tempPass)) {
                        FayazSP.put("password", passString);
                        Toast.makeText(MyLockScreenActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        gotoActivity();
                    } else {

                        tempPass = passString;
                        passString = "";
                        tempPass = "";
                        status = changeStatus1;

                        textViewDot.setText(passString);
                        textViewHAHA.setText("Enter a New Password");
                        Toast.makeText(MyLockScreenActivity.this, "Please Enter a New Password Again", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        for (int i = 0; i < passButtonIds.length; i++) {
            final Button button = findViewById(passButtonIds[i]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (passString.length() >= 8) {
                        Toast.makeText(MyLockScreenActivity.this, "Max 8 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        passString += button.getText().toString();
                    }
                    textViewDot.setText(passString);
                }
            });
        }
    }

    private String getPassword() {
        return FayazSP.getString("password", null);
    }

    private void gotoActivity() {
        finish();
    }

    @Override
    public void activityClass(Class activityClassToGo) {
        classToGoAfter = activityClassToGo;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (status.equals("check")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                ActivityCompat.finishAffinity(this);
            }
        }
    }

    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = new AppThemeManager(MyLockScreenActivity.this);
        relativeLayoutBackground.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
    }
}
