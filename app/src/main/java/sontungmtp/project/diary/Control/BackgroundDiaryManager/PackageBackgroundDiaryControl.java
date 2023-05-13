package sontungmtp.project.diary.Control.BackgroundDiaryManager;

import sontungmtp.project.diary.Model.BackgroundDiaryManager.BackgroundDiary;
import sontungmtp.project.diary.Model.BackgroundDiaryManager.PackageBackgroundDiary;
import sontungmtp.project.diary.R;

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
        ArrayList<BackgroundDiary> package2Backgrounds = new ArrayList<>();
        package2Backgrounds.add(new BackgroundDiary("image_11", R.drawable.theme_none));
        package2Backgrounds.add(new BackgroundDiary("image_12", R.drawable.bg_cartoon_1_optimized));
        package2Backgrounds.add(new BackgroundDiary("image_13", R.drawable.bg_cartoon_2_optimized));
        package2Backgrounds.add(new BackgroundDiary("image_14", R.drawable.bg_cartoon_3_optimized));
        package2Backgrounds.add(new BackgroundDiary("image_15", R.drawable.bg_cartoon_4_optimized));
        package2Backgrounds.add(new BackgroundDiary("image_16", R.drawable.bg_cartoon_5_optimized));
        package2Backgrounds.add(new BackgroundDiary("image_17", R.drawable.bg_cartoon_6_optimized));
        package2Backgrounds.add(new BackgroundDiary("image_18", R.drawable.bg_cartoon_7_optimized));
        packages.add(new PackageBackgroundDiary("Cartoon", package2Backgrounds));

        ArrayList<BackgroundDiary> package1Backgrounds = new ArrayList<>();
        package1Backgrounds.add(new BackgroundDiary("image_1", R.drawable.theme_none));
        package1Backgrounds.add(new BackgroundDiary("image_3", R.drawable.bg_abstract_2_optimized));
        package1Backgrounds.add(new BackgroundDiary("image_5", R.drawable.bg_abstract_3_optimized));
        package1Backgrounds.add(new BackgroundDiary("image_8", R.drawable.bg_abstract_6_optimized));
        packages.add(new PackageBackgroundDiary("Abstract", package1Backgrounds));

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