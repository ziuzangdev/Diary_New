package com.project.diary.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.diary.Control.Activity.BackupRestoreActivityControl;
import com.project.diary.Control.Activity.CalandarActivityControl;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Ultil.RealPathUtils;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.R;
import com.project.diary.databinding.ActivityBackupRestoreBinding;
import com.project.diary.databinding.ActivityCalandarBinding;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import me.rosuh.filepicker.config.FilePickerManager;

public class BackupRestoreActivity extends AppCompatActivity implements PermissionListener, IThemeManager {
    private ActivityBackupRestoreBinding binding;
    private BackupRestoreActivityControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackupRestoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        control = new BackupRestoreActivityControl(BackupRestoreActivity.this, binding);
        control.showCustomUI();
            TedPermission.create()
                    .setPermissionListener(this)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
    }

    private void addEvents() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.llBackupDiaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.backupDiaries();
            }
        });

        binding.llRestoreDiaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerManager
                        .from(BackupRestoreActivity.this)
                        .forResult(FilePickerManager.REQUEST_CODE);
            }
        });
    }

    private void addControls() {
        initTheme();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FilePickerManager.REQUEST_CODE){
            if(resultCode == RESULT_OK){
                ArrayList<String> list = (ArrayList<String>) FilePickerManager.obtainData();
                for(String s : list){
                    control.restoreDiaries(s);
                }
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        addControls();
        addEvents();
    }

    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {

    }

    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
    }
}