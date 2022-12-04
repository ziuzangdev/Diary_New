package com.project.diary.Control.Adapter.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.diary.R;
import com.project.diary.View.Activity.CalandarActivity;
import com.project.diary.View.Fragment.CalendarFragment;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private ArrayList<String> daysOfMonth;

    private String dateToday;

    private Context context;

    private CalendarFragment calendarFragment;

    private Context activityContext;

    public CalendarAdapter(ArrayList<String> daysOfMonth, Context activityContext, CalendarFragment calendarFragment) {
        this.daysOfMonth = daysOfMonth;
        this.activityContext = activityContext;
        this.calendarFragment = calendarFragment;
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.calandar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(dateToday != null){
                if(dateToday.equals(daysOfMonth.get(position))){
                    holder.dayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.Fresh_Guacamole_04));
                    holder.dayOfMonth.setTypeface(Typeface.DEFAULT_BOLD);
                }
        }
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        holder.dayOfMonth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                dateToday = daysOfMonth.get(position);
                ((CalandarActivity)activityContext).setCurrentDateChoose(calendarFragment);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void cvBtnTodayEvent(String day){
        dateToday = day;
        notifyDataSetChanged();
    }


    public String getDateToday() {
        return dateToday;
    }

    public void setDateToday(String dateToday) {
        this.dateToday = dateToday;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView dayOfMonth;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
        }

    }
}
