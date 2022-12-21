package com.project.diary.Control.Activity;

public interface IThemeManager {
    /**
     * All sub class must has this method, you can write logic for theme in this <br>
     * Theme data you can get from {@link com.project.diary.Model.ThemeManager.AppThemeManager}
     */
    public void initTheme();
}
