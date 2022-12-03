package com.project.diary.Control.Activity;

import android.content.ContentValues;
import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Emoji.Emoji;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ActivityDiaryControl extends RootControl {

    ArrayList<Emoji> emojis;
    String[] emojiText = new String[]{"\ud83d\ude00", "\ud83d\ude03", "\ud83d\ude04", "\ud83d\ude01", "\ud83d\ude06", "\ud83d\ude05", "\ud83d\ude02", "\ud83e\udd23", "\ud83e\udd72", "☺", "\ud83d\ude0a", "\ud83d\ude07", "\ud83d\ude42", "\ud83d\ude43", "\ud83d\ude09", "\ud83d\ude0c", "\ud83d\ude0d", "\ud83e\udd70", "\ud83d\ude18", "\ud83d\ude17", "\ud83d\ude19", "\ud83d\ude1a", "\ud83d\ude0b", "\ud83d\ude1b", "\ud83d\ude1d", "\ud83d\ude1c", "\ud83e\udd2a", "\ud83e\udd28", "\ud83e\uddd0", "\ud83e\udd13", "\ud83d\ude0e", "\ud83e\udd78", "\ud83e\udd29", "\ud83e\udd73", "\ud83d\ude0f", "\ud83d\ude12", "\ud83d\ude1e", "\ud83d\ude14", "\ud83d\ude1f", "\ud83d\ude15", "\ud83d\ude41", "☹"};

    public static final String NO_DATA_STATE = "Null";
    public ActivityDiaryControl(Context context) {
        super(context);
        createEmojis();
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

}