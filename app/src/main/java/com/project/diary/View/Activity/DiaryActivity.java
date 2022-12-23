package com.project.diary.View.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.facebookprovider.AXFacebookEmojiProvider;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.view.AXEmojiPopup;
import com.aghajari.emojiview.view.AXEmojiView;
import com.aghajari.emojiview.view.AXStickerView;
import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.project.diary.Control.Activity.ActivityDiaryControl;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import com.project.diary.Control.Adapter.Emoji.RcvStatusPickerAdapter;
import com.project.diary.Control.BackgroundDiaryManager.PackageBackgroundDiaryControl;
import com.project.diary.Control.Ultil.FileUtils;
import com.project.diary.Model.BackgroundDiaryManager.PackageBackgroundDiary;
import com.project.diary.Model.Calendar.MyMaterialCalendarView;
import com.project.diary.Model.Diary.Diary;
import com.project.diary.Model.Diary.DiaryData;
import com.project.diary.Model.RichEditor.RichEditor;
import com.project.diary.Model.SQLite.SQLite;
import com.project.diary.Model.ThemeManager.AppThemeManager;
import com.project.diary.Model.Video.Video;
import com.project.diary.View.Fragment.DiaryBackgroundFragment;
import com.project.diary.databinding.ActivityDiaryBinding;

import com.project.diary.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DiaryActivity extends AppCompatActivity  implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener, BSImagePicker.ImageLoaderDelegate, BSImagePicker.OnSelectImageCancelledListener, IThemeManager {
    private ActivityDiaryBinding binding;

    public static final int REQUEST_TEMPLATE = 243;

    private ActivityDiaryControl control;

    private SQLite sqLite;

    private RichEditor richEditor;

    private String statusInPackage;

    private Diary diary;

    private DiaryData diaryData;

    private AXEmojiView emojiView;

    private AXStickerView stickerView;

    private Thread subThread;

    private RcvColorPickerAdapter rcvColorPickerAdapter;

    private boolean isCLickTextColor = false;

    private boolean isClickTextTool = false;

    private boolean isWatchMode = false;

    private boolean isOpenStickerView = false;

    private boolean isRunning;

    private float mDownPosX, mDownPosY, mUpPosX, mUpPosY;

    public static final int REQUEST_CODE_MEDIA = 1;

    public static final int REQUEST_CODE_DRAW_CANVAS = 2;

    private float MOVE_THRESHOLD_DP;

    private ArrayList<Video> videos;

    private AXEmojiPopup emojiPopupEmoji, emojiPopupSticker;

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

            }
        }else if(requestCode == REQUEST_CODE_DRAW_CANVAS){
            if(resultCode == RESULT_OK){
                String path = Objects.requireNonNull(data).getStringExtra("pathDraw");
                richEditor.insertImage(path, "alt\" style=\"max-width:50%; height:auto");
                richEditor.insertHtml( ""+ "<BR>" + "<BR>");
            }
        }else if(requestCode == DiaryActivity.REQUEST_TEMPLATE){
            if(resultCode == RESULT_OK){
                String htmlData = null;
                try{
                    htmlData = data.getStringExtra("HTML_DATA");
                }catch (Exception e){}
                if(htmlData != null){
                    richEditor.setHtml(htmlData);
                }
            }
        }
    }
    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    public String getMonth(int month) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.US);
        return dateFormatSymbols.getMonths()[month-1];
    }
    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        if(diary.getId() == null){
            diary.setDraft(true);
            int id = sqLite.getSqLiteControl().insertData(diary);
            if(id != -1){
                diary.setId(String.valueOf(id));
            }else{
            }
        }else{
            Diary diary1 = sqLite.getSqLiteControl().readData("Diary", diary.getId());
            if(!diary.getDiaryData().getData().equals(diary1.getDiaryData().getData())){
                diary.setDraft(true);
                sqLite.getSqLiteControl().updateData(diary, "Diary");
            }
        }
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {
        binding.imgTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, TemplateActivity.class);
                intent.putExtra("NAME_ACTIVITY", "Diary");
                startActivityForResult(intent, REQUEST_TEMPLATE);
            }
        });

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

        View.OnClickListener backgroundToolEvents = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == binding.imgCompleteBackgroundTool || v == binding.imgCloseBackgroundTool){
                    binding.cvBackgroundDiary.setVisibility(View.GONE);
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
        binding.imgCompleteBackgroundTool.setOnClickListener(backgroundToolEvents);
        binding.imgCloseBackgroundTool.setOnClickListener(backgroundToolEvents);
        binding.imgbtnH1.setOnClickListener(toolEvents);
        binding.imgbtnH2.setOnClickListener(toolEvents);
        binding.imgbtnH3.setOnClickListener(toolEvents);
        binding.imgbtnAlignLeft.setOnClickListener(toolEvents);
        binding.imgbtnAlignCenter.setOnClickListener(toolEvents);
        binding.imgbtnAlignRight.setOnClickListener(toolEvents);
        binding.imgCloseTextTool.setOnClickListener(textToolEvents);
        binding.imgCompleteTextTool.setOnClickListener(textToolEvents);



        binding.imgbtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker pickerDialog = new BSImagePicker.Builder("com.project.diary.fileprovider")
                        .isMultiSelect() //Set this if you want to use multi selection mode.
                        .setMinimumMultiSelectCount(1) //Default: 1.
                        .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                        .setMultiSelectTextColor(R.color.Fresh_Guacamole_01) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                        .setMultiSelectDoneTextColor(R.color.Fresh_Guacamole_01) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                        .setOverSelectTextColor(R.color.colorPrimaryDark) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                        .disableOverSelectionMessage() //You can also decide not to show this over select message.
                        .build();
                pickerDialog.show(getSupportFragmentManager(), "picker");
            }
        });

        binding.imgWatchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.close.setImageResource(R.drawable.icon_back_black);
                isWatchMode = !isWatchMode;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(dpToPx(20), dpToPx(100), dpToPx(20), dpToPx(0));
                LinearLayout linearLayout = findViewById(R.id.llRichEditor1);
                linearLayout.setLayoutParams(layoutParams);
                binding.ToolSave.setVisibility(View.GONE);
                binding.ToolTextTool.setVisibility(View.GONE);
                binding.dateLayout.txtDate.setClickable(false);
                binding.cvStatus.setClickable(false);
                binding.cvTextTool.setVisibility(View.GONE);
                binding.cvBackgroundDiary.setVisibility(View.GONE);
                richEditor.setInputEnabled(false);
            }
        });

        binding.dateLayout.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DiaryActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_calandar);
                dialog.setCanceledOnTouchOutside(true);
                MyMaterialCalendarView calendarView = dialog.findViewById(R.id.calendarView);
                calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
                LinearLayout Root = dialog.findViewById(R.id.Root);
                TextView txtClose = dialog.findViewById(R.id.txtClose);
                TextView txtToday = dialog.findViewById(R.id.txtToday);
                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                txtToday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarView.setCurrentDate(CalendarDay.today());
                        calendarView.setDateSelected(CalendarDay.today(), true);
                        diary.setDate(CalendarDay.today());
                        binding.dateLayout.txtDay.setText(String.valueOf(diary.getDate().getDay()));
                        binding.dateLayout.txtYear.setText(String.valueOf(diary.getDate().getYear()));
                        binding.dateLayout.txtMonth.setText(getMonth(diary.getDate().getMonth()).substring(0, 2));
                    }
                });
                Root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        diary.setDate(date);
                        binding.dateLayout.txtDay.setText(String.valueOf(diary.getDate().getDay()));
                        binding.dateLayout.txtYear.setText(String.valueOf(diary.getDate().getYear()));
                        binding.dateLayout.txtMonth.setText(getMonth(diary.getDate().getMonth()).substring(0, 3));
                    }
                });

                dialog.show();
            }
        });
        richEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.closeAllPopup();
            }
        });

