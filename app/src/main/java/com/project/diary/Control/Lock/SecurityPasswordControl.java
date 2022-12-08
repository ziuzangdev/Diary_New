package com.project.diary.Control.Lock;

import android.content.Context;

import com.project.diary.Control.RootControl;
import com.project.diary.Model.SQLite.SQLite;

public class SecurityPasswordControl extends RootControl {
    private SQLite sqLite;

    public SQLite getSqLite() {
        return sqLite;
    }

    public void setSqLite(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    public SecurityPasswordControl(Context context) {
        super(context);
        sqLite = new SQLite(getContext());
    }

}
