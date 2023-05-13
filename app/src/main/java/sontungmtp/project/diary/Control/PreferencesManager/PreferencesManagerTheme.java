package sontungmtp.project.diary.Control.PreferencesManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManagerTheme {
    private static final String PREF_NAME = "THEME";
    private static final String MODE = "ID_THEME";
    private static PreferencesManagerTheme sInstance;
    private final SharedPreferences mPref;

    private PreferencesManagerTheme(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManagerTheme(context);
        }
    }

    public static synchronized PreferencesManagerTheme getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManagerTheme.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    // Lưu giá trị int vào Shared Preferences
    public void saveThemeId(String ID_THEME) {
        mPref.edit()
                .putString(MODE, ID_THEME)
                .apply();
    }

    // Đọc giá trị int từ Shared Preferences, trả về giá trị mặc định là P41 nếu không tìm thấy
    public String readThemeId() {
        return mPref.getString(MODE, "P41");
    }
}
