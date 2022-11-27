package com.project.diary.Control.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;

import com.project.diary.Control.RootControl;

import java.util.ArrayList;

public class MediaActivityControl extends RootControl {

    private ArrayList<String> arrPathImage;

    private ArrayList<String> arrPathVideo;

    private boolean isGenerateImagePath;

    private boolean isGenerateVideoPath;
    private static final int PICK_FROM_GALLERY = 0;

    public MediaActivityControl(Context context) {
        super(context);
        arrPathImage = new ArrayList<>();
        arrPathVideo = new ArrayList<>();
        isGenerateImagePath = false;
        isGenerateVideoPath = false;
    }

    public boolean isGenerateImagePath() {
        return isGenerateImagePath;
    }

    public void setGenerateImagePath(boolean generateImagePath) {
        isGenerateImagePath = generateImagePath;
    }

    public boolean isGenerateVideoPath() {
        return isGenerateVideoPath;
    }

    public void setGenerateVideoPath(boolean generateVideoPath) {
        isGenerateVideoPath = generateVideoPath;
    }

    public ArrayList<String> getArrPathImage() {
        if(arrPathImage == null){
            arrPathImage = new ArrayList<>();
        }
        return arrPathImage;
    }

    public ArrayList<String> getArrPathVideo() {
        if(arrPathVideo == null){
            arrPathVideo = new ArrayList<>();
        }
        return arrPathVideo;
    }

    /**
     * You can get live data while this Thread are working by using method {@link MediaActivityControl#getArrPathImage()}
     * @return {@link MediaActivityControl#isGenerateImagePath()} will be "false" if Thread completed reading, otherwise it will be "true"
     */
    @WorkerThread
    public void generateAllPathImage(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((Activity)getContext()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){}
                boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                if(isSDPresent){
                    isGenerateImagePath = true;
                    final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
                    final String orderBy = MediaStore.Images.Media._ID;
                    Cursor cursor = getContext().getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);
                    int count = cursor.getCount();
                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        arrPathImage.add(cursor.getString(dataColumnIndex));
                        Log.i("PATH IMAGE: ", cursor.getString(dataColumnIndex));
                    }
                    cursor.close();
                    isGenerateImagePath = false;
                }
            }
        });
        thread.start();
    }

    /**
     * You can get live data while this Thread are working by using method {@link MediaActivityControl#getArrPathVideo()}
     * @return {@link MediaActivityControl#isGenerateVideoPath()} will be "false" if Thread completed reading, otherwise it will be "true"
     */
    @WorkerThread
    public void generateAllPathVideo(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((Activity)getContext()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){}
                boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                if(isSDPresent){
                    isGenerateVideoPath = true;
                    final String[] columns = { MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID };
                    final String orderBy = MediaStore.Video.Media._ID;
                    Cursor cursor = getContext().getContentResolver().query(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);
                    int count = cursor.getCount();
                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                        arrPathVideo.add(cursor.getString(dataColumnIndex));
                        Log.i("PATH VIDEO: ", cursor.getString(dataColumnIndex));
                    }
                    cursor.close();
                    isGenerateVideoPath = false;
                }
            }
        });
        thread.start();

    }
}
