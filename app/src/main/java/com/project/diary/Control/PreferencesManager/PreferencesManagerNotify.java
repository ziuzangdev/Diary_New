package com.project.diary.Control.PreferencesManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManagerNotify {
    private static final String PREF_NAME = "NOTIFY";
    private static final String MODE = "IS_SETUP";
    private static PreferencesManagerNotify sInstance;
    private final SharedPreferences mPref;

    private PreferencesManagerNotify(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManagerNotify(context);
        }
    }

    public static synchronized PreferencesManagerNotify getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManagerNotify.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void saveSetup(boolean isAutoMood) {
        mPref.edit()
                .putBoolean(MODE, isAutoMood)
                .apply();
    }

    public Boolean readSetup() {
        return mPref.getBoolean(MODE, false);
    }
}

