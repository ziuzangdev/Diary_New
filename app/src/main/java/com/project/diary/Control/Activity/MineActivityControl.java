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
/**
 * A class that handles various operations related to the MineActivity.
 * Extends RootControl.
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class MineActivityControl extends RootControl {

    public static final int MODE_STATISTIC_7 = 7;

    public static final int MODE_STATISTIC_30 = 30;
    private ArrayList<Diary> diaries;
    private SQLite sqLite;
    private AppThemeManager appThemeManager;

    /**
     * Constructor for the MineActivityControl.
     * Initializes the SQLite instance, retrieves non-draft Diary entries from the database,
     * and initializes the AppThemeManager.
     *
     * @param context  the context of the application
     */
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

    /**
     * Returns the ArrayList of Diary objects for this MineActivityControl instance.
     *
     * @return the ArrayList of Diary objects
     */
    public ArrayList<Diary> getDiaries() {
        return diaries;
    }

    /**
     * Sets the ArrayList of Diary objects for this MineActivityControl instance.
     *
     * @param diaries the new ArrayList of Diary objects
     */
    public void setDiaries(ArrayList<Diary> diaries) {
        this.diaries = diaries;
    }

    /**
     * Returns the AppThemeManager for this MineActivityControl instance.
     *
     * @return the AppThemeManager
     */
    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    /**
     * Returns the number of consecutive dates in the given list of Diary objects that are within the past 7 days.
     *
     * @param diaryList the list of Diary objects
     * @return the number of consecutive dates within the past 7 days
     */
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

    /**
     * Returns the total number of Diary objects in the list for this MineActivityControl instance.
     *
     * @return the total number of Diary objects
     */
    public String getAmountOfDiary(){
        return String.valueOf(diaries.size());
    }
    /**
     * Returns a list of the most recent n Diary objects in the list for this MineActivityControl instance that are before or on the given date. The list is sorted by most recent to least recent.
     *
     * @param diaries the list of Diary objects
     * @param date the date to compare to
     * @param n the number of Diary objects to retrieve
     * @return the list of the most recent n Diary objects before or on the given date
     */
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


    /**
     * Updates the count for each unique status in the given list of Diary objects in the given Hashtable.
     *
     * @param diaries the list of Diary objects
     * @param data the Hashtable containing the current counts for each status
     */
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

    /**
     * Returns the nearest date in the given list of CalendarDay objects to today's date.
     *
     * @param dates the list of CalendarDay objects
     * @return the nearest CalendarDay to today's date
     */
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
