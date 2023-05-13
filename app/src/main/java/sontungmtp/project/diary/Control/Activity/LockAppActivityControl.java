package sontungmtp.project.diary.Control.Activity;

import android.content.Context;

import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Model.Lock.ISecurityPassword;
import sontungmtp.project.diary.Model.Lock.SecurityPassword;
import sontungmtp.project.diary.Model.SQLite.SQLite;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

/**
 * The LockAppActivityControl class extends the RootControl class and is responsible for managing the lock mode for the app.
 * It also provides access to an instance of the AppThemeManager and ISecurityPassword classes.
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class LockAppActivityControl extends RootControl {
    private boolean isLockMode;

    private SQLite sqLite;

    private ISecurityPassword iSecurityPassword;

    private AppThemeManager appThemeManager;

    /**
     * Returns an instance of the AppThemeManager class.
     *
     * @return an AppThemeManager instance
     */
    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    /**
     * Returns an instance of the ISecurityPassword class.
     *
     * @return an ISecurityPassword instance
     */
    public ISecurityPassword getiSecurityPassword() {
        return iSecurityPassword;
    }

    /**
     * Sets an instance of the ISecurityPassword class.
     *
     * @param iSecurityPassword an instance of the ISecurityPassword class
     */
    public void setiSecurityPassword(ISecurityPassword iSecurityPassword) {
        this.iSecurityPassword = iSecurityPassword;
    }

    /**
     * Constructs a new LockAppActivityControl object and initializes the sqLite and appThemeManager fields.
     *
     * @param context a Context object used to access application-specific resources
     */
    public LockAppActivityControl(Context context) {
        super(context);
        sqLite = new SQLite(context);
        isLockMode = sqLite.getSqLiteControl().isLockMode();
        iSecurityPassword = new SecurityPassword(getContext());
        appThemeManager =  new AppThemeManager(context);
    }

    /**
     * Returns the current lock mode.
     *
     * @return true if the lock mode is enabled, false otherwise
     */
    public boolean isLockMode() {
        return isLockMode;
    }

    /**
     * Sets the lock mode and updates the value in the SQLite database.
     *
     * @param lockMode true to enable lock mode, false to disable it
     */
    public void setLockMode(boolean lockMode) {
        isLockMode = lockMode;
        if(lockMode){
            sqLite.getSqLiteControl().updateLockMode(1);
        }else{
            sqLite.getSqLiteControl().updateLockMode(0);
        }
    }
}