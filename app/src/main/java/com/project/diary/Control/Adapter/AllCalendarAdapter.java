package com.project.diary.Control.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.diary.R;
import com.project.diary.View.Activity.CalandarActivity;
import com.project.diary.databinding.ActivityCalandarBinding;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AllCalendarAdapter extends RecyclerView.Adapter<AllCalendarAdapter.ViewHolder> {
    private Context context;

    private ActivityCalandarBinding binding;

    private ArrayList<LocalDate> localDates;

    public AllCalendarAdapter(ActivityCalandarBinding binding, ArrayList<LocalDate> localDates) {
        this.binding = binding;
        this.localDates = localDates;
    }

    @NonNull
    @Override
    public AllCalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvcalendar, parent, false);
        return new AllCalendarAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AllCalendarAdapter.ViewHolder holder, int position) {
        setMonthView(localDates.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return localDates.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView(LocalDate selectedDate, ViewHolder holder)
    {
        Toast.makeText(context, monthFromDate(selectedDate), Toast.LENGTH_SHORT).show();
        binding.txtMonth.setText(monthFromDate(selectedDate));
        binding.txtYear.setText(yearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 7);
        holder.rcvCalandarDate.setLayoutManager(layoutManager);
        holder.rcvCalandarDate.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String yearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
       // selectedDate = selectedDate.minusMonths(1);
        //setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        //selectedDate = selectedDate.plusMonths(1);
        //setMonthView();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rcvCalandarDate;
        public ViewHolder(View view) {
            super(view);
            rcvCalandarDate = view.findViewById(R.id.rcvCalandarDate);
        }
    }

}
