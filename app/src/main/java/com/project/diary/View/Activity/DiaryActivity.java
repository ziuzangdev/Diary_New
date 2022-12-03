package com.project.diary.View.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.facebookprovider.AXFacebookEmojiProvider;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.view.AXEmojiPopup;
import com.aghajari.emojiview.view.AXEmojiPopupLayout;
import com.aghajari.emojiview.view.AXEmojiView;
import com.aghajari.emojiview.view.AXStickerView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import com.project.diary.Control.Adapter.Emoji.RcvStatusPickerAdapter;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Diary.DiaryData;
import com.project.diary.Model.RichEditor.RichEditor;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.Video.Video;
import com.project.diary.databinding.ActivityDiaryBinding;

import com.project.diary.R;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DiaryActivity extends AppCompatActivity {
    private ActivityDiaryBinding binding;

    private ActivityDiaryControl control;

    private SQLite sqLite;

    private RichEditor richEditor;

    private String statusInPackage;

    private Diary diary;

    private DiaryData diaryData;

    private AXEmojiView emojiView;

    private Thread subThread;

    private RcvColorPickerAdapter rcvColorPickerAdapter;

    private boolean isCLickTextColor = false;

    private boolean isClickTextTool = false;

    private boolean isRunning;

    private float mDownPosX, mDownPosY, mUpPosX, mUpPosY;

    public static final int REQUEST_CODE_MEDIA = 1;

    public static final int REQUEST_CODE_DRAW_CANVAS = 2;

    private float MOVE_THRESHOLD_DP;

    private ArrayList<Video> videos;

    private AXEmojiPopup emojiPopup;

    public Diary getDiary() {
        return diary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        control = new ActivityDiaryControl(DiaryActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        MOVE_THRESHOLD_DP = 20.0F * getResources().getDisplayMetrics().density;
        addControls();
        addEvents();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        richEditor.focusEditor();
        if(requestCode == REQUEST_CODE_MEDIA){
            if(resultCode == Activity.RESULT_OK){
                ArrayList<String> pathChooseImage;
                pathChooseImage = new ArrayList<>();
                try{
                    pathChooseImage = Objects.requireNonNull(data).getStringArrayListExtra("pathChooseImage");
                }catch (Exception ignore){}
                if(pathChooseImage.size() > 0){
                   for(String path : pathChooseImage){
                       richEditor.insertImage(path, "alt\" style=\"max-width:50%; height:auto");
                   }
//                   diary.getMediaPaths().clear();
//                   diary.getMediaPaths().addAll(control.pullLinks(richEditor.getHtml()));
                }
            }
        }else if(requestCode == REQUEST_CODE_DRAW_CANVAS){
            if(resultCode == RESULT_OK){
                String path = Objects.requireNonNull(data).getStringExtra("pathDraw");
                richEditor.insertImage(path, "alt\" style=\"max-width:50%; height:auto");
            }
//            diary.getMediaPaths().clear();
//            diary.getMediaPaths().addAll(control.pullLinks(richEditor.getHtml()));
            System.out.println(richEditor.getHtml());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {

        View.OnClickListener textToolEvents = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == binding.imgCloseTextTool){
                    binding.cvTextTool.setVisibility(View.GONE);
                }else if(v == binding.imgCompleteTextTool){
                    binding.cvTextTool.setVisibility(View.GONE);

                }
            }
        };
        View.OnClickListener toolEvents = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == binding.imgbtnH1){
                    richEditor.setHeading(1);
                }else if(v == binding.imgbtnH2){
                    richEditor.setHeading(2);
                }else if(v == binding.imgbtnH3){
                    richEditor.setHeading(3);
                }else if(v == binding.imgbtnAlignLeft){
                    richEditor.setAlignLeft();
                }else if(v == binding.imgbtnAlignCenter){
                    richEditor.setAlignCenter();
                }else if(v == binding.imgbtnAlignRight){
                    richEditor.setAlignRight();
                }else if(v == binding.imgbtnTextTool){
                    isClickTextTool = !isClickTextTool;
                    if(isClickTextTool){
                        binding.cvTextTool.setVisibility(View.VISIBLE);
                    }else{
                        binding.cvTextTool.setVisibility(View.GONE);
                    }
                }
            }
        };
        binding.imgbtnH1.setOnClickListener(toolEvents);
        binding.imgbtnH2.setOnClickListener(toolEvents);
        binding.imgbtnH3.setOnClickListener(toolEvents);
        binding.imgbtnAlignLeft.setOnClickListener(toolEvents);
        binding.imgbtnAlignCenter.setOnClickListener(toolEvents);
        binding.imgbtnAlignRight.setOnClickListener(toolEvents);
        binding.imgCloseTextTool.setOnClickListener(textToolEvents);
        binding.imgCompleteTextTool.setOnClickListener(textToolEvents);
        richEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.closeAllPopup();
            }
        });

        emojiView.setOnEmojiActionsListener(new OnEmojiActions() {
            @Override
            public void onClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant) {
                richEditor.insertHtml(emoji.getUnicode());
            }

            @Override
            public boolean onLongClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant) {
                return false;
            }
        });

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                finish();
            }
        });

        richEditor.setOnTextChangeListener(new jp.wasabeef.richeditor.RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                System.out.println(text);
                diary.getDiaryData().setData(text);
            }
        });

        binding.txtTittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                diary.setTittle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.cvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diary.getId() == null){
                    int id = sqLite.getSqLiteControl().insertData(diary);
                    if(id != -1){
                        Toast.makeText(DiaryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        diary.setId(String.valueOf(id));
                    }else{
                        Toast.makeText(DiaryActivity.this, "Can't Save", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    sqLite.getSqLiteControl().updateData(diary, "Diary");
                    Toast.makeText(DiaryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.cvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DiaryActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_status_picker);
                dialog.setCanceledOnTouchOutside(true);
                RecyclerView rcvStatus = dialog.findViewById(R.id.rcvStatus);
                RcvStatusPickerAdapter rcvStatusPickerAdapter = new RcvStatusPickerAdapter(control.getEmojis(), dialog, binding, DiaryActivity.this);
                LinearLayout Root = dialog.findViewById(R.id.Root);
                Root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                rcvStatus.setHasFixedSize(true);
                rcvStatus.setLayoutManager(new GridLayoutManager(DiaryActivity.this, 4));
                rcvStatus.setAdapter(rcvStatusPickerAdapter);
                dialog.show();
            }
        });


    }

    private void initTextToolChoose() {

    }

    private void addControls() {
        initSQLite();
        initStatus();
        initRichEditor();
        initDiaryData();
        initDiaryObject();
        initEmoji();
        initSticker();
        initColorPicker();
        initAudioRecorder();
        initSubThreadHandleChangeData();

    }

    private void initAudioRecorder() {

    }

    private void initColorPicker() {
        rcvColorPickerAdapter = new RcvColorPickerAdapter(richEditor);
        binding.rcvColorPicker.setHasFixedSize(true);
        binding.rcvColorPicker.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        binding.rcvColorPicker.setAdapter(rcvColorPickerAdapter);
    }

    private void initSticker() {

    }

    private void initEmoji() {
        AXEmojiManager.install(DiaryActivity.this,new AXFacebookEmojiProvider(DiaryActivity.this));
        emojiView = new AXEmojiView(DiaryActivity.this);
        emojiView.setEditText(binding.edt);
        emojiPopup = new AXEmojiPopup(emojiView);
        richEditor.setEmojiPopup(emojiPopup);
    }

    private void initSQLite() {
        sqLite = new SQLite(DiaryActivity.this);
    }

    private void initSubThreadHandleChangeData() {
//        subThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isRunning = true;
//                String currentData = richEditor.getHtml();
//                while (isRunning){
//                    if(!diaryData.getData().equals(currentData)){
//                        currentData = diaryData.getData();
//                    }
//                }
//            }
//        });
//        subThread.start();
    }

    private void initDiaryData() {
        String data;
        if(richEditor.getHtml() == null){
            data = "";
        }else{
            data = richEditor.getHtml();
        }
        diaryData = new DiaryData.Builder()
                .data(data)
                .build();
    }

    private void initDiaryObject() {
        Bundle bundle = getIntent().getExtras();
        String id = null;
        try{
            id = bundle.getString("ID_DIARY");
        }catch (Exception e){}
        if(id != null){
            diary = sqLite.getSqLiteControl().readData("Diary", id);
            addDataToDiary();
        }else{
            diary = new Diary.Builder()
                    .tittle(binding.txtTittle.getText().toString())
                    .date(LocalDate.now().toString())
                    .status(statusInPackage)
                    .background(getResources().getColor(R.color.Fresh_Guacamole_05, null))
                    .mediaPaths(new ArrayList<>())
                    .diaryData(diaryData)
                    .build();
        }
    }

    private void addDataToDiary() {
        binding.txtTittle.setText(diary.getTittle());
        binding.txtEmojiStatus.setText(diary.getStatus());
        richEditor.insertHtml(diary.getDiaryData().getData());
    }

    private void initStatus() {
        statusInPackage = control.initStatus();
        binding.txtEmojiStatus.setText(statusInPackage);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initRichEditor() {
       richEditor = binding.mEditor;
       richEditor.setBinding(binding);
       richEditor.getSettings().setJavaScriptEnabled(true);
       richEditor.getSettings().setAllowFileAccess(true);
       richEditor.getSettings().setMediaPlaybackRequiresUserGesture(false);
       richEditor.setPlaceholder("Write some thing here...");
       richEditor.setEditorFontSize(16);
       richEditor.initAllEvents();
    }

}