package com.project.diary.Model.Emoji;

import android.content.Context;

public class Emoji {
    private EmojiData emojiData;

    private String emojiText;
    private Context context;

    public Emoji(Context context) {
        this.context = context;
        emojiData = new EmojiData(context);
    }

    public void initEmoji(int category, int position){
        this.emojiText = emojiData.getData(category, position);
    }

    public String getEmojiText() {
        return emojiText;
    }

    public void setEmojiText(String emojiText) {
        this.emojiText = emojiText;
    }
}
