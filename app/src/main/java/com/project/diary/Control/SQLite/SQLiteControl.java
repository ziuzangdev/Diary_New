package com.project.diary.Control.SQLite;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.project.diary.Model.Diary.Diary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class SQLiteControl{
    private static final String DATABASE_NAME = "Diary.db";

    private static final String DB_PATH_SUFFIX = "/databases/";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database = null;

    private Context context;

    public SQLiteControl(@Nullable Context context) {
        this.context = context;
        processCopy();
    }


    public void processCopy() {
        //private app
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                checkVersionDatabase();
            }
            catch (Exception e)
            {
            }
        }else{
            checkVersionDatabase();
        }
    }

    private void checkVersionDatabase(){
        if (!isCurrentVersion()) {
            overrideDatabase();
        }
    }
    private void overrideDatabase() {
        //Do something
    }



    private boolean isCurrentVersion() {
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = database.query("Detail",null,null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getInt(0) == DATABASE_VERSION){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = context.getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            // if the path doesn't exist first, create it
            File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean insertDataToDatabase(Diary diary){
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        contentValues.put("tittle", diary.getTittle());
        contentValues.put("status", diary.getStatus());
        contentValues.put("date", diary.getDate());
        contentValues.put("diaryData", gson.toJson(diary.getDiaryData()));
        if(database.insert("Diary", null, contentValues) == -1){
            return false;
        }else{
            return true;
        }
    }
}
