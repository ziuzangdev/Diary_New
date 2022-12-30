package com.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Activity.ThemeActivityControl;
import com.project.diary.Control.Activity.ViewDiaryActivityControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.databinding.ActivityThemeBinding;
import com.project.diary.databinding.ActivityViewDiaryBinding;

public class ViewDiaryActivity extends AppCompatActivity implements IThemeManager {
    private ActivityViewDiaryBinding binding;
    private ViewDiaryActivityControl control;
    private Diary diary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDiaryBinding.inflate(getLayoutInflater());
        control = new ViewDiaryActivityControl(ViewDiaryActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        initTheme();
        initDiary();
    }

    private void initDiary() {
        String diaryStr = null;
        Bundle bundle = getIntent().getExtras();
        try{
            diaryStr = bundle.getString("Diary");
        }catch (Exception ignore){}
        if(diaryStr != null){
            try{
                diary = new Gson().fromJson(diaryStr, Diary.class);
                if(diaryStr != null){
                    Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Log.e("ViewDiaryActivity.java", "Error when init Diary from string gson in \"initDiary\" method");
            }
        }else{
            Log.e("ViewDiaryActivity.java", "Error when get intent from DiaryActivity in \"initDiary\" method");
        }
    }

    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
    }
}