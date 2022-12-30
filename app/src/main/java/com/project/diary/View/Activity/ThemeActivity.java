
package com.project.diary.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.Control.Activity.ThemeActivityControl;
import com.project.diary.Control.Adapter.Theme.ViewPagerThemeAdapter;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.Model.ThemeManager.Theme;
import com.project.diary.R;
import com.project.diary.databinding.ActivityMainBinding;
import com.project.diary.databinding.ActivityThemeBinding;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ThemeActivity extends AppCompatActivity {

    private ActivityThemeBinding binding;
    
    private ThemeActivityControl control;

    private ViewPagerThemeAdapter viewPagerThemeAdapter;

    private AppThemeManager appThemeManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeBinding.inflate(getLayoutInflater());
        control = new ThemeActivityControl(ThemeActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        loadingState(true);
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.vpTheme.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                initDataThemeTemplate();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        binding.mbtnGetTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appThemeManager.saveThemeId();
                Intent intent = new Intent(ThemeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initDataThemeTemplate() {
        String ID = viewPagerThemeAdapter.getCurrentPageThemeID();
        appThemeManager.setID_THEME(ID);
        appThemeManager.initData();
        binding.mbtnGetTheme.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
        binding.txtChooseTheme.setTextColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
        Glide.with(this).load(appThemeManager.getBG_THEME())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into((binding.imgBackground));
    }

    private void addControls() {
        appThemeManager = new AppThemeManager(ThemeActivity.this);
        viewPagerThemeAdapter = new ViewPagerThemeAdapter(binding.vpTheme, ThemeActivity.this);
        binding.vpTheme.setAdapter(viewPagerThemeAdapter);
        binding.vpTheme.setCurrentItem(Integer.MAX_VALUE / 2);
        binding.vpTheme.setClipToPadding(false);
        binding.vpTheme.setClipChildren(false);
        binding.vpTheme.setOffscreenPageLimit(3);
        binding.vpTheme.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        binding.vpTheme.setPageTransformer(compositePageTransformer);
        initDataThemeTemplate();
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!viewPagerThemeAdapter.isDisplayDataToScreen()){}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingState(false);
                    }
                });
            }
        });
        thread.start();
    }

    public void loadingState(boolean isLoading){
        if (isLoading){
            binding.animationView.setVisibility(View.VISIBLE);
        }else{
            binding.animationView.setVisibility(View.GONE);
        }
    }
}