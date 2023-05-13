package sontungmtp.project.diary.Control.Adapter.Emoji;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import sontungmtp.project.diary.Model.Emoji.Emoji;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.View.Activity.DiaryActivity;
import sontungmtp.project.diary.databinding.ActivityDiaryBinding;

import java.util.ArrayList;

public class RcvStatusPickerAdapter extends RecyclerView.Adapter<RcvStatusPickerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Emoji> emojis;

    private Dialog dialog;

    private String currentEmojiText;

    private ActivityDiaryBinding binding;

    private Context activityContext;

    public RcvStatusPickerAdapter(ArrayList<Emoji> emojis, Dialog dialog, ActivityDiaryBinding binding, Context activityContext) {
        this.emojis = emojis;
        this.dialog = dialog;
        this.binding = binding;
        this.activityContext = activityContext;
    }

    public String getCurrentEmojiText() {
        if(currentEmojiText == null){
            return "\ud83d\ude00";
        }else{
            return currentEmojiText;
        }
    }

    public void setCurrentEmojiText(String currentEmojiText) {
        this.currentEmojiText = currentEmojiText;
    }

    @NonNull
    @Override
    public RcvStatusPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcvstatus, parent, false);
        return new RcvStatusPickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvStatusPickerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtStatus.setText(emojis.get(position).getEmojiText());
        holder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setCurrentEmojiText(emojis.get(position).getEmojiText());
                binding.txtEmojiStatus.setText(getCurrentEmojiText());
                ((DiaryActivity)activityContext).getDiary().setStatus(getCurrentEmojiText());
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
