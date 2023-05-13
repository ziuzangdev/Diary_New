package sontungmtp.project.diary.Control.Adapter.Media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import sontungmtp.project.diary.R;

import java.util.ArrayList;

public class RcvMediaDemoAdapter extends RecyclerView.Adapter<RcvMediaDemoAdapter.ViewHolder> {
    private Context context;

    private ArrayList<String> mediaPaths;

    public RcvMediaDemoAdapter(ArrayList<String> mediaPaths) {
        this.mediaPaths = mediaPaths;
    }

    @NonNull
    @Override
    public RcvMediaDemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvmedia_demo, parent, false);
        return new RcvMediaDemoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvMediaDemoAdapter.ViewHolder holder, int position) {
        try{
            displayImageWithPath(mediaPaths.get(position), holder.imgMedia);
        }catch (Exception ignore){}

    }

    @Override
    public int getItemCount() {
        return mediaPaths.size();
    }
    public void displayImageWithPath(String Path, ImageView myImage){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.isMemoryCacheable();
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(Path)
                .into(myImage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMedia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMedia = itemView.findViewById(R.id.imgMedia);
        }
    }
}
