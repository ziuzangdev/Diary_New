package com.project.diary.View.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.project.diary.Control.Activity.LockAppActivityControl;
import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Lock.ISecurityPassword;
import com.project.diary.Model.Lock.MyLock;
import com.project.diary.Model.Lock.SecurityPassword;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.R;
import com.project.diary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private MainActivityControl control;

    private LockAppActivityControl lockAppActivityControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockAppActivityControl = new LockAppActivityControl(MainActivity.this);
        if(lockAppActivityControl.isLockMode()){
            MyLock.checkPassword(MainActivity.this);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        control = new MainActivityControl(MainActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        SQLite sqLite = new SQLite(MainActivity.this);
        addControls();
        addEvents();
    }



    @Override
    protected void onResume() {
        super.onResume();
        initRcvDiary();
    }

    private void addEvents() {
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_item_lockapp){
                    Intent intent = new Intent(MainActivity.this, LockAppActivity.class);
                    startActivity(intent);
                }else if(item.getItemId() == R.id.menu_item_exportimport){
                    Intent intent = new Intent(MainActivity.this, ExportImportActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        MyLock.forgotPassword(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISecurityPassword securityPassword = new SecurityPassword(MainActivity.this);
                if(securityPassword.isHasPassword()){
                    Intent intent = new Intent(MainActivity.this, SecurityPasswordActivity.class);
                    intent.putExtra(MyLock.FORGOT_PASS_MODE, MyLock.FORGOT_PASS_MODE);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "You have not set up a security question!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.edtxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                control.getDiariesSearch().clear();
                Thread threadSearch = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!s.equals("")){
                            for(Diary diary : control.getDiaries()){
                                if(diary.getTittle().contains(s)){
                                    control.getDiariesSearch().add(diary);
                                }
                            }
                            control.setRcvDiaryAdapter(new RcvDiaryAdapter(control.getDiariesSearch(), binding, MainActivity.this));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.rcvDiary.setHasFixedSize(true);
                                    binding.rcvDiary.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                    binding.rcvDiary.setAdapter(control.getRcvDiaryAdapter());
                                }
                            });
                        }else{
                            initRcvDiary();
                        }

                    }
                });
                threadSearch.start();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.imgSearchOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDisplaySearchView(true);
            }
        });
        binding.imgSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDisplaySearchView(false);
            }
        });
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

        binding.cvNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    intent = new Intent(MainActivity.this, DiaryActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    private void isDisplaySearchView(boolean b) {
        if(b){
            binding.ToolbarView1.setVisibility(View.GONE);
            binding.ToolbarView2.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.VISIBLE);
        }else{
            binding.searchView.setVisibility(View.GONE);
            binding.ToolbarView1.setVisibility(View.VISIBLE);
            binding.ToolbarView2.setVisibility(View.VISIBLE);
        }
    }

    private void addControls() {
        binding.navigationView.setItemIconTintList(null);
        initRcvDiary();
    }

    public void initRcvDiary() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                control.InitRcvDiaryItem();
                control.setRcvDiaryAdapter(new RcvDiaryAdapter(control.getDiaries(), binding, MainActivity.this));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (control.getDiaries().size() > 0){
                            binding.animationView.setVisibility(View.GONE);
                            binding.rcvDiary.setHasFixedSize(true);
                            binding.rcvDiary.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            binding.rcvDiary.setAdapter(control.getRcvDiaryAdapter());
                        }else{
                            binding.animationView.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        });
        thread.start();
    }


}