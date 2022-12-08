package com.project.diary.View.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.project.diary.Control.Activity.CalandarActivityControl;
import com.project.diary.Control.Adapter.Diary.RcvCalandarDiaryAdapter;
import com.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import com.project.diary.databinding.ActivityCalandarBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalandarActivity extends AppCompatActivity {

    private ActivityCalandarBinding binding;

    private CalandarActivityControl control;

    private CalendarDay calendarDay;

    public ActivityCalandarBinding getBinding() {
        return binding;
    }

    public void setBinding(ActivityCalandarBinding binding) {
        this.binding = binding;
    }

    public CalandarActivityControl getControl() {
        return control;
    }

    public void setControl(CalandarActivityControl control) {
        this.control = control;
    }

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }

    public void setCalendarDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
    }

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

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addEvents() {

        binding.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                calendarDay = date;
                binding.txtSelectedDate.setText( getMonth(calendarDay.getMonth())+" " + calendarDay.getDay() + ", " + calendarDay.getYear() + "");
                initRcvDiary();
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


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addControls() {
        calendarDay = CalendarDay.today();
        binding.calendarView.setDateSelected(calendarDay, true);
        binding.txtSelectedDate.setText( getMonth(calendarDay.getMonth())+" " + calendarDay.getDay() + ", " + calendarDay.getYear() + "");
        initRcvDiary();
    }
    public void initRcvDiary() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                control.InitRcvDiaryItem();
                control.initDiariesOnDate(calendarDay);
                if(control.getDiariesOnDate().size() > 0){
                    control.setRcvCalandarDiaryAdapter(new RcvCalandarDiaryAdapter(control.getDiariesOnDate(), CalandarActivity.this));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.txtNoDiary.setVisibility(View.GONE);
                            binding.rcvDiary.setVisibility(View.VISIBLE);
                            binding.rcvDiary.setHasFixedSize(true);
                            binding.rcvDiary.setLayoutManager(new LinearLayoutManager(CalandarActivity.this));
                            binding.rcvDiary.setAdapter(control.getRcvCalandarDiaryAdapter());
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.txtNoDiary.setVisibility(View.VISIBLE);
                            binding.rcvDiary.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });
        thread.start();
    }

}