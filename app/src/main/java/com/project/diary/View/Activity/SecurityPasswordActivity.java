package com.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.diary.Control.Activity.LockAppActivityControl;
import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.Model.Lock.ISecurityPassword;
import com.project.diary.Model.Lock.MyLock;
import com.project.diary.Model.Lock.SecurityPassword;
import com.project.diary.R;
import com.project.diary.databinding.ActivityLockAppBinding;
import com.project.diary.databinding.ActivitySecurityPasswordBinding;

public class SecurityPasswordActivity extends AppCompatActivity {
    private ActivitySecurityPasswordBinding binding;
    private LockAppActivityControl control;

    private String MODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityPasswordBinding.inflate(getLayoutInflater());
        control =  new LockAppActivityControl(SecurityPasswordActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {

        binding.mbtnSaveSecurityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QUESTION = binding.edtxtQuestion.getText().toString();
                String ANSWER = binding.edtxtAnswer.getText().toString();
                if(MODE == null){
                    if(QUESTION != null && ANSWER != null){
                        if(!QUESTION.equals("") && !ANSWER.equals("")){
                            control.getiSecurityPassword().initQuestionAndPassword(QUESTION, ANSWER);
                            control.getiSecurityPassword().saveQuestionAndPassword(control.getiSecurityPassword());
                            Toast.makeText(SecurityPasswordActivity.this, "Completed!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(SecurityPasswordActivity.this, "You should enter all input fields!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SecurityPasswordActivity.this, "You should enter all input fields!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(ANSWER.equals(control.getiSecurityPassword().getANSWER())){
                        MyLock.setPassword(SecurityPasswordActivity.this, MainActivity.class);
                    }
                }

            }
        });

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        Bundle intent = getIntent().getExtras();
        MODE = null;
        try {
            MODE = intent.getString(MyLock.FORGOT_PASS_MODE);
        }catch (Exception e){
            MODE = null;
        }
        if(MODE != null){
            binding.edtxtQuestion.setText(control.getiSecurityPassword().getQUESTION());
            binding.mbtnSaveSecurityQuestion.setText("NEXT");
        }
    }
}