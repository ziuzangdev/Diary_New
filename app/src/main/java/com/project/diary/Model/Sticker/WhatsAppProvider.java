package com.project.diary.Model.Sticker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.aghajari.emojiview.sticker.StickerLoader;
import com.aghajari.emojiview.sticker.StickerProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class WhatsAppProvider implements StickerProvider {
    private Context context;

    public WhatsAppProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StickerCategory<?>[] getCategories() {
        return new StickerCategory[]{
                new WhatsAppStickers(context)
        };
    }

    @NonNull
    @Override
    public StickerLoader getLoader() {
        return new StickerLoader() {
            @Override
            public void onLoadSticker(View view, Sticker<?> sticker) {
                if (sticker.isInstance(String.class)) {
                    System.out.println(sticker.getData());
                    Glide.with(view).load(String.valueOf(sticker.getData())).into((AppCompatImageView)view);
                }

            }

            @Override
            public void onLoadStickerCategory(View view, StickerCategory<?> stickerCategory, boolean selected) {
                try{
                    if (stickerCategory instanceof ShopStickers) {
                        Drawable dr0 = AppCompatResources.getDrawable(view.getContext(), (Integer) stickerCategory.getCategoryData());
                        Drawable dr = dr0.getConstantState().newDrawable();
                        if (selected) {
                            DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getSelectedColor());
                        }else{
                            DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getStickerViewTheme().getDefaultColor());
                        }
                        ((AppCompatImageView)view).setImageDrawable(dr);
                    } else {
                        Glide.with(view).load(Integer.valueOf(stickerCategory.getCategoryData().toString())).apply(RequestOptions.fitCenterTransform()).into((AppCompatImageView)view);
                    }
                }catch (Exception ignore){
                }
            }
        };
    }

    @Override
    public boolean isRecentEnabled() {
        return true;
    }
}