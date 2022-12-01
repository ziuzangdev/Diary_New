package com.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import com.project.diary.R;
import com.project.diary.databinding.ActivityCanvasBinding;
import com.project.diary.databinding.ActivityDiaryBinding;

public class CanvasActivity extends AppCompatActivity {
    private ActivityCanvasBinding binding;
    private RcvColorPickerAdapter rcvColorPickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        binding = ActivityCanvasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.rtgGroupPaintTool.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbtnPaintTool){
                    binding.vDrawView.enableEraserMode(false);
                }else if(checkedId == R.id.rbtnEraser){
                    binding.vDrawView.enableEraserMode(true);
                }
            }
        });

        binding.imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vDrawView.undo();
            }
        });

        binding.imgRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vDrawView.redo();
            }
        });
        binding.sbStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.txtStrokeWidth.setText(String.valueOf(progress));
                binding.vDrawView.setStrokeWidth(progress);
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
        ViewTreeObserver vto = binding.vDrawView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.vDrawView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = binding.vDrawView.getMeasuredWidth();
                int height = binding.vDrawView.getMeasuredHeight();
                binding.vDrawView.init(height, width);
                binding.vDrawView.setStrokeWidth(15);
                binding.sbStrokeWidth.setProgress(15);
            }
        });
        initColorPicker();
    }

    private void initColorPicker() {
        rcvColorPickerAdapter = new RcvColorPickerAdapter(binding.vDrawView);
        binding.rcvColorPicker.setHasFixedSize(true);
        binding.rcvColorPicker.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.rcvColorPicker.setAdapter(rcvColorPickerAdapter);
    }
}