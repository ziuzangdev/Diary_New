package com.project.diary.Control.BackgroundDiaryManager;

import com.project.diary.Model.BackgroundDiaryManager.BackgroundDiary;
import com.project.diary.Model.BackgroundDiaryManager.PackageBackgroundDiary;
import com.project.diary.R;

import java.util.ArrayList;

/**
 * This class provides access to the data of the package backgrounds and diary backgrounds.
 * The data includes the names and lists of backgrounds in each package, as well as the IDs and resource IDs of all backgrounds.
 */
public final class PackageBackgroundDiaryControl {
    private static final ArrayList<PackageBackgroundDiary> packages = new ArrayList<>();
    private static final ArrayList<BackgroundDiary> backgrounds = new ArrayList<>();

    static {
        // Initialize data for PackageBackgroundDiary
        ArrayList<BackgroundDiary> package1Backgrounds = new ArrayList<>();
        package1Backgrounds.add(new BackgroundDiary("image_1", R.drawable.theme_none));
        package1Backgrounds.add(new BackgroundDiary("image_2", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_3", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_4", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_5", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_6", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_7", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_8", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_9", R.drawable.p41_bg_theme));
        package1Backgrounds.add(new BackgroundDiary("image_10", R.drawable.p41_bg_theme));
        packages.add(new PackageBackgroundDiary("Package 1", package1Backgrounds));

        ArrayList<BackgroundDiary> package2Backgrounds = new ArrayList<>();
        package2Backgrounds.add(new BackgroundDiary("image_11", R.drawable.theme_none));
        package2Backgrounds.add(new BackgroundDiary("image_12", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_13", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_14", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_15", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_16", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_17", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_18", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_19", R.drawable.p41_bg_theme));
        package2Backgrounds.add(new BackgroundDiary("image_20", R.drawable.p41_bg_theme));
        packages.add(new PackageBackgroundDiary("Package 2", package2Backgrounds));

        // Initialize data for BackgroundDiary
        backgrounds.addAll(package1Backgrounds);
        backgrounds.addAll(package2Backgrounds);
    }

    private PackageBackgroundDiaryControl() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the list of packages.
     *
     * @return the list of packages
     */
    public static ArrayList<PackageBackgroundDiary> getPackages() {
        return packages;
    }

    public static PackageBackgroundDiary getPackage(int position){
        return packages.get(position);
    }

    /**
     * Returns the list of backgrounds.
     *
     * @return the list of backgrounds
     */
    public static ArrayList<BackgroundDiary> getBackgrounds() {
        return backgrounds;
    }
}