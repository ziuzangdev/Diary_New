package sontungmtp.project.diary.View.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import sontungmtp.project.diary.Control.Activity.IThemeManager;
import sontungmtp.project.diary.Control.Activity.LockAppActivityControl;
import sontungmtp.project.diary.Control.Activity.MainActivityControl;
import sontungmtp.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import sontungmtp.project.diary.Control.Notification.NotificationScheduler;
import sontungmtp.project.diary.Model.Diary.Diary;
import sontungmtp.project.diary.Model.Lock.ISecurityPassword;
import sontungmtp.project.diary.Model.Lock.MyLock;
import sontungmtp.project.diary.Model.Lock.SecurityPassword;
import sontungmtp.project.diary.Model.SQLite.SQLite;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.databinding.ActivityMainBinding;

import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IThemeManager {
    private ActivityMainBinding binding;

    private MainActivityControl control;

    private LockAppActivityControl lockAppActivityControl;

    private boolean doubleBackToExitPressedOnce = false;

    private boolean sortMode = true;
    private InterstitialAd mInterstitialAd;
    private int type_show_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockAppActivityControl = new LockAppActivityControl(MainActivity.this);
        if (lockAppActivityControl.isLockMode()) {
            MyLock.checkPassword(MainActivity.this);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        control = new MainActivityControl(MainActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        SQLite sqLite = new SQLite(MainActivity.this);
        addControls();
        addEvents();
        MobileAds.initialize(this, initializationStatus -> {
        });

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isOpen()) {
            binding.drawerLayout.closeDrawers();
        } else {
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
        loadInterstitialAd();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (control.getRcvDiaryAdapter() == null) {
                }
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
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                    type_show_ads = 1;
                } else {
                    Intent intent = new Intent(MainActivity.this, MineActivity.class);
                    startActivity(intent);

                }
            }
        });
        binding.imgSort.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                sortMode = !sortMode;
                control.sortArrayListByCalendarDay(control.getDiaries(), sortMode);
                control.setRcvDiaryAdapter(new RcvDiaryAdapter(control.getDiaries(), binding, MainActivity.this));
                binding.rcvDiary.setHasFixedSize(true);
                binding.rcvDiary.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                binding.rcvDiary.setAdapter(control.getRcvDiaryAdapter());
            }
        });
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_item_lockapp) {
                    Intent intent = new Intent(MainActivity.this, LockAppActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_item_backuprestore) {
                    Intent intent = new Intent(MainActivity.this, BackupRestoreActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_item_theme) {
                    Toast.makeText(getApplicationContext(), "This feature will avaiable soon!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
//                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_item_setting) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_item_moreapp) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=SonTungMTP"));
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_item_shareapp) {

                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en";
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    startActivity(Intent.createChooser(intent, url));
                }else if (item.getItemId() == R.id.menu_item_pro_version){
                    Toast.makeText(getApplicationContext(), "This feature will avaiable soon!", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });
        MyLock.forgotPassword(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISecurityPassword securityPassword = new SecurityPassword(MainActivity.this);
                if (securityPassword.isHasPassword()) {
                    Intent intent = new Intent(MainActivity.this, SecurityPasswordActivity.class);
                    intent.putExtra(MyLock.FORGOT_PASS_MODE, MyLock.FORGOT_PASS_MODE);
                    startActivity(intent);
                } else {
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
                        if (!s.equals("")) {
                            for (Diary diary : control.getDiaries()) {
                                if (diary.getTittle().contains(s) ||
                                        diary.getDiaryData().getData().replaceAll("\\<[^>]*>", "").contains(s)) {
                                    control.getDiariesSearch().add(diary);
                                }
                            }
                            control.setRcvDiaryAdapter(new RcvDiaryAdapter(control.getDiariesSearch(), binding, MainActivity.this));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.rcvDiary.setHasFixedSize(true);
                                    binding.rcvDiary.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                                    binding.rcvDiary.setAdapter(control.getRcvDiaryAdapter());
                                }
                            });
                        } else {
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
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                    type_show_ads = 2;
                } else {
                    Intent intent = new Intent(MainActivity.this, CalandarActivity.class);
                    startActivity(intent);

                }
            }
        });

        binding.cvNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                    type_show_ads = 3;
                } else {
                    Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void isDisplaySearchView(boolean b) {
        if (b) {
            binding.ToolbarView1.setVisibility(View.GONE);
            binding.ToolbarView2.setVisibility(View.GONE);
            binding.searchView.setVisibility(View.VISIBLE);
            binding.edtxtSearch.clearComposingText();
            control.getRcvDiaryAdapter().setSearchMode(true);
        } else {
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
                control.sortArrayListByCalendarDay(control.getDiaries(), sortMode);
                control.setRcvDiaryAdapter(new RcvDiaryAdapter(control.getDiaries(), binding, MainActivity.this));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (control.getDiaries().size() > 0) {
                            binding.animationView.setVisibility(View.GONE);
                            binding.rcvDiary.setHasFixedSize(true);
                            binding.rcvDiary.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            binding.rcvDiary.setAdapter(control.getRcvDiaryAdapter());
                        } else {
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

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getResources().getString(R.string.ads_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        switch (type_show_ads) {
                            case 1:
                                Intent intent = new Intent(MainActivity.this, MineActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                Intent intent2 = new Intent(MainActivity.this, CalandarActivity.class);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(MainActivity.this, DiaryActivity.class);
                                startActivity(intent3);
                                break;
                            default:
                                break;

                        }

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });

    }

}