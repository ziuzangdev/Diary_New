package sontungmtp.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.text.DateFormatSymbols;
import java.util.Locale;

import sontungmtp.project.diary.Control.Activity.IThemeManager;
import sontungmtp.project.diary.Control.Activity.ViewDiaryActivityControl;
import sontungmtp.project.diary.Model.Diary.Diary;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.databinding.ActivityViewDiaryBinding;

public class ViewDiaryActivity extends AppCompatActivity implements IThemeManager {
    private ActivityViewDiaryBinding binding;
    private ViewDiaryActivityControl control;
    private Diary diary;

    private String finish = null;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDiaryBinding.inflate(getLayoutInflater());
        control = new ViewDiaryActivityControl(ViewDiaryActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showDialogRemove();
            }
        });
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.editMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finish != null){
                    Intent intent = new Intent(ViewDiaryActivity.this, DiaryActivity.class);
                    intent.putExtra("ID_DIARY", diary.getId());
                    intent.putExtra("mode", "ViewMode");
                    startActivity(intent);
                }
                finish();
            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initRichEditor() {
        binding.mEditor.getSettings().setJavaScriptEnabled(true);
        binding.mEditor.getSettings().setAllowFileAccess(true);
        binding.mEditor.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.mEditor.setPlaceholder("Write some thing here...");
        binding.mEditor.setEditorFontSize(16);
    }

    private void addControls() {
        initTheme();
        initDiary();
        initRichEditor();
        initView();
    }

    private void initView() {
        if(finish == null){
            binding.editMode.setVisibility(View.GONE);
        }
        if(diary.getId() == null){
            binding.remove.setVisibility(View.GONE);
        }
        if(finish == null){
            binding.remove.setVisibility(View.GONE);
        }
    }
    public String getMonth(int month) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.US);
        return dateFormatSymbols.getMonths()[month - 1];
    }
    private void initDiary() {
        String diaryStr = null;
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        try{
            diaryStr = bundle.getString("Diary");
        }catch (Exception ignore){}
        try{
            finish = bundle.getString("finish");
        }catch (Exception e){
            finish = null;
        }
        if(diaryStr != null){
            try{
                diary = new Gson().fromJson(diaryStr, Diary.class);
                if(diaryStr != null){
                    blockView();
                    binding.txtTittle.setText(diary.getTittle());
                    binding.mEditor.setHtml(diary.getDiaryData().getData());
                    binding.dateLayout.txtDay.setText(String.valueOf(diary.getDate().getDay()));
                    binding.dateLayout.txtYear.setText(String.valueOf(diary.getDate().getYear()));
                    binding.dateLayout.txtMonth.setText(getMonth(diary.getDate().getMonth()).substring(0, 3));
                    try{
                        binding.Root.setBackgroundResource(diary.getBackground());
                    }catch (Exception e){
                        binding.Root.setBackgroundColor(Color.parseColor(control.getAppThemeManager().getPaletteColor()[4]));
                    }
                }
            }catch (Exception e){
                Log.e("ViewDiaryActivity.java", "Error when init Diary from string gson in \"initDiary\" method");
            }
        }else{
            Log.e("ViewDiaryActivity.java", "Error when get intent from DiaryActivity in \"initDiary\" method");
        }
    }

    private void blockView() {
        binding.dateLayout.txtDate.setEnabled(false);
        binding.dateLayout.txtDate.setClickable(false);
        binding.txtTittle.setEnabled(false);
        binding.txtTittle.setClickable(false);
        binding.txtEmojiStatus.setClickable(false);
        binding.mEditor.setEnabled(false);
        binding.mEditor.setClickable(false);
        binding.mEditor.setFocusable(false);
    }
    private void showDialogRemove(){
        Dialog dialog = new Dialog(ViewDiaryActivity.this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_diary);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout Root = dialog.findViewById(R.id.Root);
        MaterialButton mbtnCancel = dialog.findViewById(R.id.mbtnCancel);
        MaterialButton mbtnDelete = dialog.findViewById(R.id.mbtnDelete);
        mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diary.getId() != null){
                    control.getSqLite().getSqLiteControl().removeData("Diary", diary.getId());
                    finish();
                }
            }
        });

        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
    }
}