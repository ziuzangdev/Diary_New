package sontungmtp.project.diary.Model.ThemeManager;

import android.content.Context;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;
import sontungmtp.project.diary.Control.PreferencesManager.PreferencesManagerTheme;
import sontungmtp.project.diary.Control.ThemeManager.AppThemeManagerControl;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.databinding.ActivityCanvasBinding;

/**
 * This class manages the theme for the app. It retrieves the theme ID and associated color and background resources
 * from the PreferencesManagerTheme and the AppThemeManagerControl classes, and provides methods to access and update
 * these values.
 */
public class AppThemeManager {

    // ID of the current theme
    private String ID_THEME;
    // Resource ID for the background of the main activity
    private int BG_THEME;
    // Resource ID for the background of the mine activity
    private int BG_THEME_MINE;
    // Array of color codes for the current theme
    private String[] paletteColor;

    private Context context;

    public void setID_THEME(String ID_THEME) {
        this.ID_THEME = ID_THEME;
    }

    public int getBG_THEME() {
        return BG_THEME;
    }

    public int getBG_THEME_MINE() {
        return BG_THEME_MINE;
    }

    public String[] getPaletteColor() {
        return paletteColor;
    }

    /**
     * Constructor for the AppThemeManager. Initializes the context and retrieves the theme ID from the
     * PreferencesManagerTheme.
     * @param context the context of the activity using the AppThemeManager
     */
    public AppThemeManager(Context context) {
        this.context = context;
        PreferencesManagerTheme.initializeInstance(context);
        PreferencesManagerTheme prefManager = PreferencesManagerTheme.getInstance();
        ID_THEME = prefManager.readThemeId();
        initData();
    }

    /**
     * Initializes the color, background, and mine background resources for the current theme.
     */
    public void initData() {
        try{
            paletteColor = AppThemeManagerControl.getListColor(ID_THEME);
            BG_THEME = AppThemeManagerControl.getBackgrounds(ID_THEME)[0];
            BG_THEME_MINE = AppThemeManagerControl.getBackgrounds(ID_THEME)[1];
        }catch (Exception e){
            Log.e("AppThemeManager", "ID_THEME: Can't find ID_THEME");
        }
    }

    /**
     * Saves the current theme ID to the PreferencesManagerTheme.
     */
    public void saveThemeId(){
        PreferencesManagerTheme.initializeInstance(context);
        PreferencesManagerTheme prefManager = PreferencesManagerTheme.getInstance();
        prefManager.saveThemeId(ID_THEME);
    }

    // TODO: MUST CHANCE CODE BY YOUR SELF
    /**
     * Sets the state of the paint and eraser buttons in the canvas activity based on the current theme.
     * @param binding the data binding object for the canvas activity
     */
    public void setStateButtonForRatioButtonCanvas(ActivityCanvasBinding binding) {
        if(ID_THEME.equals("P41")){
            binding.rbtnPaintTool.setBackgroundResource(R.drawable.state_icon_paint_square_p41);
            binding.rbtnEraser.setBackgroundResource(R.drawable.state_icon_eraser_square_p41);
        }
    }

    /**
     * Initializes the menu for the NavigationView based on the current theme.
     * @param navigationView the NavigationView for which the menu will be initialized
     */
    public void initMenu(NavigationView navigationView) {
        if(ID_THEME.equals(("P41"))){
            navigationView.inflateMenu(R.menu.menu_navdrawer_mainactivity_p41);
        }
    }
}
