package com.project.diary.Control.ThemeManager;

import com.project.diary.R;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides access to color and background resources for a specific theme.
 * The theme is identified by the ID_THEME parameter.
 */
public final class AppThemeManagerControl {

    /**
     * Map of arrays of color codes for each supported theme.
     */
    private static final Map<String, String[]> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("P41", new String[]{
                "#0096D1",
                "#FFF4EA",
                "#A8EAD5",
                "#3EBDC6",
                "#D6EDF7"
        });
        // Add more themes here as needed
    }

    /**
     * Map of arrays of resource IDs for the background images for each supported theme.
     */
    private static final Map<String, Integer[]> BG_MAP = new HashMap<>();
    static {
        BG_MAP.put("P41", new Integer[]{
                R.drawable.p41_bg_theme,
                R.drawable.p41_background_main_1
        });
        // Add more themes here as needed

    }

    /**
     * Returns an array of strings representing the hexadecimal color codes for the theme specified by ID_THEME.
     * @param ID_THEME the ID of the theme for which the color codes are requested.
     * @return the array of color codes for the specified theme, or null if the theme is not supported.
     */
    public static String[] getListColor(String ID_THEME){
        return COLOR_MAP.get(ID_THEME);
    }

    /**
     * Returns an array of integers representing the resource IDs for the background images for the theme specified by ID_THEME.
     * @param ID_THEME the ID of the theme for which the background images are requested.
     * @return the array of resource IDs for the background images for the specified theme, or null if the theme is not supported.
     */
    public static Integer[] getBackgrounds(String ID_THEME){
        return BG_MAP.get(ID_THEME);
    }
}
