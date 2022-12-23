package com.project.diary.Control.Activity;

import android.content.Context;

import com.project.diary.Control.Adapter.Diary.RcvTemplateDiaryAdapter;
import com.project.diary.Control.RootControl;
import com.project.diary.Model.ThemeManager.AppThemeManager;

public class ActivityTemplateControl extends RootControl {
    private AppThemeManager appThemeManager;

    private RcvTemplateDiaryAdapter rcvTemplateDiaryAdapter;

    public RcvTemplateDiaryAdapter getRcvTemplateDiaryAdapter() {
        return rcvTemplateDiaryAdapter;
    }

    public void setRcvTemplateDiaryAdapter(RcvTemplateDiaryAdapter rcvTemplateDiaryAdapter) {
        this.rcvTemplateDiaryAdapter = rcvTemplateDiaryAdapter;
    }

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    public ActivityTemplateControl(Context context) {
        super(context);
        appThemeManager = new AppThemeManager(context);
    }
}
