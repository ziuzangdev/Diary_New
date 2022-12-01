package com.project.diary.Model.Video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class Video {
    private String path;
    private Bitmap thumb;

    public Video(String path) {
        this.path = path;
        this.thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(this.path), 250, 250);
    }

    public String getPath() {
        return path;
    }

    public Bitmap getThumb() {
        return thumb;
    }
}
