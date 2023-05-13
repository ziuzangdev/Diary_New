package sontungmtp.project.diary.Control.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Model.Emoji.Emoji;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ActivityDiaryControl class is a subclass of RootControl that manages the display and functionality of an activity diary feature in an application.
 *<br>
 * It has a list of Emoji objects, which can be accessed through the emojis property, and a list of emoji characters represented in Unicode, which can be accessed through the emojiText property. It also has an instance of the AppThemeManager class, which can be accessed through the appThemeManager property.
 *<br>
 * The class has a constructor that takes in a Context object and initializes the emojis list and appThemeManager instance. It also has methods for creating the emojis list, getting and setting the emojis list, getting an emoji at a specific position in the list, returning the first emoji in the list as the default status, extracting image tags from an HTML string, checking if a file path exists, converting a Bitmap to a base64 encoded string, converting a Drawable to a Bitmap, extracting text from the src attribute of img tags in an HTML string, and getting the appThemeManager instance and the emojiText list.
 *<br>
 * @author [TrikayDev]
 * <br>
 * @since [12/30/2022]
 */
public class ActivityDiaryControl extends RootControl {

    private ArrayList<Emoji> emojis;
    private String[] emojiText = new String[]{
            "\uD83D\uDE06",
            "\uD83D\uDE05",
            "\uD83D\uDE02",
            "\uD83E\uDD70",
            "\uD83E\uDD2A",
            "\uD83E\uDD14",
            "\uD83D\uDE34",
            "\uD83E\uDD15",
            "\uD83D\uDE24",
            "\uD83D\uDE21"
          };

    public static final String NO_DATA_STATE = "Null";
    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    public String[] getEmojiText() {
        return emojiText;
    }

    /**
     * Constructs a new ActivityDiaryControl object.
     *
     * @param context The Context object to be used for initialization
     */
    public ActivityDiaryControl(Context context) {
        super(context);
        createEmojis();
        appThemeManager = new AppThemeManager(context);
    }

    /**
     * Returns the emojis list.
     *
     * @return ArrayList<Emoji> The emojis list
     */
    public ArrayList<Emoji> getEmojis() {
        return emojis;
    }

    public Emoji getEmoji(int position){
        return emojis.get(position);
    }

    public void setEmojis(ArrayList<Emoji> emojis) {
        this.emojis = emojis;
    }

    /**
     * Populates the emojis list with Emoji objects, using the emojiText string array.
     */
    private void createEmojis(){
        emojis = new ArrayList<>();
        for(String emojiTxt : emojiText){
            Emoji emoji = new Emoji();
            emoji.setEmojiText(emojiTxt);
            emojis.add(emoji);
        }

    }
    /**
     Returns the first emoji character in the emojis list as the default status.
     @return String The first emoji character in the emojis list
     */
    public String initStatus(){
        return emojis.get(0).getEmojiText();
    }

/**
 Returns a list of image tags in the provided html string.
 @param html The html string to extract image tags from
 @return List<String> The list of image tags
 */
    public List<String> getImgTags(String html) {
        List<String> imgTags = new ArrayList<>();
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(html);
        while (m.find()) {
            imgTags.add(m.group());
        }
        return imgTags;
    }
    /**
     Returns a boolean indicating whether the provided file path exists.
     @param path The file path to check
     @return boolean True if the file path exists, false otherwise
     */
    public boolean isPath(String path) {
        File file = new File(path);
        return file.exists();
    }
    /**
     Returns a base64 encoded string representation of the provided Bitmap.
     @param bitmap The Bitmap to be encoded
     @return String The base64 encoded string
     */
    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    /**
     Returns a Bitmap object from the provided Drawable.
     @param drawable The Drawable to be converted
     @return Bitmap The resulting Bitmap object
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
/**
 Returns a string of all text found in the src attribute of img tags in the provided html string.
 @param html The html string to extract text from
 @return String The extracted text
 */
    public String readAllTextFromImgSrc(String html){
        String imageSrc = "";
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher matcher = pattern.matcher(html);
        if(matcher.find()){
            imageSrc = matcher.group(1);
        }
        return imageSrc;
    }
}