package com.project.diary.Control.Activity;

import android.content.ContentValues;
import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Emoji.Emoji;

import java.util.ArrayList;

public class ActivityDiaryControl extends RootControl {

    ArrayList<Emoji> emojis;

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
        int i=0;
        int j=0;
        boolean isRunning = true;
        while (isRunning){
            com.project.diary.Model.Emoji.Emoji emoji = new Emoji(getContext());
            emoji.initEmoji(i, j);
            if(emoji.getEmojiText().equals(NO_DATA_STATE)){
                i++;
                emoji.initEmoji(i, j);
                if(emoji.getEmojiText().equals(NO_DATA_STATE)){
                    isRunning = false;
                }
            }else{
                j++;
                emojis.add(emoji);
            }
        }

    }

    public String initStatus() {
        return emojis.get(0).getEmojiText();
    }

}