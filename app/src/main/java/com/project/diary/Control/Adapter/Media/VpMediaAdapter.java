package com.project.diary.Control.Adapter.Media;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.project.diary.View.Fragment.MediaFragment;

import java.util.ArrayList;

public class VpMediaAdapter extends FragmentStatePagerAdapter {
    private ArrayList<MediaFragment> fragments;
    public VpMediaAdapter(@NonNull FragmentManager fm, ArrayList<MediaFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
