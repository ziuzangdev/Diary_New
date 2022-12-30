package com.project.diary.View.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Activity.LockAppActivityControl;
import com.project.diary.Control.Activity.MainActivityControl;
import com.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import com.project.diary.Control.Notification.NotificationScheduler;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Lock.ISecurityPassword;
import com.project.diary.Model.Lock.MyLock;
import com.project.diary.Model.Lock.SecurityPassword;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.R;
import com.project.diary.databinding.ActivityMainBinding;

import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IThemeManager {
    private ActivityMainBinding binding;

    private MainActivityControl control;

    private LockAppActivityControl lockAppActivityControl;

    private boolean doubleBackToExitPressedOnce = false;

    private boolean sortMode = true;

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
    public void onBackPressed() {
        if( binding.drawerLayout.isOpen()){
            binding.drawerLayout.closeDrawers();
        }else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRcvDiary();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (control.getRcvDiaryAdapter() == null){}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isDisplaySearchView(false);
                    }
                });
            }
        });
        thread.start();
    }

    private void addEvents() {
        binding.imgMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MineActivity.class);
                startActivity(intent);
            }
        });
        binding.imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortMode = !sortMode;
                control.sortArrayListByCalendarDay(control.getDiaries(), sortMode);
                control.getRcvDiaryAdapter().notifyDataSetChanged();
            }
        });
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_item_lockapp){
                    Intent intent = new Intent(MainActivity.this, LockAppActivity.class);
                    startActivity(intent);
                }else if(item.getItemId() == R.id.menu_item_backuprestore){
                    Intent intent = new Intent(MainActivity.this, BackupRestoreActivity.class);
                    startActivity(intent);
                }else if(item.getItemId() == R.id.menu_item_theme){
                    Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
                    startActivity(intent);
                }else if(item.getItemId() == R.id.menu_item_setting){
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }else if(item.getItemId() == R.id.menu_item_moreapp){
                    String url = "https://www.example.com";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }else if(item.getItemId() == R.id.menu_item_shareapp){
//                    String packageName = getPackageName();
//                    ApplicationInfo appInfo = null;
//                    try {
//                        appInfo = getPackageManager().getApplicationInfo(packageName, 0);
//                        String apkPath = appInfo.publicSourceDir;
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setType("application/vnd.android.package-archive");
//                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + apkPath));
//                        startActivity(Intent.createChooser(intent, "Share app via"));
//                    } catch (PackageManager.NameNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
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
                                if(diary.getTittle().contains(s) ||
                                        diary.getDiaryData().getData().replaceAll("\\<[^>]*>","").contains(s)){
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
                initRcvDiary();
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

    public void isDisplaySearchView(boolean b) {
        if(b){
            binding.ToolbarView1.setVisibility(View.GONE);
            binding.ToolbarView2.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.VISIBLE);
            binding.edtxtSearch.clearComposingText();
            control.getRcvDiaryAdapter().setSearchMode(true);
        }else{
            binding.searchView.setVisibility(View.GONE);
            binding.ToolbarView1.setVisibility(View.VISIBLE);
            binding.ToolbarView2.setVisibility(View.VISIBLE);
            control.getRcvDiaryAdapter().setSearchMode(false);
        }
    }

    private void addControls() {
        initTheme();
        binding.navigationView.setItemIconTintList(null);
        initRcvDiary();
        initNotify();
    }

    private void initNotify() {
        Map<String, String> schedule = new Hashtable<>();
        schedule.put("Good morning, have a nice day. Let's start the day with a brand new diary page", "06:00");
        schedule.put("How are you? Write your wonderful morning experience, don't forget it, work hard in journaling every day to express your feelings better UwU", "12:00");
        schedule.put("How is your day? No matter what, remember \"Everything will be okay\" UwU", "20:00");
        schedule.put("Good night UwU, see you tomorrow UwU", "00:00");
        NotificationScheduler notificationScheduler = new NotificationScheduler(MainActivity.this, schedule);
        notificationScheduler.scheduleNotifications();
    }


    public void initRcvDiary() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.rcvDiary.removeAllViews();
                    }
                });
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

    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.rlBackground.setBackgroundResource(appThemeManager.getBG_THEME());
        binding.llNewDiary.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
        binding.navigationView.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
        appThemeManager.initMenu(binding.navigationView);
    }
}