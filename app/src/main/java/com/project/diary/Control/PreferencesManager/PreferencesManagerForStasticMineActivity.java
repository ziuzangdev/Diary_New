package com.project.diary.Control.PreferencesManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManagerForStasticMineActivity {
    private static final String PREF_NAME = "preferences";
    private static final String MODE = "mode";
    private static PreferencesManagerForStasticMineActivity sInstance;
    private final SharedPreferences mPref;

    private PreferencesManagerForStasticMineActivity(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManagerForStasticMineActivity(context);
        }
    }

    public static synchronized PreferencesManagerForStasticMineActivity getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManagerForStasticMineActivity.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    // Lưu giá trị int vào Shared Preferences
    public void saveModeStatistic(int mode) {
        mPref.edit()
                .putInt(MODE, mode)
                .apply();
    }

    // Đọc giá trị int từ Shared Preferences, trả về giá trị mặc định là 30 nếu không tìm thấy
    public int readModeStatistic() {
        return mPref.getInt(MODE, 30);
    }
}