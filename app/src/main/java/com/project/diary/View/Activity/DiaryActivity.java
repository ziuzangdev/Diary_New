package com.project.diary.View.Activity;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.databinding.ActivityDiaryBinding;

import com.project.diary.R;

public class DiaryActivity extends AppCompatActivity {
    private ActivityDiaryBinding binding;

    private ActivityDiaryControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        control = new ActivityDiaryControl(DiaryActivity.this);
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        binding.txtEmojiStatus.setText(control.getEmoji(0).getEmojiText());
    }

}