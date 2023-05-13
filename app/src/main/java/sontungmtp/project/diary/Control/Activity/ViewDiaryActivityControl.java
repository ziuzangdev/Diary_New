package sontungmtp.project.diary.Control.Activity;

import android.content.Context;

import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Model.SQLite.SQLite;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

/**
 * Class for controlling the ViewDiaryActivity
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
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
