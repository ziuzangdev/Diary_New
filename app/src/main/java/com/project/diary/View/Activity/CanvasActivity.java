package com.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.PathParser;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Control.Activity.CanvasActivityControl;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.R;
import com.project.diary.databinding.ActivityCanvasBinding;
import com.project.diary.databinding.ActivityDiaryBinding;

import java.util.List;

public class CanvasActivity extends AppCompatActivity implements IThemeManager {
    private ActivityCanvasBinding binding;

    private CanvasActivityControl control;
    private RcvColorPickerAdapter rcvColorPickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        binding = ActivityCanvasBinding.inflate(getLayoutInflater());
        control = new CanvasActivityControl(CanvasActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.cvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = null;
                        path = binding.vDrawView.saveDraw();
                        while (path == null){}
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("pathDraw", path);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                thread.start();
            }
        });
        binding.rtgGroupPaintTool.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbtnPaintTool){
                    binding.vDrawView.onClickEraser(false);
                }else if(checkedId == R.id.rbtnEraser){
                    binding.vDrawView.onClickEraser(true);
                }
            }
        });

        binding.imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vDrawView.onClickUndo();
            }
        });

        binding.imgRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vDrawView.onClickRedo();
            }
        });
        binding.sbStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.txtStrokeWidth.setText(String.valueOf(progress));
                binding.vDrawView.getDrawPaint().setStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void addControls() {
        initTheme();
        initColorPicker();
    }

    private void initColorPicker() {
        rcvColorPickerAdapter = new RcvColorPickerAdapter(binding.vDrawView);
        binding.rcvColorPicker.setHasFixedSize(true);
        binding.rcvColorPicker.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.rcvColorPicker.setAdapter(rcvColorPickerAdapter);
    }

    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
        binding.llNext.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
        binding.llSeekbar.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
        appThemeManager.setStateButtonForRatioButtonCanvas(binding);
    }
}