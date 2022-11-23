package com.project.diary.View.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.GravityCompat;

import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private MainActivityControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        control = new MainActivityControl(MainActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.imgNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.imgOpenCalanderActivity.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalandarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        binding.navigationView.setItemIconTintList(null);
    }


}