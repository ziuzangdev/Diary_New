package com.project.diary.View.Activity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Model.RichEditor.RichEditor;
import com.project.diary.databinding.ActivityDiaryBinding;

import com.project.diary.R;

public class DiaryActivity extends AppCompatActivity {
    private ActivityDiaryBinding binding;

    private ActivityDiaryControl control;

    private RichEditor richEditor;

    private String statusInPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        control = new ActivityDiaryControl(DiaryActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        //initStatus();
        initRichEditor();
    }

    private void initStatus() {
        statusInPackage = control.initStatus();
        binding.txtEmojiStatus.setText(statusInPackage);
    }

    private void initRichEditor() {
       richEditor = binding.mEditor;
       richEditor.setBinding(binding);
       richEditor.initAllEvents();
    }

}