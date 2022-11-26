package com.project.diary.View.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Diary.DiaryData;
import com.project.diary.Model.RichEditor.RichEditor;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.databinding.ActivityDiaryBinding;

import com.project.diary.R;

import java.time.LocalDate;
@RequiresApi(api = Build.VERSION_CODES.O)
public class DiaryActivity extends AppCompatActivity {
    private ActivityDiaryBinding binding;

    private ActivityDiaryControl control;

    private SQLite sqLite;

    private RichEditor richEditor;

    private String statusInPackage;

    private Diary diary;

    private DiaryData diaryData;

    private Thread subThread;

    private boolean isRunning;

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
                isRunning = false;
                finish();
            }
        });

        richEditor.setOnTextChangeListener(new jp.wasabeef.richeditor.RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                System.out.println(text);
                diaryData.setData(text);
            }
        });

        binding.txtTittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                diary.setTittle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.cvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diary.getId() != null){
                    int id = sqLite.getSqLiteControl().insertData(diary);
                    if(id != -1){
                        diary.setId(String.valueOf(id));
                    }
                }else{
                    sqLite.getSqLiteControl().updateData(diary, "Diary");
                }

            }
        });
    }

    private void addControls() {
        initSQLite();
        initStatus();
        initRichEditor();
        initDiaryData();
        initDiaryObject();
        initSubThreadHandleChangeData();
    }

    private void initSQLite() {
        sqLite = new SQLite(DiaryActivity.this);
    }

    private void initSubThreadHandleChangeData() {
//        subThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isRunning = true;
//                String currentData = richEditor.getHtml();
//                while (isRunning){
//                    if(!diaryData.getData().equals(currentData)){
//                        currentData = diaryData.getData();
//                    }
//                }
//            }
//        });
//        subThread.start();
    }

    private void initDiaryData() {
        String data;
        if(richEditor.getHtml() == null){
            data = "";
        }else{
            data = richEditor.getHtml();
        }
        diaryData = new DiaryData.Builder()
                .data(data)
                .build();
    }

    private void initDiaryObject() {
        diary = new Diary.Builder()
                .tittle(binding.txtTittle.getText().toString())
                .date(LocalDate.now().toString())
                .status(statusInPackage)
                .diaryData(diaryData)
                .build();
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