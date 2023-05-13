package sontungmtp.project.diary.Model.SQLite;

import android.content.Context;

import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Control.SQLite.SQLiteControl;

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
