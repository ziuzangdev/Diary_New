package com.project.diary.View.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.diary.Control.Activity.CalandarActivityControl;
import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.Control.Adapter.AllCalendarAdapter;
import com.project.diary.Control.Adapter.CalendarAdapter;
import com.project.diary.Control.Adapter.CalendarFragmentAdapter;
import com.project.diary.R;
import com.project.diary.View.Fragment.CalendarFragment;
import com.project.diary.databinding.ActivityCalandarBinding;
import com.project.diary.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.YearMonth;
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i< calendarFragments.size(); i++){
                            if(i != position){
                                if(calendarFragments.get(i).getCalendarAdapter() != null){
                                    if(calendarFragments.get(i).getCalendarAdapter().getDateToday() != null){
                                        calendarFragments.get(i).getCalendarAdapter().setDateToday(null);
                                    }
                                }
                            }
                        }
                    }
                });
               thread.start();
                if(position == 1){
                    calendarFragments.add(0, new CalendarFragment(calendarFragments.get(0).getSelectedDate().minusMonths(1)));
                    calendarFragmentAdapter.notifyDataSetChanged();
                    binding.vpCalendar.setCurrentItem(2,true);
                }else if(position == calendarFragments.size()-2){
                    calendarFragments.add(new CalendarFragment(calendarFragments.
                            get(calendarFragments.size() - 1)
                            .getSelectedDate()
                            .plusMonths(1)));
                    calendarFragmentAdapter.notifyDataSetChanged();
                }
                binding.txtMonth.setText(monthFromDate(calendarFragments.get(position).getSelectedDate()));
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

    private void initViewPagerCalendar() {
        calendarFragments = new ArrayList<>();
        calendarFragments.add(new CalendarFragment(LocalDate.now().minusMonths(2)));
        calendarFragments.add(new CalendarFragment(LocalDate.now().minusMonths(1)));
        calendarFragments.add(new CalendarFragment(LocalDate.now()));
        calendarFragments.add(new CalendarFragment(LocalDate.now().plusMonths(1)));
        calendarFragments.add(new CalendarFragment(LocalDate.now().plusMonths(2)));
        calendarFragmentAdapter = new CalendarFragmentAdapter(getSupportFragmentManager(),calendarFragments);
        binding.vpCalendar.setAdapter(calendarFragmentAdapter);
        binding.vpCalendar.setCurrentItem(2,true);
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