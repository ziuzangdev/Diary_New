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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import com.project.diary.Control.Adapter.Media.RcvMediaDemoAdapter;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.R;
import com.project.diary.View.Activity.DiaryActivity;
import com.project.diary.View.Activity.MainActivity;
import com.project.diary.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class RcvDiaryAdapter extends RecyclerView.Adapter<RcvDiaryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Diary> diaries;


    private ArrayList<Diary> chooseList;

    private boolean isChooseMode;

    private ActivityMainBinding binding;

    private Context activityContext;

    public RcvDiaryAdapter(ArrayList<Diary> diaries, ActivityMainBinding binding, Context activityContext) {
        this.diaries = diaries;
        this.binding = binding;
        this.activityContext = activityContext;
        chooseList = new ArrayList<>();
        isChooseMode = false;
        initEvents();
    }

    private void initEvents() {
        binding.imgCloseDeleteMode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                isChooseMode = false;
                chooseList.clear();
                binding.ToolbarView3.setVisibility(View.GONE);
                binding.ToolbarView4.setVisibility(View.GONE);
                binding.ToolbarView1.setVisibility(View.VISIBLE);
                binding.ToolbarView2.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });

        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite sqLite = new SQLite(activityContext);
                for(Diary diary : chooseList){
                    sqLite.getSqLiteControl().removeData("Diary", diary.getId());
                }
                isChooseMode = false;
                chooseList.clear();
                binding.ToolbarView3.setVisibility(View.GONE);
                binding.ToolbarView4.setVisibility(View.GONE);
                binding.ToolbarView1.setVisibility(View.VISIBLE);
                binding.ToolbarView2.setVisibility(View.VISIBLE);
                ((MainActivity)activityContext).initRcvDiary();
            }
        });
    }

    @NonNull
    @Override
    public RcvDiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvdiary, parent, false);
        return new RcvDiaryAdapter.ViewHolder(view);
    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    @Override
    public void onBindViewHolder(@NonNull RcvDiaryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(!isChooseMode){
            holder.Root.setBackgroundColor(context.getResources().getColor(R.color.nomal_mode, null));
        }
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
                if(chooseList.size() == 0){
                    binding.ToolbarView3.setVisibility(View.GONE);
                    binding.ToolbarView4.setVisibility(View.GONE);
                    binding.ToolbarView1.setVisibility(View.VISIBLE);
                    binding.ToolbarView2.setVisibility(View.VISIBLE);
                    isChooseMode = false;
                }else{
                    isChooseMode = true;
                }
                if(chooseList.contains(diaries.get(position))){
                    holder.Root.setBackgroundColor(context.getResources().getColor(R.color.nomal_mode, null));
                    chooseList.remove(diaries.get(position));
                }else{
                    if(isChooseMode){
                        holder.Root.setBackgroundColor(context.getResources().getColor(R.color.choose_mode, null));
                        chooseList.add(diaries.get(position));
                    }else{

                        Intent intent = new Intent(context, DiaryActivity.class);
                        intent.putExtra("ID_DIARY", diaries.get(position).getId());
                        context.startActivity(intent);
                    }

                }
                if(chooseList.size() == 0){
                    binding.ToolbarView3.setVisibility(View.GONE);
                    binding.ToolbarView4.setVisibility(View.GONE);
                    binding.ToolbarView1.setVisibility(View.VISIBLE);
                    binding.ToolbarView2.setVisibility(View.VISIBLE);
                    isChooseMode = false;
                }else{
                    isChooseMode = true;
                }
            }
        });

        holder.Root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(chooseList.size() == 0){
                    binding.ToolbarView3.setVisibility(View.GONE);
                    binding.ToolbarView4.setVisibility(View.GONE);
                    binding.ToolbarView1.setVisibility(View.VISIBLE);
                    binding.ToolbarView2.setVisibility(View.VISIBLE);
                    isChooseMode = false;
                }else{
                    isChooseMode = true;
                }
                if(chooseList.contains(diaries.get(position))){
                    holder.Root.setBackgroundColor(context.getResources().getColor(R.color.nomal_mode, null));
                    chooseList.remove(diaries.get(position));
                }else{
                    holder.Root.setBackgroundColor(context.getResources().getColor(R.color.choose_mode, null));
                    chooseList.add(diaries.get(position));
                }
                if(chooseList.size() == 0){
                    binding.ToolbarView3.setVisibility(View.GONE);
                    binding.ToolbarView4.setVisibility(View.GONE);
                    binding.ToolbarView1.setVisibility(View.VISIBLE);
                    binding.ToolbarView2.setVisibility(View.VISIBLE);
                    isChooseMode = false;
                }else{
                    isChooseMode = true;
                    binding.ToolbarView1.setVisibility(View.GONE);
                    binding.ToolbarView2.setVisibility(View.GONE);
                    binding.ToolbarView3.setVisibility(View.VISIBLE);
                    binding.ToolbarView4.setVisibility(View.VISIBLE);
                }
                return true;
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
