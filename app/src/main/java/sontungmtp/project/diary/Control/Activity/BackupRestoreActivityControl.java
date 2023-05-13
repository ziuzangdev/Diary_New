package sontungmtp.project.diary.Control.Activity;

import android.Manifest;
import android.content.Context;

import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Model.Diary.Diary;
import sontungmtp.project.diary.Model.Diary.RootDiary;
import sontungmtp.project.diary.Model.SQLite.SQLite;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;
import sontungmtp.project.diary.databinding.ActivityBackupRestoreBinding;

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

/**
 * The BackupRestoreActivityControl class is a subclass of RootControl that manages the display and functionality of a backup and restore feature in an application.
 *
 * It has properties for an instance of the AppThemeManager class and an instance of the SQLite class. It also has a constructor that takes in a Context object and an ActivityBackupRestoreBinding object and initializes the appThemeManager and sqLite instances. It has methods for backing up and restoring diary entries, generating a note on the SD card, and displaying a toast message.
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 *
 */
public class BackupRestoreActivityControl extends RootControl {
    private ActivityBackupRestoreBinding binding;

    private AppThemeManager appThemeManager;

    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    private SQLite sqLite;

    /**
     * Constructs a new BackupRestoreActivityControl object.
     *
     * @param context The Context object to be used for initialization
     * @param binding The ActivityBackupRestoreBinding object to be used for data binding
     */
    public BackupRestoreActivityControl(Context context, ActivityBackupRestoreBinding binding) {
        super(context);
        this.binding = binding;
        sqLite = new SQLite(getContext());
        appThemeManager = new AppThemeManager(context);
    }

    /**
     * Back up the diary entries using a PermissionListener object to request permissions and save the data to the SD card.
     */
    public void backupDiaries() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String currentDatabase = "/data/data/sontungmtp.project.diary/databases/Diary.db";
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


    /**
     * Restores the diary entries from the file at the provided path. Displays a toast message indicating the success or failure of the operation.
     *
     * @param path The file path to the backup data
     */
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

    /**
     * Generates a note with the provided file name and body on the SD card.
     *
     * @param context The Context object to be used for file operations
     * @param sFileName The file name for the note
     * @param sBody The body text for the note
     */
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
