package com.project.diary.Model.Sticker;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.project.diary.R;

public class WhatsAppStickers implements StickerCategory<Integer> {
    private  Context context;
    public WhatsAppStickers(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Sticker<?>[] getStickers() {
        return new Sticker[]{
                new Sticker<>(context.getResources().getString(R.string.sticker_01))
        };
    }

    @Override
    public Integer getCategoryData() {
        return R.drawable.sticker;
    }

    @Override
    public boolean useCustomView() {
        return false;
    }

    @Override
    public View getView(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view) {}

    @Override
    public View getEmptyView(ViewGroup viewGroup) {
        return null;
    }
}
