package com.project.diary.Model.ThemeManager;

import com.project.diary.R;

import java.util.Hashtable;

// Theme class represents a set of visual style settings for an application.
public final class Theme {
    // idAndImageTheme is a mapping of theme ID strings to drawable resource IDs.
    private static final Hashtable<String, Integer> idAndImageTheme = new Hashtable<>();

    // Populate idAndImageTheme with a default theme ID and drawable resource.
    static {
        idAndImageTheme.put("P41", R.drawable.p41_theme_demo);
    }

    public static Hashtable<String, Integer> getidAndImageTheme(){
        return idAndImageTheme;
    }
    // Returns the drawable resource ID associated with the given theme ID.
    // If the theme ID is not found, returns -1.
    public static Integer getImageById(String ID){
        return idAndImageTheme.getOrDefault(ID, -1);
    }



}