package com.project.diary.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.diary.Control.Activity.CalandarActivityControl;
import com.project.diary.Control.Activity.ExportImportActivityControl;
import com.project.diary.Model.Calendar.MyMaterialCalendarView;
import com.project.diary.R;
import com.project.diary.databinding.ActivityCalandarBinding;
import com.project.diary.databinding.ActivityExportImportBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.List;

public class ExportImportActivity extends AppCompatActivity implements PermissionListener {
    private ActivityExportImportBinding binding;

    private ExportImportActivityControl control;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExportImportBinding.inflate(getLayoutInflater());
        control = new ExportImportActivityControl(ExportImportActivity.this, binding);
        control.showCustomUI();
        setContentView(binding.getRoot());
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
        binding.llExportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        control.exportToPdf();
                    }
                });
                thread.start();
            }
        });
        binding.cvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.showMyMaterialCalendarView(v);
            }
        });

        binding.cvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.showMyMaterialCalendarView(v);
            }
        });
    }

    private void addControls() {
        control.initMyMaterialCalendarView();
        binding.tiledProgressView.setLoadingColor(Color.parseColor("#03A9F4"));
    }


    @Override
    public void onPermissionGranted() {
        addControls();
        addEvents();
    }

    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {

    }
}