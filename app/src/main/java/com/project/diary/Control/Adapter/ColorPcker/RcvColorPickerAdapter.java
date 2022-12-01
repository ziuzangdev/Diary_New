package com.project.diary.Control.Adapter.ColorPcker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.diary.Control.Adapter.Calendar.CalendarAdapter;
import com.project.diary.Model.Painting.DrawView;
import com.project.diary.Model.RichEditor.RichEditor;
import com.project.diary.R;

import java.util.ArrayList;

public class RcvColorPickerAdapter extends RecyclerView.Adapter<RcvColorPickerAdapter.ViewHolder> {
    private ArrayList<String> color;
    private Context context;

    private RichEditor richEditor;

    private DrawView drawView;

    public RcvColorPickerAdapter(DrawView drawView) {
        inutColor();
        this.drawView = drawView;
    }

    public RcvColorPickerAdapter(RichEditor richEditor) {
        inutColor();
        this.richEditor = richEditor;
    }

    private void inutColor() {
        color = new ArrayList<>();
        color.add("#000000");
        color.add("#FFFFFF");
        color.add("#FFFC00");
        color.add("#0099FF");
        color.add("#81C784");
        color.add("#212121");
        color.add("#BA68C8");
        color.add("#D500F9");
        color.add("#FF3D00");
    }

    @NonNull
    @Override
    public RcvColorPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvcolorpicker, parent, false);
        return new RcvColorPickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvColorPickerAdapter.ViewHolder holder, int position) {
        if(richEditor != null){
            holder.color_item.setBackgroundColor(Color.parseColor(color.get(position)));
            holder.color_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    richEditor.setTextColor(Color.parseColor(color.get(position)));
                }
            });
        }else if(drawView != null){
            holder.color_item.setBackgroundColor(Color.parseColor(color.get(position)));
            holder.color_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   drawView.setColor(Color.parseColor(color.get(position)));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return color.size();
    }

    public ArrayList<String> getColor() {
        return color;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout color_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color_item = itemView.findViewById(R.id.color_item);
        }
    }
}
