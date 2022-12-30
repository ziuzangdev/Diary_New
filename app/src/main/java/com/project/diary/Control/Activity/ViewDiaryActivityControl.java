package com.project.diary.Control.Activity;

import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;

public class ViewDiaryActivityControl extends RootControl {
    private AppThemeManager appThemeManager;

    private SQLite sqLite;

    public SQLite getSqLite() {
        return sqLite;
    }

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    public ViewDiaryActivityControl(Context context) {
        super(context);
        appThemeManager = new AppThemeManager(context);
        sqLite = new SQLite(context);
    }
}