//        richEditor.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
//                    String html = richEditor.getHtml();
//                    String[] lines = html.split("\n");
//                    StringBuilder sb = new StringBuilder();
//                    for (String line : lines) {
//                        if (line.startsWith("<ol>") || line.startsWith("<ul>")) {
//                            // remove the serial number from the list item
//                            line = line.substring(line.indexOf(">") + 1);
//                        }
//                        sb.append(line).append("\n");
//                    }
//                    richEditor.setHtml(sb.toString());
//                    return true;
//                }
//                return false;
//            }
//        });

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
                if(isWatchMode){
                    isWatchMode = false;
                    binding.close.setImageResource(R.drawable.icon_close_stroke);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(dpToPx(20), dpToPx(100), dpToPx(20), dpToPx(65));
                    LinearLayout linearLayout = findViewById(R.id.llRichEditor1);
                    linearLayout.setLayoutParams(layoutParams);
                    binding.ToolSave.setVisibility(View.VISIBLE);
                    binding.ToolTextTool.setVisibility(View.VISIBLE);
                    binding.dateLayout.txtDate.setClickable(true);
                    binding.cvStatus.setClickable(true);
                    richEditor.setInputEnabled(true);
                }else{
                    isRunning = false;
                    if(diary.getId() == null){
                        diary.setDraft(true);
                        int id = sqLite.getSqLiteControl().insertData(diary);
                        if(id != -1){
                            diary.setId(String.valueOf(id));
                        }else{
                        }
                    }else{
                        Diary diary1 = sqLite.getSqLiteControl().readData("Diary", diary.getId());
                        if(!diary.getDiaryData().getData().equals(diary1.getDiaryData().getData())){
                            diary.setDraft(true);
                            sqLite.getSqLiteControl().updateData(diary, "Diary");
                        }
                    }
                    finish();
                }
            }
        });

        richEditor.setOnTextChangeListener(new jp.wasabeef.richeditor.RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                System.out.println(text);
                diary.getDiaryData().setData(text);
                List<String> imgTag = control.getImgTags(diary.getDiaryData().getData());
                diary.setMediaPaths(new ArrayList<>());
                for(String s : imgTag){
                    String path = control.readAllTextFromImgSrc(s);
                    if(control.isPath(path)){
                        diary.getMediaPaths().add(path);
                    }
                }
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
                diary.setDraft(false);
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
                showDialogStatus();
            }
        });

    }


    private void showDialogStatus(){
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
        rcvStatus.setLayoutManager(new GridLayoutManager(DiaryActivity.this, 5));
        rcvStatus.setAdapter(rcvStatusPickerAdapter);
        dialog.show();
    }
    private void initTextToolChoose() {

    }

    private void addControls() {
        initTheme();
        initSQLite();
        initStatus();
        initRichEditor();
        initDiaryData();
        initDiaryObject();
        initEmoji();
        initSticker();
        initColorPicker();
        initAudioRecorder();
        initTabBackgroundDiary();
        initSubThreadHandleChangeData();

    }

    public void setBackground(int idRes){
        diary.setBackground(idRes);
        binding.Root.setBackgroundResource(diary.getBackground());
    }

    public void setBackground(){
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
        diary.setBackground(-999);
    }

    private void initTabBackgroundDiary() {
        ArrayList<PackageBackgroundDiary> packageBackgroundDiaries = PackageBackgroundDiaryControl.getPackages();
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for(int i=0; i< packageBackgroundDiaries.size(); i++){
            PackageBackgroundDiary packageBackgroundDiary = packageBackgroundDiaries.get(i);
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", i);
            System.out.println("========" + i);
            creator.add(packageBackgroundDiary.getNamePackage(), DiaryBackgroundFragment.class, bundle);
        }
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), creator.create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
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
//        stickerView = new AXStickerView(DiaryActivity.this , "stickers" , new WhatsAppProvider(DiaryActivity.this));
//        stickerView.setEditText(binding.edt1);
//        emojiPopupSticker = new AXEmojiPopup(stickerView);
//        richEditor.setEmojiPopupSticker(emojiPopupSticker);
    }

    private void initEmoji() {
        AXEmojiManager.install(DiaryActivity.this,new AXFacebookEmojiProvider(DiaryActivity.this));
        emojiView = new AXEmojiView(DiaryActivity.this);
        emojiView.setEditText(binding.edt);
        emojiPopupEmoji = new AXEmojiPopup(emojiView);
        richEditor.setEmojiPopupEmoji(emojiPopupEmoji);
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
                    .date(CalendarDay.today())
                    .status(statusInPackage)
                    .background(getResources().getColor(R.color.Fresh_Guacamole_05, null))
                    .mediaPaths(new ArrayList<>())
                    .diaryData(diaryData)
                    .build();
            showDialogStatus();
        }
        try{
            binding.Root.setBackgroundResource(diary.getBackground());
        }catch (Exception e){}
        binding.dateLayout.txtDay.setText(String.valueOf(diary.getDate().getDay()));
        binding.dateLayout.txtYear.setText(String.valueOf(diary.getDate().getYear()));
        binding.dateLayout.txtMonth.setText(getMonth(diary.getDate().getMonth()).substring(0, 3));
        String htmlData = null;
        try{
            htmlData = bundle.getString("HTML_DATA");
        }catch (Exception e){}
        if(htmlData != null){
            diaryData.setData(htmlData);
            diary.setDiaryData(diaryData);
            addDataToDiary();
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

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(DiaryActivity.this)
                .load(imageUri)
                .into(ivImage);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Uri uri : uriList){
                    String path = FileUtils.getPath(DiaryActivity.this, uri);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            richEditor.insertImage(path, "alt\" style=\"max-width:60%; height:auto");
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        richEditor.insertHtml( ""+ "<BR>" + "<BR>");
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {

    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        String path = FileUtils.getPath(DiaryActivity.this, uri);
        System.out.println(path);
        richEditor.insertImage(path, "alt\" style=\"max-width:50%; height:auto");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void initTheme() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
        binding.llSave.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[3]));
    }
}