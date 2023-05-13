package sontungmtp.project.diary.Control.Activity;

import android.content.Context;

import sontungmtp.project.diary.Control.Adapter.Diary.RcvTemplateDiaryAdapter;
import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

/**
 * The ActivityTemplateControl class is a subclass of RootControl that manages the display and functionality of a template feature in an application.
 *<br>
 * It has properties for an instance of the AppThemeManager class and an instance of the RcvTemplateDiaryAdapter class. It also has a constructor that takes in a Context object and initializes the appThemeManager instance. It has methods for getting and setting the RcvTemplateDiaryAdapter instance and getting the appThemeManager instance.
 *<br>
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class ActivityTemplateControl extends RootControl {
    private AppThemeManager appThemeManager;
    private RcvTemplateDiaryAdapter rcvTemplateDiaryAdapter;

    /**
     * Constructs a new ActivityTemplateControl object.
     *
     * @param context The Context object to be used for initialization
     */
    public ActivityTemplateControl(Context context) {
        super(context);
        appThemeManager = new AppThemeManager(context);
    }

    // Methods

    /**
     * Returns the RcvTemplateDiaryAdapter instance.
     *
     * @return RcvTemplateDiaryAdapter The RcvTemplateDiaryAdapter instance
     */
    public RcvTemplateDiaryAdapter getRcvTemplateDiaryAdapter() {
        return rcvTemplateDiaryAdapter;
    }

    /**
     * Sets the RcvTemplateDiaryAdapter instance to the provided rcvTemplateDiaryAdapter argument.
     *
     * @param rcvTemplateDiaryAdapter The new RcvTemplateDiaryAdapter instance
     */
    public void setRcvTemplateDiaryAdapter(RcvTemplateDiaryAdapter rcvTemplateDiaryAdapter) {
        this.rcvTemplateDiaryAdapter = rcvTemplateDiaryAdapter;
    }

    /**
     * Returns the appThemeManager instance.
     *
     * @return AppThemeManager The appThemeManager instance
     */
    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

}

