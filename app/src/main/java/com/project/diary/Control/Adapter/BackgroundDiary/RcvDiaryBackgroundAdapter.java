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

/**
 * A RecyclerView.Adapter for displaying a list of BackgroundDiary objects as items in a RecyclerView.
 * Allows the user to select a background image for a Diary entry.
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class RcvDiaryBackgroundAdapter extends RecyclerView.Adapter<RcvDiaryBackgroundAdapter.ViewHolder> {
    private PackageBackgroundDiary packageBackgroundDiary;
    private Context context, activityContext;

    /**
     * Constructor for the RcvDiaryBackgroundAdapter.
     * Initializes the PackageBackgroundDiary for the specified position.
     *
     * @param activityContext the context of the parent Activity
     * @param position the position of the PackageBackgroundDiary to retrieve
     */
    public RcvDiaryBackgroundAdapter(Context activityContext, int position) {
        this.activityContext = activityContext;
        packageBackgroundDiary = PackageBackgroundDiaryControl.getPackage(position);
    }

    /**
     * Creates a new ViewHolder when a new item is added to the RecyclerView.
     *
     * @param parent the parent ViewGroup
     * @param viewType the type of the View
     * @return the new ViewHolder
     */
    @NonNull
    @Override
    public RcvDiaryBackgroundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_rcv_diary_background, parent, false);
        return new RcvDiaryBackgroundAdapter.ViewHolder(view);
    }

    /**
     * Binds a ViewHolder's views to the data for the corresponding position in the data set.
     * Sets an OnClickListener for the background image that allows the user to select the image as the background.
     *
     * @param holder the ViewHolder to bind the data to
     * @param position the position of the data in the data set
     */
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


    /**
     * Returns the total number of items in the data set.
     *
     * @return the total number of items in the data set
     */
    @Override
    public int getItemCount() {
        return packageBackgroundDiary.getBackgroundDiaryArrayList().size();
    }

    /**
     * A ViewHolder class that represents a single item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBackground = itemView.findViewById(R.id.imgBackground);
        }

        /**
         * Loads an image from the given resource id into the ImageView using Glide.
         *
         * @param resourceId the resource id of the image to load
         */
        public void loadImageWithGlide(int resourceId) {
            Glide.with(activityContext)
                    .load(resourceId)
                    .into(imgBackground);
        }
    }
}
