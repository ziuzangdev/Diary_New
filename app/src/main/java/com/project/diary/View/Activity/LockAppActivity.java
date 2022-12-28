package com.project.diary.View.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.project.diary.Control.Activity.LockAppActivityControl;
import com.project.diary.Model.Lock.ISecurityPassword;
import com.project.diary.Model.Lock.MyLock;
import com.project.diary.Model.Lock.SecurityPassword;
import com.project.diary.databinding.ActivityLockAppBinding;

import p32929.easypasscodelock.Utils.LockscreenHandler;

public class LockAppActivity extends LockscreenHandler {
    private ActivityLockAppBinding binding;

    private LockAppActivityControl control;

    private ISecurityPassword securityPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockAppBinding.inflate(getLayoutInflater());
        control = new LockAppActivityControl(LockAppActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControl();
        addEvents();
    }



    private void addEvents() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.llSecurityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LockAppActivity.this, SecurityPasswordActivity.class);
                startActivity(intent);
            }
        });
        binding.llSettupPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLock.checkPasswordNoTask(LockAppActivity.this);
                if(MyLock.hasPassword){
                    MyLock.changePassword(LockAppActivity.this);
                }else{
                    MyLock.setPassword(LockAppActivity.this);
                }
            }
        });
        binding.swEnablePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                control.setLockMode(isChecked);
              initCurrentStateSwEnablePassword(isChecked);
            }
        });

        MyLock.forgotPassword(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addControl() {
            binding.swEnablePassword.setChecked(control.isLockMode());
            initCurrentStateSwEnablePassword(control.isLockMode());
    }

    private void initCurrentStateSwEnablePassword(boolean isChecked) {
        if(isChecked){
            binding.llSettupPassword.setVisibility(View.VISIBLE);
            binding.llSecurityQuestion.setVisibility(View.VISIBLE);
        }else{
            binding.llSettupPassword.setVisibility(View.GONE);
            binding.llSecurityQuestion.setVisibility(View.GONE);
        }
    }

}