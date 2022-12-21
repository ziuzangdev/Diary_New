package com.project.diary.Control.Adapter.Theme;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.project.diary.Control.Adapter.Diary.RcvCalandarDiaryAdapter;
import com.project.diary.Model.ThemeManager.Theme;
import com.project.diary.R;

import java.util.ArrayList;
import java.util.Hashtable;

// ViewPagerThemeAdapter is a custom Adapter for a ViewPager2 that displays a list of image resources
public class ViewPagerThemeAdapter extends RecyclerView.Adapter<ViewPagerThemeAdapter.ViewHolder> {

    // Hashtable to store the mapping of theme ID strings to drawable resource IDs
    private static final Hashtable<String, Integer> idAndImageTheme = Theme.getidAndImageTheme();

    private ViewPager2 viewPager2;
    private Context context, activityContext;

    private boolean isDisplayDataToScreen;

    public boolean isDisplayDataToScreen() {
        return isDisplayDataToScreen;
    }

    public ViewPagerThemeAdapter(ViewPager2 viewPager2, Context activityContext) {
        this.viewPager2 = viewPager2;
        this.activityContext = activityContext;
        isDisplayDataToScreen = false;
    }

    // Method called when a new ViewHolder is needed to display an item in the ViewPager2
    @NonNull
    @Override
    public ViewPagerThemeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the ViewPager2
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = inflater.inflate(R.layout.item_vptheme, parent, false);
        // Return a new ViewHolder with the inflated layout
        return new ViewPagerThemeAdapter.ViewHolder(view);
    }

    // Method called to bind an image resource to the ImageView in a ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewPagerThemeAdapter.ViewHolder holder, int position) {
        // Get the theme ID string at the specified position in the Hashtable
        String themeID = (String) idAndImageTheme.keySet().toArray()[position % idAndImageTheme.size()];
        // Load the image resource into the ImageView using Glide
        holder.loadImage(Theme.getImageById(themeID));
    }

    // Method to return the number of items in the Adapter's data set
    @Override
    public int getItemCount() {
        // Return the size of the Hashtable of themes, multiplied by a large number to enable infinite scrolling
        return Integer.MAX_VALUE;
    }

    @Override
    public int getItemViewType(int position) {
        return POSITION_NONE;
    }

    // Method to return the theme ID string of the idAndImageTheme Hashtable of the current page
    public String getCurrentPageThemeID() {
        // Get the current page position in the ViewPager2
        int currentPage = viewPager2.getCurrentItem();
        // Get the theme ID string at the current page position in the Hashtable
        return (String) idAndImageTheme.keySet().toArray()[currentPage % idAndImageTheme.size()];
    }

    // ViewHolder inner class to hold a reference to the ImageView in the item layout
    public class ViewHolder extends RecyclerView.ViewHolder {

        // ImageView to display the image resource
        ImageView imgItemTheme;

        // Constructor to initialize the ImageView
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemTheme = itemView.findViewById(R.id.imgItemTheme);
        }

        // Method to load an image resource into the ImageView using Glide
        public void loadImage(int resID){
            Glide.with(activityContext)
                    .load(resID)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            isDisplayDataToScreen = true;
                            return false;
                        }
                    })
                    .into(imgItemTheme);
        }
    }
}