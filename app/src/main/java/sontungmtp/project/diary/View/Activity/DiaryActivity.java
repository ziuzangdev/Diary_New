package sontungmtp.project.diary.View.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import sontungmtp.project.diary.Control.Activity.ActivityDiaryControl;
import sontungmtp.project.diary.Control.Activity.IThemeManager;
import sontungmtp.project.diary.Control.Adapter.ColorPcker.RcvColorPickerAdapter;
import sontungmtp.project.diary.Control.Adapter.Emoji.RcvStatusPickerAdapter;
import sontungmtp.project.diary.Control.BackgroundDiaryManager.PackageBackgroundDiaryControl;
import sontungmtp.project.diary.Control.PreferencesManager.PreferencesManagerAutoMood;
import sontungmtp.project.diary.Control.Ultil.FileUtils;
import sontungmtp.project.diary.Model.BackgroundDiaryManager.PackageBackgroundDiary;
import sontungmtp.project.diary.Model.Calendar.MyMaterialCalendarView;
import sontungmtp.project.diary.Model.Diary.Diary;
import sontungmtp.project.diary.Model.Diary.DiaryData;
import sontungmtp.project.diary.Model.RichEditor.RichEditor;
import sontungmtp.project.diary.Model.SQLite.SQLite;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;
import sontungmtp.project.diary.Model.Video.Video;
import sontungmtp.project.diary.View.Fragment.DiaryBackgroundFragment;

