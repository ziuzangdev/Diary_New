package com.project.diary.Control.Adapter.BackgroundDiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import com.project.diary.Control.BackgroundDiaryManager.PackageBackgroundDiaryControl;
import com.project.diary.Model.BackgroundDiaryManager.BackgroundDiary;
import com.project.diary.Model.BackgroundDiaryManager.PackageBackgroundDiary;
import com.project.diary.R;
import com.project.diary.View.Activity.DiaryActivity;

import java.util.ArrayList;

public class RcvDiaryBackgroundAdapter extends RecyclerView.Adapter<RcvDiaryBackgroundAdapter.ViewHolder> {
    private PackageBackgroundDiary packageBackgroundDiary;
    private Context context, activityContext;

    public RcvDiaryBackgroundAdapter(Context activityContext, int position) {
        this.activityContext = activityContext;
        packageBackgroundDiary = PackageBackgroundDiaryControl.getPackage(position);
    }

    @NonNull
    @Override
    public RcvDiaryBackgroundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcv_diary_background, parent, false);
        return new RcvDiaryBackgroundAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvDiaryBackgroundAdapter.ViewHolder holder, int position) {
        BackgroundDiary backgroundDiary = packageBackgroundDiary.getBackgroundDiaryArrayList().get(position);
        holder.loadImageWithGlide(backgroundDiary.getResIdBackground());
        holder.imgBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    ((DiaryActivity)activityContext).setBackground();
                }else{
                    ((DiaryActivity)activityContext).setBackground(backgroundDiary.getResIdBackground());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return packageBackgroundDiary.getBackgroundDiaryArrayList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBackground = itemView.findViewById(R.id.imgBackground);
        }

        public void loadImageWithGlide(int resourceId) {
            Glide.with(activityContext)
                    .load(resourceId)
                    .into(imgBackground);
        }
    }
}
