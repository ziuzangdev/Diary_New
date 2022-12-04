package com.project.diary.Control.Adapter.Calendar;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.project.diary.View.Fragment.CalendarFragment;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<CalendarFragment> calendarFragments;
    public CalendarFragmentAdapter(@NonNull FragmentManager fm,  ArrayList<CalendarFragment> calendarFragments) {
        super(fm);
        this.calendarFragments = calendarFragments;
    }

    public ArrayList<CalendarFragment> getCalendarFragments() {
        return calendarFragments;
    }

    public void setCalendarFragments(ArrayList<CalendarFragment> calendarFragments) {
        this.calendarFragments = calendarFragments;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getFirstLocalDate(){
        return calendarFragments.get(0).getSelectedDate();
    }

    public void chanceData(){
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return calendarFragments.get(position);
    }

    @Override
    public int getCount() {
        return calendarFragments.size();
    }
}
