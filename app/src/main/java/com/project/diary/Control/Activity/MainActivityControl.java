package com.project.diary.Control.Activity;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivityControl extends RootControl {
    private ArrayList<Diary> diaries;

    private ArrayList<Diary> diariesSearch;

    private SQLite sqLite;

    private boolean isRunning;

    private RcvDiaryAdapter rcvDiaryAdapter;

    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

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
        appThemeManager = new AppThemeManager(context);
    }

    @WorkerThread
    public void InitRcvDiaryItem(){
        isRunning = true;
        System.out.println("gkfokodfkodgog");
        if(diaries != null){
            diaries.clear();
        }
        ArrayList<Diary> diaries1 = sqLite.getSqLiteControl().readData("Diary");
        diaries = new ArrayList<>();
        for(int i = diaries1.size() - 1; i>=0; i--){
            diaries.add(diaries1.get(i));
        }
        sortArrayListByCalendarDay(diaries, true);
        isRunning = false;
    }
    public void sortArrayListByCalendarDay(ArrayList<Diary> arrayList, boolean isNewest) {
        if(isNewest){
            arrayList.sort(new Comparator<Diary>() {
                @Override
                public int compare(Diary o1, Diary o2) {
                    CalendarDay cd1 = o1.getDate();
                    CalendarDay cd2 = o2.getDate();
                    if (cd1.isBefore(cd2)) {
                        return 1;
                    } else if (cd1.isAfter(cd2)) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }else{
            arrayList.sort(new Comparator<Diary>() {
                @Override
                public int compare(Diary o1, Diary o2) {
                    CalendarDay cd1 = o1.getDate();
                    CalendarDay cd2 = o2.getDate();
                    if (cd1.isBefore(cd2)) {
                        return -1;
                    } else if (cd1.isAfter(cd2)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }

    }



}
