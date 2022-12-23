package com.project.diary.Control.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Emoji.Emoji;
import com.project.diary.Model.ThemeManager.AppThemeManager;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    public String[] getEmojiText() {
        return emojiText;
    }

    public static final String NO_DATA_STATE = "Null";
    public ActivityDiaryControl(Context context) {
        super(context);
        createEmojis();
        appThemeManager = new AppThemeManager(context);
    }

    public ArrayList<Emoji> getEmojis() {
        return emojis;
    }

    public Emoji getEmoji(int position){
        return emojis.get(position);
    }

    public void setEmojis(ArrayList<Emoji> emojis) {
        this.emojis = emojis;
    }

    private void createEmojis(){
        emojis = new ArrayList<>();
        for(String emojiTxt : emojiText){
            Emoji emoji = new Emoji();
            emoji.setEmojiText(emojiTxt);
            emojis.add(emoji);
        }

    }
    public ArrayList<String> pullLinks(String html) {
        ArrayList links = new ArrayList();
        Elements srcs = Jsoup.parse(html).select("[src]"); //get All tags containing "src"
        for (int i = 0; i < srcs.size(); i++) {
            links.add(srcs.get(i).attr("abs:src").toString()); // get links of selected tags
        }
        return links;
    }
    public String initStatus(){
        return emojis.get(0).getEmojiText();
    }

    public List<String> getImgTags(String html) {
        List<String> imgTags = new ArrayList<>();
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(html);
        while (m.find()) {
            imgTags.add(m.group());
        }
        return imgTags;
    }
    public boolean isPath(String path) {
        File file = new File(path);
        return file.exists();
    }
    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
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