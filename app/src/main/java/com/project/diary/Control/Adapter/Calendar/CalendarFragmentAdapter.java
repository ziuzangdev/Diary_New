package com.project.diary.Control.Adapter.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.project.diary.View.Fragment.CalendarFragment;

import java.util.ArrayList;

public class CalendarFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<CalendarFragment> calendarFragments;
    public CalendarFragmentAdapter(@NonNull FragmentManager fm,  ArrayList<CalendarFragment> calendarFragments) {
        super(fm);
        this.calendarFragments = calendarFragments;
    }



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
