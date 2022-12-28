package com.project.diary.Control.PreferencesManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManagerAutoMood {
    private static final String PREF_NAME = "AUTOMOOD";
    private static final String MODE = "ID_MOOD";
    private static PreferencesManagerAutoMood sInstance;
    private final SharedPreferences mPref;

    private PreferencesManagerAutoMood(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManagerAutoMood(context);
        }
    }

    public static synchronized PreferencesManagerAutoMood getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManagerAutoMood.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void saveAutoMood(boolean isAutoMood) {
        mPref.edit()
                .putBoolean(MODE, isAutoMood)
                .apply();
    }

    public Boolean readAutoMood() {
        return mPref.getBoolean(MODE, true);
    }
}

