package com.project.diary.View.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.project.diary.Control.Activity.CalandarActivityControl;
import com.project.diary.Control.Adapter.Calendar.CalendarFragmentAdapter;
import com.project.diary.View.Fragment.CalendarFragment;
import com.project.diary.databinding.ActivityCalandarBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
@RequiresApi(api = Build.VERSION_CODES.O)
public class CalandarActivity extends AppCompatActivity {

    private ActivityCalandarBinding binding;

    private CalandarActivityControl control;

    private ArrayList<CalendarFragment> calendarFragments;

    private CalendarFragmentAdapter calendarFragmentAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalandarBinding.inflate(getLayoutInflater());
        control = new CalandarActivityControl(CalandarActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addEvents() {
        binding.cvBtnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<calendarFragments.size(); i++){
                    if(calendarFragments.get(i).getSelectedDate().toString().equals(LocalDate.now().toString())){
                        binding.vpCalendar.setCurrentItem(i, true);
                        calendarFragments.get(i).getCalendarAdapter().cvBtnTodayEvent(dayFromDate(LocalDate.now()));
                        break;
                    }
                }
            }
        });

        binding.cvNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalandarActivity.this, DiaryActivity.class);
                startActivity(intent);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.vpCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    binding.txtMonth.setText(monthFromDate(calendarFragments.get(position).getSelectedDate()));
                    binding.txtYear.setText(yearFomDate(calendarFragments.get(position).getSelectedDate()));
                    if(position == 1){
                        CalendarFragment calendarFragment = new CalendarFragment(calendarFragmentAdapter
                                .getFirstLocalDate()
                                .minusMonths(1),
                                CalandarActivity.this);
                            if(!calendarFragment.isAdded()){
                                calendarFragments.add(0, calendarFragment);
                            }
                            calendarFragmentAdapter.chanceData();
                            binding.vpCalendar.setCurrentItem(2, true);
                    }else if(position == calendarFragments.size()-2){
                        CalendarFragment calendarFragment = new CalendarFragment(calendarFragments.
                                get(calendarFragments.size() - 1)
                                .getSelectedDate()
                                .plusMonths(1), CalandarActivity.this);
                        if(!calendarFragment.isAdded()){
                            calendarFragments.add(calendarFragment);
                        }
                        calendarFragmentAdapter.setCalendarFragments(calendarFragments);
                        calendarFragmentAdapter.chanceData();

                    }
                    binding.txtYear.setText(yearFomDate(calendarFragments.get(position).getSelectedDate()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addControls() {
        initViewPagerCalendar();
    }

    public void setCurrentDateChoose(CalendarFragment calendarFragment){
        Thread thread = new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                for(CalendarFragment calendarFragmentTemp : calendarFragments){
                    if(calendarFragmentTemp != calendarFragment){
                        if(calendarFragmentTemp.getCalendarAdapter() != null){
                            calendarFragmentTemp.getCalendarAdapter().setDateToday(null);
                            calendarFragmentTemp.getCalendarAdapter().notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        thread.start();
    }

    private void initViewPagerCalendar() {
        calendarFragments = new ArrayList<>();
        calendarFragments.add(new CalendarFragment(LocalDate.now().minusMonths(2), CalandarActivity.this));
        calendarFragments.add(new CalendarFragment(LocalDate.now().minusMonths(1), CalandarActivity.this));
        calendarFragments.add(new CalendarFragment(LocalDate.now(), CalandarActivity.this));
        calendarFragments.add(new CalendarFragment(LocalDate.now().plusMonths(1), CalandarActivity.this));
        calendarFragments.add(new CalendarFragment(LocalDate.now().plusMonths(2), CalandarActivity.this));
        calendarFragmentAdapter = new CalendarFragmentAdapter(getSupportFragmentManager(),calendarFragments);
        binding.vpCalendar.setAdapter(calendarFragmentAdapter);
        binding.vpCalendar.setCurrentItem(2,true);
        binding.vpCalendar.setOffscreenPageLimit(1000);
        getSupportFragmentManager().executePendingTransactions();
    }

    private String monthFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }

    private String yearFomDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    private String dayFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        return date.format(formatter);
    }






}