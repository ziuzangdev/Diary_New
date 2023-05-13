package sontungmtp.project.diary.Control.Activity;

import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

public interface IThemeManager {
    /**
     * All sub class must has this method, you can write logic for theme in this <br>
     * Theme data you can get from {@link AppThemeManager}
     */
    public void initTheme();
}
