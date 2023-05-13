package sontungmtp.project.diary.Control.Adapter.Diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import sontungmtp.project.diary.R;

import java.util.ArrayList;

public class RcvMediaDemo extends RecyclerView.Adapter<RcvMediaDemo.ViewHolder> {
    private ArrayList<String> mediaPaths;
    private Context context, activityContext;

    public RcvMediaDemo(ArrayList<String> mediaPaths, Context activityContext) {
        this.mediaPaths = mediaPaths;
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public RcvMediaDemo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvmedia_demo, parent, false);
        return new RcvMediaDemo.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvMediaDemo.ViewHolder holder, int position) {
        displayImageFromPath(mediaPaths.get(position), holder.imgMedia);
    }

    @Override
    public int getItemCount() {
        return mediaPaths.size();
    }

    public void displayImageFromPath(String path, ImageView imageView){
        Glide.with(context)
                .load(path)
                .into(imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMedia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMedia = itemView.findViewById(R.id.imgMedia);
        }
    }
}
