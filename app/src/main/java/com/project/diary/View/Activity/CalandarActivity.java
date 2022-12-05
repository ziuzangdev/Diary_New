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

    private LinearLayoutManager layoutManager;

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
                binding.txtSelectedDate.setText( getMonth(date.getMonth())+" " + date.getDay() + ", " + date.getYear() + "");
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
        binding.calendarView.setDateSelected(CalendarDay.today(), true);
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