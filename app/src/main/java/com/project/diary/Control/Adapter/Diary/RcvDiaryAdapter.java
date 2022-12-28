package com.project.diary.Control.Adapter.Diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Locale;

public class RcvDiaryAdapter extends RecyclerView.Adapter<RcvDiaryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Diary> diaries;


    private ArrayList<Diary> chooseList;

    private ArrayList<RcvMediaDemo> rcvMediaDemos;

    private boolean isChooseMode;

    private ActivityMainBinding binding;

    private Context activityContext;

    public RcvDiaryAdapter(ArrayList<Diary> diaries, ActivityMainBinding binding, Context activityContext) {
        this.diaries = diaries;
        this.binding = binding;
        this.activityContext = activityContext;
        chooseList = new ArrayList<>();
        rcvMediaDemos = new ArrayList<>();
        isChooseMode = false;
        initEvents();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        diaries.clear();
        notifyDataSetChanged();
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
                clear();
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
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.US);
        return dateFormatSymbols.getMonths()[month-1];
    }
    @Override
    public void onBindViewHolder(@NonNull RcvDiaryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(!isChooseMode){
            holder.Root.setBackgroundColor(context.getResources().getColor(R.color.nomal_mode, null));
        }
        holder.txtName.setText(displayTittle(diaries.get(position).getTittle()));
        holder.txtData.setText(removeAllHtmlTag(diaries.get(position).getDiaryData().getData()));
        holder.txtEmojiStatus.setText(diaries.get(position).getStatus());
        holder.txtDay.setText(String.valueOf(diaries.get(position).getDate().getDay()));
        holder.txtYear.setText(String.valueOf(diaries.get(position).getDate().getYear()));
        holder.txtMonth.setText(getMonth(diaries.get(position).getDate().getMonth()).substring(0,3));
        try{
            if(diaries.get(position).isDraft()){
                holder.imgDraft.setVisibility(View.VISIBLE);
                holder.txtDraft.setVisibility(View.VISIBLE);
            }else{
                holder.imgDraft.setVisibility(View.GONE);
                holder.txtDraft.setVisibility(View.GONE);
            }
        }catch (Exception e){}

        //Recycleview Medias
        if(diaries.get(position).getMediaPaths().size() > 0){
            holder.rcvMediaDemo.setVisibility(View.VISIBLE);
            RcvMediaDemo rcvMediaDemo = new RcvMediaDemo(diaries.get(position).getMediaPaths(), activityContext);
            holder.rcvMediaDemo.setLayoutManager(new LinearLayoutManager(activityContext, RecyclerView.HORIZONTAL, false));
            holder.rcvMediaDemo.setHasFixedSize(true);
            holder.rcvMediaDemo.setAdapter(rcvMediaDemo);
        }

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

    private String displayTittle(String tittle) {
        if(tittle.equals("")){
            return "No tittle";
        }else{
            return tittle;
        }
    }

    private String removeAllHtmlTag(String html) {
        System.out.println("----------------------------");
        String strippedHtml = html;

        // Loại bỏ các tag img
        strippedHtml = strippedHtml.replaceAll("<img[^>]*>", "");

        // Loại bỏ các tag HTML khác
        strippedHtml = strippedHtml.replaceAll("<(?!li|br|/li|/br)[^>]*>", "");
        strippedHtml = strippedHtml.replaceAll("<br>", "\n");
        try{
            int index = strippedHtml.indexOf("<li>");
            if(index != 0){
                String firstPart = strippedHtml.substring(0, index);
                String secondPart = strippedHtml.substring(index);
                strippedHtml = firstPart + "\n" ;
                secondPart = secondPart.replaceAll("\\s*<li>\\s*", "");
                secondPart = secondPart.replaceAll("\\s*</li>\\s*", "\n");
                return strippedHtml + secondPart;
            }else{
                strippedHtml = strippedHtml.replaceAll("\\s*<li>\\s*", "");
                strippedHtml = strippedHtml.replaceAll("\\s*</li>\\s*", "\n");
            }
        }catch (Exception e){}
        strippedHtml = strippedHtml.replaceAll("&nbsp;", "");
        strippedHtml = strippedHtml.trim();
        System.out.println(strippedHtml);
        return strippedHtml;
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
        txtYear, txtDraft;
        LinearLayout Root;

        View Date;

        RecyclerView rcvMediaDemo;

        ImageView imgDraft;

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
            imgDraft = itemView.findViewById(R.id.imgDraft);
            txtDraft = itemView.findViewById(R.id.txtDraft);
        }
    }
}
