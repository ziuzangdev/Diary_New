package sontungmtp.project.diary.Control.Adapter.Media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.View.Fragment.MediaFragment;

import java.io.IOException;
import java.util.ArrayList;

public class RcvMediaAdapter extends RecyclerView.Adapter<RcvMediaAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mediaPath;

    private ArrayList<String> pathChose;

    private RootControl rootControl;

    private int MODE;

    public RcvMediaAdapter(ArrayList<String> mediaPath, int MODE) {
        this.mediaPath = mediaPath;
        this.MODE = MODE;
        pathChose = new ArrayList<>();
    }

    public ArrayList<String> getPathChose() {
        return pathChose;
    }

    @NonNull
    @Override
    public RcvMediaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        rootControl = new RootControl(context);
        View view = inflater.inflate(R.layout.item_rcvmedia, parent, false);
        return new RcvMediaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvMediaAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(MODE == MediaFragment.MODE_VIDEO){
           setData(mediaPath.get(position), holder.txtDurationVideo, holder.imgDemo);
        }else{
            rootControl.displayImageWithPath(mediaPath.get(position), holder.imgDemo);
            holder.txtDurationVideo.setVisibility(View.GONE);
        }
        if(pathChose.contains(mediaPath.get(position))){
            holder.cvChoose.setVisibility(View.VISIBLE);
            holder.txtNumberChoose.setText(String.valueOf(pathChose.indexOf(mediaPath.get(position)) + 1));
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if(holder.cvChoose.getVisibility() == View.INVISIBLE){
                    holder.cvChoose.setVisibility(View.VISIBLE);
                    pathChose.add(mediaPath.get(position));
                    holder.txtNumberChoose.setText(String.valueOf(pathChose.indexOf(mediaPath.get(position)) + 1));
                }else{
                    holder.cvChoose.setVisibility(View.INVISIBLE);
                    pathChose.remove(mediaPath.get(position));
                    notifyDataSetChanged();
                }
            }
        };
        holder.Root.setOnClickListener(onClickListener);
        holder.cvChoose.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return mediaPath.size();
    }

    private void setData(String path, TextView txtDurationVideo , ImageView imgDemo){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(path);
                    long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    try {
                        retriever.release();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rootControl.displayImageWithPath(path, imgDemo);
                            txtDurationVideo.setText(rootControl.formatDuration(duration));
                        }
                    });
                }catch (Exception e){
                    Log.e("RcvMediaAdapter.java", "Exception when create duration video with path: " + path);
                }

            }
        });
        thread.start();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDemo;
        TextView txtDurationVideo,
                txtNumberChoose;

        MaterialCardView cvChoose;

        RelativeLayout Root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDemo = itemView.findViewById(R.id.imgDemo);
            txtDurationVideo = itemView.findViewById(R.id.txtDurationVideo);
            txtNumberChoose = itemView.findViewById(R.id.txtNumberChoose);
            cvChoose = itemView.findViewById(R.id.cvChoose);
            Root = itemView.findViewById(R.id.Root);
        }
    }
}
