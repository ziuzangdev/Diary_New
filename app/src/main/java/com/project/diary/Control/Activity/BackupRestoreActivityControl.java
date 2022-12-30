package com.project.diary.Control.Activity;

import static com.project.diary.Control.SQLite.SQLiteControl.DB_PATH_SUFFIX;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.diary.Control.RootControl;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Diary.RootDiary;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.databinding.ActivityBackupRestoreBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class BackupRestoreActivityControl extends RootControl {
    private ActivityBackupRestoreBinding binding;

    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    private SQLite sqLite;
    public BackupRestoreActivityControl(Context context, ActivityBackupRestoreBinding binding) {
        super(context);
        this.binding = binding;
        sqLite = new SQLite(getContext());
        appThemeManager = new AppThemeManager(context);
    }

    public void backupDiaries() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String currentDatabase = "/data/data/com.project.diary/databases/Diary.db";
                String tagretDatabase = "/storage/emulated/0/Documents";
                ArrayList<Diary> diaries = sqLite.getSqLiteControl().readData("Diary");
                RootDiary rootDiary = new RootDiary();
                rootDiary.getDiaries().addAll(diaries);
                String rootDiaries = new Gson().toJson(rootDiary);
                generateNoteOnSD(getContext(), "BackupDiary", rootDiaries);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                for(String s : deniedPermissions){
                    System.out.println(s);
                }
            }
        };
            TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();


    }


    public void restoreDiaries(String path) {
        File file = new File(path);
        try{
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
            catch (IOException e) {
            }
            RootDiary rootDiary = new Gson().fromJson(text.toString(), RootDiary.class);
            ArrayList<Diary> diaries = sqLite.getSqLiteControl().readData("Diary");
            for(Diary diary : diaries){
                sqLite.getSqLiteControl().removeData("Diary", diary.getId());
            }
            for(Diary diary : rootDiary.getDiaries()){
                sqLite.getSqLiteControl().insertData(diary);
            }
            Toast.makeText(getContext(), "Restore completed by using backup file at: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(), "Restore can't completed because you using file: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }

    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            int index = 0;
            File root = new File("/storage/emulated/0/Documents/", "BackupDiaries");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            while (gpxfile.exists()){
                index++;
                gpxfile = new File(root, sFileName + "(" +String.valueOf(index) + ")");
            }
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Backup completed at " + gpxfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
