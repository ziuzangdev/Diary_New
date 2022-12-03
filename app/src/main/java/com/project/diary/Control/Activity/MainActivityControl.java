package com.project.diary.Control.Activity;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.SQLite.SQLite;

import java.util.ArrayList;

public class MainActivityControl extends RootControl {
    private ArrayList<Diary> diaries;

    private ArrayList<Diary> diariesSearch;

    private SQLite sqLite;

    private boolean isRunning;

    private RcvDiaryAdapter rcvDiaryAdapter;

    public RcvDiaryAdapter getRcvDiaryAdapter() {
        return rcvDiaryAdapter;
    }

    public ArrayList<Diary> getDiariesSearch() {
        if(diariesSearch == null){
            diariesSearch = new ArrayList<>();
        }
        return diariesSearch;
    }

    public void setDiariesSearch(ArrayList<Diary> diariesSearch) {
        this.diariesSearch = diariesSearch;
    }

    public void setRcvDiaryAdapter(RcvDiaryAdapter rcvDiaryAdapter) {
        this.rcvDiaryAdapter = rcvDiaryAdapter;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public ArrayList<Diary> getDiaries() {
        if(diaries == null){
            diaries = new ArrayList<>();
        }
        return diaries;
    }

    public MainActivityControl(Context context) {
        super(context);
        isRunning = false;
        sqLite = new SQLite(context);
    }

    @WorkerThread
    public void InitRcvDiaryItem(){
        isRunning = true;
        diaries = sqLite.getSqLiteControl().readData("Diary");
        isRunning = false;
    }




}