import sontungmtp.project.diary.R;
import sontungmtp.project.diary.databinding.ActivityDiaryBinding;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DiaryActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener, BSImagePicker.ImageLoaderDelegate, BSImagePicker.OnSelectImageCancelledListener, IThemeManager {
    private static final String TAG = "DiaryActivity.java";
    public static final int REQUEST_TEMPLATE = 243;
    public static final int REQUEST_CODE_MEDIA = 1;
    public static final int REQUEST_CODE_DRAW_CANVAS = 2;

    private ActivityDiaryBinding binding;
    private boolean isOpenFromMain = true;
    private ActivityDiaryControl control;
    private SQLite sqLite;
    private RichEditor richEditor;
    private String statusInPackage;
    private Diary diary;
    private DiaryData diaryData;
    private RcvColorPickerAdapter rcvColorPickerAdapter;
    private boolean isClickTextTool = false;
    private boolean isWatchMode = false;

    private ArrayList<Video> videos;

    public Diary getDiary() {
        return diary;
    }

    private InterstitialAd mInterstitialAd;
    private int type_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        control = new ActivityDiaryControl(DiaryActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
        loadInterstitialAd();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MEDIA) {
            if (resultCode == Activity.RESULT_OK) {

            }
        } else if (requestCode == REQUEST_CODE_DRAW_CANVAS) {
            if (resultCode == RESULT_OK) {
                String path = Objects.requireNonNull(data).getStringExtra("pathDraw");
                String alt = "alt\" style=\"max-width:50%; height:auto";
                String html = "<img src=\"" + path + "\" alt=\"" + alt + "\" />";
                richEditor.insertHtml("<BR>" + html + "<BR>");
                richEditor.insertHtml("" + "<BR>" + "<BR>");
            }
        } else if (requestCode == DiaryActivity.REQUEST_TEMPLATE) {
            if (resultCode == RESULT_OK) {
                String htmlData = null;
                try {
                    htmlData = data.getStringExtra("HTML_DATA");
                } catch (Exception e) {
                }
                if (htmlData != null) {
                    diary.getDiaryData().setData(htmlData);
                    diary.getMediaPaths().clear();
                    richEditor.setHtml(diary.getDiaryData().getData());
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
        return dateFormatSymbols.getMonths()[month - 1];
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            type_click = 2;
            mInterstitialAd.show(DiaryActivity.this);
        } else {
            if (binding.cvTextTool.getVisibility() == View.VISIBLE ||
                    binding.cvBackgroundDiary.getVisibility() == View.VISIBLE) {
                binding.cvBackgroundDiary.setVisibility(View.GONE);
                binding.cvTextTool.setVisibility(View.GONE);
            } else {
                if (diary.getId() == null) {
                    if (!diary.getDiaryData().getData().equals("") || !binding.txtTittle.getText().toString().equals("")) {
                        diary.setDraft(true);
                        int id = sqLite.getSqLiteControl().insertData(diary);
                        if (id != -1) {
                            diary.setId(String.valueOf(id));
                        } else {
                        }
                    }
                } else {
                    Diary diary1 = sqLite.getSqLiteControl().readData("Diary", diary.getId());
                    if (!diary.getDiaryData().getData().equals(diary1.getDiaryData().getData())) {
                        diary.setDraft(true);
                        sqLite.getSqLiteControl().updateData(diary, "Diary");
                    }
                }
                finish();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {

        binding.editMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpenFromMain = true;
                enableWatchMode(false);
            }
        });
        binding.imgbtnIndentLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setOutdent();
            }
        });

        binding.imgbtnIndentRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setIndent();
            }
        });

        binding.imgbtnListBullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBullets();
            }
        });
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
                if (v == binding.imgCloseTextTool) {
                    binding.cvTextTool.setVisibility(View.GONE);
                } else if (v == binding.imgCompleteTextTool) {
                    binding.cvTextTool.setVisibility(View.GONE);

                }
            }
        };

        View.OnClickListener backgroundToolEvents = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == binding.imgCompleteBackgroundTool || v == binding.imgCloseBackgroundTool) {
                    binding.cvBackgroundDiary.setVisibility(View.GONE);
                }
            }
        };
        View.OnClickListener toolEvents = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == binding.imgbtnH1) {
                    richEditor.setHeading(1);
                } else if (v == binding.imgbtnH2) {
                    richEditor.setHeading(2);
                } else if (v == binding.imgbtnH3) {
                    richEditor.setHeading(3);
                } else if (v == binding.imgbtnAlignLeft) {
                    richEditor.setAlignLeft();
                } else if (v == binding.imgbtnAlignCenter) {
                    richEditor.setAlignCenter();
                } else if (v == binding.imgbtnAlignRight) {
                    richEditor.setAlignRight();
                } else if (v == binding.imgbtnTextTool) {
                    isClickTextTool = !isClickTextTool;
                    if (isClickTextTool) {
                        binding.cvTextTool.setVisibility(View.VISIBLE);
                    } else {
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
                BSImagePicker pickerDialog = new BSImagePicker.Builder("sontungmtp.project.diary.fileprovider")
                        .isMultiSelect() //Set this if you want to use multi selection mode.
                        .setMinimumMultiSelectCount(1) //Default: 1.
                        .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                        .setMultiSelectTextColor(R.color.Fresh_Guacamole_01) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                        .setMultiSelectDoneTextColor(R.color.Fresh_Guacamole_01) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                        .setOverSelectTextColor(R.color.colorPrimaryDark) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                        .build();
                pickerDialog.show(getSupportFragmentManager(), "picker");
            }
        });

        binding.imgWatchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableWatchMode(true);
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

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd != null) {
                    type_click = 1;
                    mInterstitialAd.show(DiaryActivity.this);
                } else {
                    if (isOpenFromMain && isWatchMode) {
                        enableWatchMode(false);
                    } else {
                        if (diary.getId() == null) {
                            if (!diary.getDiaryData().getData().equals("") || !binding.txtTittle.getText().toString().equals("")) {
                                diary.setDraft(true);
                                int id = sqLite.getSqLiteControl().insertData(diary);
                                if (id != -1) {
                                    diary.setId(String.valueOf(id));
                                } else {
                                }
                            }
                        } else {
                            Diary diary1 = sqLite.getSqLiteControl().readData("Diary", diary.getId());
                            if (!diary.getDiaryData().getData().equals(diary1.getDiaryData().getData())) {
                                diary.setDraft(true);
                                sqLite.getSqLiteControl().updateData(diary, "Diary");
                            }
                        }
                        finish();
                    }
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
                for (String s : imgTag) {
                    String path = control.readAllTextFromImgSrc(s);
                    if (control.isPath(path)) {
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
                if (diary.getId() == null) {
                    int id = sqLite.getSqLiteControl().insertData(diary);
                    if (id != -1) {
                        Toast.makeText(DiaryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        diary.setId(String.valueOf(id));
                    } else {
                        Toast.makeText(DiaryActivity.this, "Can't Save", Toast.LENGTH_SHORT).show();
                    }
                } else {
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

        binding.imgbtnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cvBackgroundDiary.setVisibility(View.VISIBLE);
            }
        });

        binding.imgbtnTextTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cvTextTool.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showDialogStatus() {
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

    private void addControls() {
        initTheme();
        initSQLite();
        initStatus();
        initRichEditor();
        initDiaryData();
        initDiaryObject();
        initSticker();
        initColorPicker();
        initAudioRecorder();
        initTabBackgroundDiary();
        initSubThreadHandleChangeData();
    }

    public void setBackground(int idRes) {
        diary.setBackground(idRes);
        binding.Root.setBackgroundResource(diary.getBackground());
    }

    public void setBackground() {
        AppThemeManager appThemeManager = control.getAppThemeManager();
        binding.Root.setBackgroundColor(Color.parseColor(appThemeManager.getPaletteColor()[4]));
        diary.setBackground(-999);
    }

    private void initTabBackgroundDiary() {
        ArrayList<PackageBackgroundDiary> packageBackgroundDiaries = PackageBackgroundDiaryControl.getPackages();
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for (int i = 0; i < packageBackgroundDiaries.size(); i++) {
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

    private void enableWatchMode(boolean isEnable) {
        if (isEnable) {
            if (diary != null) {
                Intent intent = new Intent(DiaryActivity.this, ViewDiaryActivity.class);
                String diaryStr = new Gson().toJson(diary);
                Bundle bundle = new Bundle();
                intent.putExtra("Diary", diaryStr);
                if (isOpenFromMain) {
                    intent.putExtra("finish", "FINISH");
                }
                startActivity(intent);
            }
        } else {
            isWatchMode = false;
        }
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
        if (richEditor.getHtml() == null) {
            data = "";
        } else {
            data = richEditor.getHtml();
        }
        diaryData = new DiaryData.Builder()
                .data(data)
                .build();
    }

    private void initDiaryObject() {
        Log.d(TAG, "==================== initDiaryObject task started ==================");
        Bundle bundle = getIntent().getExtras();
        String mode = null;
        try {
            mode = bundle.getString("mode");
        } catch (Exception e) {
        }
        String id = null;
        try {
            id = bundle.getString("ID_DIARY");
            Log.d(TAG, "get ID completed");
        } catch (Exception e) {
        }
        if (id != null) {
            diary = sqLite.getSqLiteControl().readData("Diary", id);
            Log.d(TAG, "Real data of ID from database co pleated with data: " + diary.getDiaryData().getData());
            binding.dateLayout.txtDay.setText(String.valueOf(diary.getDate().getDay()));
            binding.dateLayout.txtYear.setText(String.valueOf(diary.getDate().getYear()));
            binding.dateLayout.txtMonth.setText(getMonth(diary.getDate().getMonth()).substring(0, 3));
            if(diary.getDiaryData().getData() != null && !diary.getDiaryData().getData().equals("")){
                binding.mEditor.focusEditor();
                addDataToDiary();
            }else{
                binding.mEditor.setPlaceholder("Write some thing here...");
            }
            if (mode == null) {
                enableWatchMode(true);
                if (isOpenFromMain) {
                    finish();
                }
            } else {
                isOpenFromMain = false;
                enableWatchMode(false);
            }
        } else {
            isOpenFromMain = false;
            diary = new Diary.Builder()
                    .tittle(binding.txtTittle.getText().toString())
                    .date(CalendarDay.today())
                    .status(statusInPackage)
                    .background(getResources().getColor(R.color.Fresh_Guacamole_05, null))
                    .mediaPaths(new ArrayList<>())
                    .diaryData(diaryData)
                    .build();
            PreferencesManagerAutoMood.initializeInstance(getApplicationContext());
            PreferencesManagerAutoMood prefManager = PreferencesManagerAutoMood.getInstance();
            if (prefManager.readAutoMood()) {
                showDialogStatus();
            }
        }
        try {
            binding.Root.setBackgroundResource(diary.getBackground());
        } catch (Exception e) {
        }
        String htmlData = null;
        try {
            htmlData = bundle.getString("HTML_DATA");
        } catch (Exception e) {
        }
        System.out.println("HTML DATA: " + htmlData);
        if (htmlData != null && !htmlData.equals("")) {
            binding.mEditor.focusEditor();
            diaryData.setData(htmlData);
            diary.setDiaryData(diaryData);
            addDataToDiary();
        } else {
            binding.mEditor.setPlaceholder("Write some thing here...");
        }
    }

    private void addDataToDiary() {
        binding.txtTittle.setText(diary.getTittle());
        binding.txtEmojiStatus.setText(diary.getStatus());
        richEditor.insertHtml(diary.getDiaryData().getData());
        Log.d("TAG", "addDataToDiary completed");
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
                for (Uri uri : uriList) {
                    String path = FileUtils.getPath(DiaryActivity.this, uri);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String alt = "alt\" style=\"max-width:50%; height:auto";
                            String html = "<img src=\"" + path + "\" alt=\"" + alt + "\" />";
                            richEditor.insertHtml("<BR>" + html + "<BR>");
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        richEditor.insertHtml("" + "<BR>" + "<BR>");
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

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getResources().getString(R.string.ads_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (type_click == 1) {
                            if (isOpenFromMain && isWatchMode) {
                                enableWatchMode(false);
                            } else {
                                if (diary.getId() == null) {
                                    if (!diary.getDiaryData().getData().equals("") || !binding.txtTittle.getText().toString().equals("")) {
                                        diary.setDraft(true);
                                        int id = sqLite.getSqLiteControl().insertData(diary);
                                        if (id != -1) {
                                            diary.setId(String.valueOf(id));
                                        } else {
                                        }
                                    }
                                } else {
                                    Diary diary1 = sqLite.getSqLiteControl().readData("Diary", diary.getId());
                                    if (!diary.getDiaryData().getData().equals(diary1.getDiaryData().getData())) {
                                        diary.setDraft(true);
                                        sqLite.getSqLiteControl().updateData(diary, "Diary");
                                    }
                                }
                                finish();
                            }

                        } else {
                            if (binding.cvTextTool.getVisibility() == View.VISIBLE ||
                                    binding.cvBackgroundDiary.getVisibility() == View.VISIBLE) {
                                binding.cvBackgroundDiary.setVisibility(View.GONE);
                                binding.cvTextTool.setVisibility(View.GONE);
                            } else {
                                if (diary.getId() == null) {
                                    if (!diary.getDiaryData().getData().equals("") || !binding.txtTittle.getText().toString().equals("")) {
                                        diary.setDraft(true);
                                        int id = sqLite.getSqLiteControl().insertData(diary);
                                        if (id != -1) {
                                            diary.setId(String.valueOf(id));
                                        } else {
                                        }
                                    }
                                } else {
                                    Diary diary1 = sqLite.getSqLiteControl().readData("Diary", diary.getId());
                                    if (!diary.getDiaryData().getData().equals(diary1.getDiaryData().getData())) {
                                        diary.setDraft(true);
                                        sqLite.getSqLiteControl().updateData(diary, "Diary");
                                    }
                                }
                                finish();
                            }
                        }

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });

    }

}