package com.project.diary.Model.SQLite;

import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Control.SQLite.SQLiteControl;

public class SQLite extends RootControl {
    private SQLiteControl sqLiteControl;
    public SQLite(Context context) {
        super(context);
        sqLiteControl = new SQLiteControl(context);
    }

    public SQLiteControl getSqLiteControl() {
        return sqLiteControl;
    }
}
