package sontungmtp.project.diary.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import sontungmtp.project.diary.Control.Activity.BackupRestoreActivityControl;
import sontungmtp.project.diary.Control.Activity.IThemeManager;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;

import java.util.ArrayList;
import java.util.List;

import me.rosuh.filepicker.config.FilePickerManager;

public class BackupRestoreActivity extends AppCompatActivity implements PermissionListener, IThemeManager {
    private sontungmtp.project.diary.databinding.ActivityBackupRestoreBinding binding;
    private BackupRestoreActivityControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = sontungmtp.project.diary.databinding.ActivityBackupRestoreBinding.inflate(getLayoutInflater());
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