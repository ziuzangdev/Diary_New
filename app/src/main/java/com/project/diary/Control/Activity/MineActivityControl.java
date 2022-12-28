package com.project.diary.Control.Activity;

import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class MineActivityControl extends RootControl {

    public static final int MODE_STATISTIC_7 = 7;

    public static final int MODE_STATISTIC_30 = 30;
    private ArrayList<Diary> diaries;
    private SQLite sqLite;

    public ArrayList<Diary> getDiaries() {
        return diaries;
    }

    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    public void setDiaries(ArrayList<Diary> diaries) {
        this.diaries = diaries;
    }

    public MineActivityControl(Context context) {
        super(context);
        sqLite = new SQLite(context);
        ArrayList<Diary> diaries1 = sqLite.getSqLiteControl().readData("Diary");
        diaries = new ArrayList<>();
            for(Diary diary : diaries1){
                try{
                    if(!diary.isDraft()){
                        diaries.add(diary);
                    }
                }catch (Exception e){
                    diaries.add(diary);
                }
            }
        appThemeManager = new AppThemeManager(context);
    }
    public int getNumberOfConsecutiveDates(ArrayList<Diary> diaryList) {
        CalendarDay today = CalendarDay.today();
        int count = 0;

        for (Diary diary : diaryList) {
            CalendarDay date = diary.getDate();
            if (date.isBefore(today) && date.isAfter(CalendarDay.from(today.getDate().minusDays(7)))) {
                count++;
            }
        }
        return count;
    }


    public String getAmountOfDiary(){
        return String.valueOf(diaries.size());
    }
    public int getContinuousDateCount(List<CalendarDay> dates) {
//        Calendar today = Calendar.getInstance();
//        CalendarDay nearestDate = getNearestDate(dates);
//        int count = 0;
//        CalendarDay currentDate = nearestDate;
//        while (dates.contains(currentDate) && currentDate.isAfter(today)) {
//            count++;
//            currentDate = currentDate.getNext();
//        }
//        return count;
        return 0;
    }

    public ArrayList<Diary> getRecentNDiaries(ArrayList<Diary> diaries, CalendarDay date, int n) {
        // Sắp xếp diaries theo ngày từ mới nhất đến cũ nhất
        diaries.sort((d1, d2) -> {
            CalendarDay date1 = d1.getDate();
            CalendarDay date2 = d2.getDate();
            if (date1.getYear() != date2.getYear()) {
                return date2.getYear() - date1.getYear();
            } else if (date1.getMonth() != date2.getMonth()) {
                return date2.getMonth() - date1.getMonth();
            } else {
                return date2.getDay() - date1.getDay();
            }
        });

        ArrayList<Diary> recentNDiaries = new ArrayList<>();
        for (Diary diary : diaries) {
            if (diary.getDate().isBefore(date) || diary.getDate().equals(date)) {
                recentNDiaries.add(diary);
            }
            if (recentNDiaries.size() == n) {
                break;
            }
        }
        return recentNDiaries;
    }


    public void updateStatusCount(ArrayList<Diary> diaries, Hashtable<String, Integer> data) {
        System.out.println(diaries.size()); //2
        diaries.stream().forEach(diary -> {
            String status = diary.getStatus();
            if (data.containsKey(status)) {
                int count = data.get(status);
                System.out.println(status + "|" + (count + 1));
                data.put(status, count + 1);
            }
        });
    }

    public static CalendarDay getNearestDate(List<CalendarDay> dates) {
        Calendar today = Calendar.getInstance();
        CalendarDay nearestDate = null;
        int minYearDiff = Integer.MAX_VALUE;
        int minMonthDiff = Integer.MAX_VALUE;
        int minDayDiff = Integer.MAX_VALUE;
        for (CalendarDay date : dates) {
            int yearDiff = Math.abs(date.getYear() - today.get(Calendar.YEAR));
            int monthDiff = Math.abs(date.getMonth() - today.get(Calendar.MONTH));
            int dayDiff = Math.abs(date.getDay() - today.get(Calendar.DAY_OF_MONTH));
            if (yearDiff < minYearDiff || (yearDiff == minYearDiff && monthDiff < minMonthDiff) || (yearDiff == minYearDiff && monthDiff == minMonthDiff && dayDiff < minDayDiff)) {
                minYearDiff = yearDiff;
                minMonthDiff = monthDiff;
                minDayDiff = dayDiff;
                nearestDate = date;
            }
        }
        return nearestDate;
    }
}
