package com.project.diary.Model.Audio;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AudioRecorder {
    final MediaRecorder recorder = new MediaRecorder();
    public String path;
    private Context context;

    public AudioRecorder(String path, Context context) {
        this.context = context;
        this.path = sanitizePath(path);
    }

    public Context getContext() {
        return context;
    }

    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += String.valueOf(System.currentTimeMillis()) + ".3GPP";
        }
        return getContext().getExternalCacheDir().getAbsolutePath()
                + path;
    }

    @WorkerThread
    public void start() throws IOException {
        requestPermission();
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state
                    + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity)getContext()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){}
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity)getContext()), new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }
        while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){}
    }

    @WorkerThread
    public void stop() throws IOException {
        recorder.pause();
        Log.i("AudioRecorder: ", "Completed Record: " + path);
    }

    @WorkerThread
    public void saveRecorder(){
        recorder.stop();
        recorder.release();
    }

    @WorkerThread
    public void playarcoding(String path) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(path);
        mp.prepare();
        mp.start();
        mp.setVolume(10, 10);
    }
}
