package com.project.diary.Model.Sticker;

import com.aghajari.emojiview.sticker.Sticker;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ShopSticker extends Sticker {
    private static final long serialVersionUID = 3L;

    String title;
    int count;

    public ShopSticker(Sticker[] data, String Title, int StickersSize) {
        super(data);
        this.title = Title;
        this.count = StickersSize;
    }
}
