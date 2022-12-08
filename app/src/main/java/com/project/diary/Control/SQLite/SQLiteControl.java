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
import com.project.diary.Model.Diary.DiaryData;
import com.project.diary.Model.Lock.ISecurityPassword;
import com.project.diary.Model.Lock.SecurityPassword;
import com.project.diary.Model.Lock.SecurityPasswordGson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class include all logic of task "Interact with database" <br>
 * If you chance logic related to database, you must do it in this class
 */
public class SQLiteControl{
    private static final String DATABASE_NAME = "Diary.db";

    private static final String DB_PATH_SUFFIX = "/databases/";

    /**
     * This variable is the latest version of database that the app has <br>
     * It must have the same value with "Version" in table "Detail" that database file in assets folder has.
     */
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database = null;

    private Context context;

    public SQLiteControl(@Nullable Context context) {
        this.context = context;
        processCopy();
    }


    /**
     * Call this method to start process copy database in assets folder into user device
     */
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

    /**
     * This method will call {@link  SQLiteControl#checkVersionDatabase()} to check {@link SQLiteControl#DATABASE_VERSION} with "Version" in table "Detail" that database file in assets folder has <br>
     * If result not are the same, it will call {@link SQLiteControl#overrideDatabase()}
     */
    private void checkVersionDatabase(){
        if (!isCurrentVersion()) {
            overrideDatabase();
        }
    }

    /**
     * This method will overwrite the newer version database over the old version
     */
    private void overrideDatabase() {
        //Do something
    }


    /**
     * Check {@link SQLiteControl#DATABASE_VERSION} with "Version" in table "Detail" that database file in assets folder has
     * @return true if they are the same <br>
     * false if they not are the same
     */
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


    /**
     * Insert new data into new row in database
     * @param diary an object data need insert into database
     * @return true if insert completed <br>
     * false if not complete
     */
    public int insertData(Diary diary){
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        contentValues.put("data", gson.toJson(diary));
        return (int) database.insert("Diary", null, contentValues);
    }

    /**
     * Remove a row of data in the database
     * @param TABLE_NAME Table has row data need remove
     * @param Key An Key of Data need remove (Key is {@link Diary#getId()})
     */
    public void removeData(String TABLE_NAME, String Key) {
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        database.delete(TABLE_NAME, "id=?", new String[]{Key});
    }

    /**
     * Remove multiple rows of data in the database
     * @param TABLE_NAME Table has row data need remove
     * @param Keys Array of Key of Data need remove (Key is {@link Diary#getId()})
     */
    public void removeData(String TABLE_NAME, String[] Keys) {
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        database.delete(TABLE_NAME, "id=?", Keys);
    }

    /**
     * Overwrite a row over an existing row in the database
     * @param diary Object contains all new data
     * @param TABLE_NAME Table has row data need overwrite
     */
    public void updateData(Diary diary, String TABLE_NAME){
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        contentValues.put("data", gson.toJson(diary));
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        database.update(TABLE_NAME, contentValues, "id=?", new String[]{diary.getId()});
    }

    /**
     * Read singly row in database
     * @param TABLE_NAME Table has row data need read
     * @param Key An Key of Data need read (Key is {@link Diary#getId()})
     * @return New Object has data you need
     */
    public Diary readData(String TABLE_NAME, String Key){
        Diary diary = null;
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query(TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            if (cursor.getString(0).equals(Key)){
                diary = new Gson().fromJson(cursor.getString(1), Diary.class);
                diary.setId(cursor.getString(0));
                break;
            }
        }
        cursor.close();
        return diary;
    }

    public ArrayList<Diary> readData(String TABLE_NAME){
        ArrayList<Diary> diaries = new ArrayList<>();
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query(TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Diary diary = new Gson().fromJson(cursor.getString(1), Diary.class);
            diary.setId(cursor.getString(0));
            diaries.add(diary);
        }
        cursor.close();
        return  diaries;
    }
    public ArrayList<Diary> readData(String TABLE_NAME, String[] Keys){
        ArrayList<Diary> diaries = new ArrayList<>();
        for(String Key : Keys){
            diaries.add(readData(TABLE_NAME, Key));
        }
        return  diaries;
    }

    public boolean isLockMode(){
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("Detail",null,null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getInt(1) == 0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }
    }

    public SecurityPassword getSecurityPassword(){
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("Detail",null,null,null,null,null,null);
        cursor.moveToFirst();
        SecurityPasswordGson securityPasswordGson = new Gson().fromJson(cursor.getString(3), SecurityPasswordGson.class);
        if(securityPasswordGson != null){
            SecurityPassword securityPassword = new SecurityPassword();
            securityPassword.initQuestionAndPassword(securityPasswordGson.getQUESTION(), securityPasswordGson.getANSWER());
            return securityPassword;
        }else{
            return null;
        }

    }

    public void updateLockMode(int lockMode){
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        contentValues.put("isPassword", lockMode);
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        database.update("Detail", contentValues, "key=?", new String[]{"trikay"});
    }

    public void updateSecurityPassword(ISecurityPassword securityPassword){
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        SecurityPasswordGson securityPasswordGson = new SecurityPasswordGson(securityPassword.getQUESTION(), securityPassword.getANSWER());
        contentValues.put("securityPassword", gson.toJson(securityPasswordGson));
        database = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        database.update("Detail", contentValues, "key=?", new String[]{"trikay"});
    }


}
