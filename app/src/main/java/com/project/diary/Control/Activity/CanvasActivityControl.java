package com.project.diary.Control.Activity;

import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.ThemeManager.AppThemeManager;

public class CanvasActivityControl extends RootControl {
    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    public CanvasActivityControl(Context context) {
        super(context);
        appThemeManager = new AppThemeManager(context);
    }
}
