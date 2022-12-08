package com.project.diary.Model.FileProvider;

import androidx.core.content.FileProvider;

public class MyFileProvider extends FileProvider {
    public MyFileProvider() {
    }

    public MyFileProvider(int resourceId) {
        super(resourceId);
    }
}
