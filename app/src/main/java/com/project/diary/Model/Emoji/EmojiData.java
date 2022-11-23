package com.project.diary.Model.Emoji;

import android.content.Context;

import com.aghajari.emojiview.facebookprovider.AXFacebookEmojiData;
import com.aghajari.emojiview.facebookprovider.AXFacebookEmojiProvider;

public class EmojiData {

    private String[][] data;

    private Context context;

    protected EmojiData(Context context) {
        this.context = context;
        data = new AXFacebookEmojiProvider(context).getEmojiData().getData();
    }


    protected String getData(int packageEmoji, int positionEmoji){
        try{
            return data[packageEmoji][positionEmoji];
        }catch (Exception e){
            return "Null";
        }

    }

}
