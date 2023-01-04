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

/**
 * The MediaActivityControl class extends the RootControl class and is responsible for managing the data and functionality related to media in the MediaActivity.
 * It provides access to lists of paths for images and videos, and methods for generating these lists.
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class MediaActivityControl extends RootControl {
    private ArrayList<String> arrPathImage;

    private ArrayList<String> arrPathVideo;

    private boolean isGenerateImagePath;

    private boolean isGenerateVideoPath;
    private static final int PICK_FROM_GALLERY = 0;

    /**
     * Constructs a new MediaActivityControl object and initializes the arrPathImage and arrPathVideo fields.
     *
     * @param context a Context object used to access application-specific resources
     */
    public MediaActivityControl(Context context) {
        super(context);
        arrPathImage = new ArrayList<>();
        arrPathVideo = new ArrayList<>();
        isGenerateImagePath = false;
        isGenerateVideoPath = false;
    }

    /**
     * Returns the status of the image path generation process.
     *
     * @return true if the process is currently running, false otherwise
     */
    public boolean isGenerateImagePath() {
        return isGenerateImagePath;
    }

    /**
     * Sets the status of the image path generation process.
     *
     * @param generateImagePath true to set the process as running, false otherwise
     */
    public void setGenerateImagePath(boolean generateImagePath) {
        isGenerateImagePath = generateImagePath;
    }

    /**
     * Returns the status of the video path generation process.
     *
     * @return true if the process is currently running, false otherwise
     */
    public boolean isGenerateVideoPath() {
        return isGenerateVideoPath;
    }
    /**
     * Sets the status of the video path generation process.
     *
     * @param generateVideoPath true to set the process as running, false otherwise
     */
    public void setGenerateVideoPath(boolean generateVideoPath) {
        isGenerateVideoPath = generateVideoPath;
    }

    /**
     * Returns the list of image paths.
     *
     * @return an ArrayList of strings containing the paths to images
     */
    public ArrayList<String> getArrPathImage() {
        if(arrPathImage == null){
            arrPathImage = new ArrayList<>();
        }
        return arrPathImage;
    }

    /**
     * Returns the list of video paths.
     *
     * @return an ArrayList of strings containing the paths to videos
     */
    public ArrayList<String> getArrPathVideo() {
        if(arrPathVideo == null){
            arrPathVideo = new ArrayList<>();
        }
        return arrPathVideo;
    }

    /**
     * Generates a list of all image paths in the device's external storage and updates the arrPathImage field with the list.
     * This method should be called on a worker thread.
     * You can get live data while this Thread are working by using method {@link MediaActivityControl#getArrPathImage()}
     *
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
     * Generates a list of all video paths in the device's external storage and updates the arrPathVideo field with the list.
     * This method should be called on a worker thread.
     * You can get live data while this Thread are working by using method {@link MediaActivityControl#getArrPathVideo()}
     *
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
