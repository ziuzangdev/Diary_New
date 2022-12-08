package com.project.diary.Control.Activity;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.project.diary.Control.Adapter.Diary.RcvCalandarDiaryAdapter;
import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class CalandarActivityControl extends MainActivityControl {
    private RcvCalandarDiaryAdapter rcvCalandarDiaryAdapter;

    private ArrayList<Diary> diariesOnDate;


    @WorkerThread
    public void initDiariesOnDate(CalendarDay calendarDay) {
        System.out.println("root: " + calendarDay.toString());
        if(diariesOnDate == null){
            diariesOnDate = new ArrayList<>();
        }
        diariesOnDate.clear();
        for(Diary diary : getDiaries()){
            System.out.println("sub: " + diary.getDate().toString());
            if(diary.getDate().getDay() == calendarDay.getDay() &&
                    diary.getDate().getMonth() == calendarDay.getMonth() &&
                    diary.getDate().getYear() == calendarDay.getYear()){
                    diariesOnDate.add(diary);
            }
        }
    }

    public ArrayList<Diary> getDiariesOnDate() {
        if(diariesOnDate == null){
            diariesOnDate = new ArrayList<>();
        }
        return diariesOnDate;
    }

    public void setDiariesOnDate(ArrayList<Diary> diariesOnDate) {
        this.diariesOnDate = diariesOnDate;
    }

    public RcvCalandarDiaryAdapter getRcvCalandarDiaryAdapter() {
        return rcvCalandarDiaryAdapter;
    }

    public void setRcvCalandarDiaryAdapter(RcvCalandarDiaryAdapter rcvCalandarDiaryAdapter) {
        this.rcvCalandarDiaryAdapter = rcvCalandarDiaryAdapter;
    }

    public CalandarActivityControl(Context context) {
        super(context);
    }
}
