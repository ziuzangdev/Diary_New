package com.project.diary.Control.Adapter.Diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.R;
import com.project.diary.View.Activity.DiaryActivity;
import com.project.diary.View.Activity.MainActivity;
import com.project.diary.databinding.ActivityMainBinding;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class RcvCalandarDiaryAdapter extends RecyclerView.Adapter<RcvCalandarDiaryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Diary> diaries;
    private Context activityContext;

    public RcvCalandarDiaryAdapter(ArrayList<Diary> diaries, Context activityContext) {
        this.diaries = diaries;
        this.activityContext = activityContext;
        initEvents();
    }

    private void initEvents() {
    }

    @NonNull
    @Override
    public RcvCalandarDiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvdiary, parent, false);
        return new RcvCalandarDiaryAdapter.ViewHolder(view);
    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    @Override
    public void onBindViewHolder(@NonNull RcvCalandarDiaryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(diaries.get(position).getTittle());
        holder.txtData.setText(diaries.get(position).getDiaryData().getData().replaceAll("\\<.*?>", ""));
        holder.txtEmojiStatus.setText(diaries.get(position).getStatus());
        holder.txtDay.setText(String.valueOf(diaries.get(position).getDate().getDay()));
        holder.txtYear.setText(String.valueOf(diaries.get(position).getDate().getYear()));
        holder.txtMonth.setText(getMonth(diaries.get(position).getDate().getMonth()).substring(0,3));

        //Events
        holder.Root.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(context, DiaryActivity.class);
                        intent.putExtra("ID_DIARY", diaries.get(position).getId());
                        context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,
                txtData,
                txtEmojiStatus,
                txtDay,
                txtMonth,
                txtYear;
        LinearLayout Root;

        View Date;

        RecyclerView rcvMediaDemo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtData = itemView.findViewById(R.id.txtData);
            txtEmojiStatus = itemView.findViewById(R.id.txtEmojiStatus);
            Root = itemView.findViewById(R.id.Root);
            rcvMediaDemo = itemView.findViewById(R.id.rcvMediaDemo);
            Date = itemView.findViewById(R.id.Date);
            txtDay = Date.findViewById(R.id.txtDay);
            txtMonth = Date.findViewById(R.id.txtMonth);
            txtYear = Date.findViewById(R.id.txtYear);
        }
    }
}
