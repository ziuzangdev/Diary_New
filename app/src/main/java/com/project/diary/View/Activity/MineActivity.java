package com.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Activity.MineActivityControl;
import com.project.diary.Control.PreferencesManager.PreferencesManagerForStasticMineActivity;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.databinding.ActivityMineBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Hashtable;

public class MineActivity extends AppCompatActivity implements IThemeManager {
    
    private ActivityMineBinding binding;
    
    private MineActivityControl control;

    private boolean is7DayMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMineBinding.inflate(getLayoutInflater());
        control = new MineActivityControl(MineActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.llModeStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManagerForStasticMineActivity.initializeInstance(getApplicationContext());
                PreferencesManagerForStasticMineActivity prefManager = PreferencesManagerForStasticMineActivity.getInstance();
                is7DayMode = !is7DayMode;
                if(is7DayMode){
                    prefManager.saveModeStatistic(7);
                    binding.txtModeStastics.setText("7 days ago");
                }else{
                    prefManager.saveModeStatistic(30);
                    binding.txtModeStastics.setText("30 days ago");
                }
                initColumnChartView();
            }
        });

    }

    private void addControls() {
        initTheme();
        binding.txtAmountOfDiary.setText(control.getAmountOfDiary());
        binding.txtDausKeepWriting.setText("Keep writing for " + String.valueOf(control.getNumberOfConsecutiveDates(control.getDiaries()) + " days"));
        initModeStastics();
        initColumnChartView();
    }

    private void initModeStastics() {
        PreferencesManagerForStasticMineActivity.initializeInstance(getApplicationContext());
        PreferencesManagerForStasticMineActivity prefManager = PreferencesManagerForStasticMineActivity.getInstance();
        int MODE = prefManager.readModeStatistic();
        if(MODE == MineActivityControl.MODE_STATISTIC_30){
           is7DayMode = false;
            binding.txtModeStastics.setText("30 days ago");
        }else if(MODE == MineActivityControl.MODE_STATISTIC_7){
           is7DayMode = true;
            binding.txtModeStastics.setText("7 days ago");
        }
    }

    private void initColumnChartView() {
        PreferencesManagerForStasticMineActivity.initializeInstance(getApplicationContext());
        PreferencesManagerForStasticMineActivity prefManager = PreferencesManagerForStasticMineActivity.getInstance();
        int MODE = prefManager.readModeStatistic();
        ArrayList<Diary> diaries = new ArrayList<>();
        if(MODE == MineActivityControl.MODE_STATISTIC_30){
            diaries.addAll(control.getRecentNDiaries(control.getDiaries(), CalendarDay.today(), 30));
        }else if(MODE == MineActivityControl.MODE_STATISTIC_7){
            diaries.addAll(control.getRecentNDiaries(control.getDiaries(), CalendarDay.today(), 7));
        }
        Hashtable<String, Integer> data = new Hashtable<>();
        ActivityDiaryControl diaryControl = new ActivityDiaryControl(MineActivity.this);
        String[] emojiText = diaryControl.getEmojiText();
        for(int i=0; i< emojiText.length; i++){
            data.put(emojiText[i], 0);
        }
        control.updateStatusCount(diaries, data);
        binding.ColumnChartView.setData(data);
        binding.ColumnChartView.setColumnWidth(10);
        binding.ColumnChartView.generateChart();

    }

    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
        binding.imgBackground.setImageResource(appThemeManager.getBG_THEME_MINE());
        binding.mbtnStartTemplateDiary.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));

    }
}