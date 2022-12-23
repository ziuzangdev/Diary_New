package com.project.diary.Control.Adapter.Diary;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.project.diary.Control.TemplateManager.TemplateControl;
import com.project.diary.Model.TemplateManager.Template;
import com.project.diary.R;
import com.project.diary.View.Activity.TemplateActivity;

public class RcvTemplateDiaryAdapter extends RecyclerView.Adapter<RcvTemplateDiaryAdapter.ViewHolder> {
    private Context context, activityContext;

    public RcvTemplateDiaryAdapter(Context activityContext) {
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public RcvTemplateDiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcv_template_diary, parent, false);
        return new RcvTemplateDiaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvTemplateDiaryAdapter.ViewHolder holder, int position) {
        Template template = TemplateControl.getTemplateAt(position);
        holder.setContentText(template.getContent());
        holder.setNameText(template.getName());
        holder.setLlTemplateDiaryBackground(Color.parseColor(template.getBackgroundColor()));
        holder.setMbtnStartTemplateDiaryBackground(Color.parseColor(template.getButtonColor()));
        holder.loadImageWithGlide(template.getImageId());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TemplateActivity)activityContext).addTemplate(template);
            }
        };

        holder.llTemplateDiary.setOnClickListener(onClickListener);
        holder.mbtnStartTemplateDiary.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return TemplateControl.getTemplateCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, content;
        MaterialButton mbtnStartTemplateDiary;
        LinearLayout llTemplateDiary;
        ImageView imageId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            mbtnStartTemplateDiary = itemView.findViewById(R.id.mbtnStartTemplateDiary);
            llTemplateDiary = itemView.findViewById(R.id.llTemplateDiary);
            imageId = itemView.findViewById(R.id.imageId);
        }

        public void loadImageWithGlide(int resourceId) {
            Glide.with(activityContext)
                    .load(resourceId)
                    .into(imageId);
        }

        public void setNameText(String text) {
            name.setText(text);
        }

        public void setContentText(String text) {
            content.setText(text);
        }

        public void setMbtnStartTemplateDiaryBackground(int color) {
            mbtnStartTemplateDiary.setBackgroundColor(color);
        }

        public void setLlTemplateDiaryBackground(int color) {
            llTemplateDiary.setBackgroundColor(color);
        }
    }
}
