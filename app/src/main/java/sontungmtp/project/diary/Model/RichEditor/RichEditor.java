package sontungmtp.project.diary.Model.RichEditor;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import sontungmtp.project.diary.Model.Audio.AudioRecorder;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.View.Activity.CanvasActivity;
import sontungmtp.project.diary.View.Activity.DiaryActivity;
import sontungmtp.project.diary.databinding.ActivityDiaryBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RichEditor extends jp.wasabeef.richeditor.RichEditor {
    private RichEditor mEditor;

    private ActivityDiaryBinding binding;

    private  boolean isSetNumber = false;

    private Timer T;
    private long secondCount = 0;

    private AudioRecorder audioRecorder;

    private boolean isClickTextTool = false;

    private boolean isCheckForVoiceDialog = false;

    private boolean isOpenSticker = false, isOpenEmoji = false;
    public RichEditor(Context context) {
        super(context);
        mEditor = this;
        initRichEditor();
    }

    public RichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        mEditor = this;
        initRichEditor();
    }

    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mEditor = this;
        initRichEditor();
    }

    public void setBinding(ActivityDiaryBinding binding) {
        this.binding = binding;
    }


    private void initRichEditor() {
        mEditor.setEditorFontSize(12);
        mEditor.setEditorFontColor(getResources().getColor(R.color.black));
        mEditor.setEditorBackgroundColor(Color.parseColor("#00000000"));
        mEditor.setPlaceholder("Write some here...");
    }

    @Override
    protected EditorWebViewClient createWebviewClient() {
        return super.createWebviewClient();
    }

    @Override
    public void setOnTextChangeListener(OnTextChangeListener listener) {
        super.setOnTextChangeListener(listener);
    }

    @Override
    public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        super.setOnDecorationChangeListener(listener);
    }

    @Override
    public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        super.setOnInitialLoadListener(listener);
    }

    @Override
    public void setHtml(String contents) {
        super.setHtml(contents);
    }

    @Override
    public String getHtml() {
        return super.getHtml();
    }

    @Override
    public void setEditorFontColor(int color) {
        super.setEditorFontColor(color);
    }

    @Override
    public void setEditorFontSize(int px) {
        super.setEditorFontSize(px);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public void setEditorBackgroundColor(int color) {
        super.setEditorBackgroundColor(color);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    @Override
    public void setBackground(String url) {
        super.setBackground(url);
    }

    @Override
    public void setEditorWidth(int px) {
        super.setEditorWidth(px);
    }

    @Override
    public void setEditorHeight(int px) {
        super.setEditorHeight(px);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        super.setPlaceholder(placeholder);
    }

    @Override
    public void setInputEnabled(Boolean inputEnabled) {
        super.setInputEnabled(inputEnabled);
    }

    @Override
    public void loadCSS(String cssFile) {
        super.loadCSS(cssFile);
    }

    @Override
    public void undo() {
        super.undo();
    }

    @Override
    public void redo() {
        super.redo();
    }

    @Override
    public void setBold() {
        super.setBold();
    }

    @Override
    public void setItalic() {
        super.setItalic();
    }

    @Override
    public void setSubscript() {
        super.setSubscript();
    }

    @Override
    public void setSuperscript() {
        super.setSuperscript();
    }

    @Override
    public void setStrikeThrough() {
        super.setStrikeThrough();
    }

    @Override
    public void setUnderline() {
        super.setUnderline();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    @Override
    public void setTextBackgroundColor(int color) {
        super.setTextBackgroundColor(color);
    }

    @Override
    public void setFontSize(int fontSize) {
        super.setFontSize(fontSize);
    }

    @Override
    public void removeFormat() {
        super.removeFormat();
    }

    @Override
    public void setHeading(int heading) {
        super.setHeading(heading);
    }

    @Override
    public void setIndent() {
        super.setIndent();
    }

    @Override
    public void setOutdent() {
        super.setOutdent();
    }

    @Override
    public void setAlignLeft() {
        super.setAlignLeft();
    }

    @Override
    public void setAlignCenter() {
        super.setAlignCenter();
    }

    @Override
    public void setAlignRight() {
        super.setAlignRight();
    }

    @Override
    public void setBlockquote() {
        super.setBlockquote();
    }

    @Override
    public void setBullets() {
        super.setBullets();
    }

    @Override
    public void setNumbers() {
        super.setNumbers();
    }

    public void insertImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String dataURL= "data:image/png;base64," + imgageBase64;
        insertImage(dataURL, "");
    }

    @Override
    public void insertImage(String url, String alt) {
        focusEditor();
        super.insertImage(url, alt);
    }

    @Override
    public void insertImage(String url, String alt, int width) {
        super.insertImage(url, alt, width);
    }

    @Override
    public void insertImage(String url, String alt, int width, int height) {
        super.insertImage(url, alt, width, height);
    }




    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File( ((Activity)getContext()).getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out =((Activity)getContext()).openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String toBase64(Bitmap bitmap, String type) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        switch (type) {
            case "jpg": case "jpeg":
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                break;
            case "png":
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                break;
        }
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    @Override
    public void insertVideo(String url) {
        super.insertVideo(url);
//        exec("javascript:RE.prepareInsert();");
//        exec("javascript:RE.insertHTML('</p><video src=\"" + url + "controls>\n" + "</video><br><br>')");
    }

    public void insertImageAsBase64(Bitmap bitmap) {
        String tag = "data:image/" + "png" + ";base64, ";
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + tag + convert(bitmap) + "','');");
    }

    public Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
    public void insertVideo(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertVideo('" + url + "', '" + alt + "');");
    }

    @Override
    public void insertVideo(String url, int width) {
        super.insertVideo(url, width);
    }

    @Override
    public void insertVideo(String url, int width, int height) {
        super.insertVideo(url, width, height);
    }

    @Override
    public void insertAudio(String url) {
        super.insertAudio(url);
    }

    @Override
    public void insertYoutubeVideo(String url) {
        super.insertYoutubeVideo(url);
    }

    @Override
    public void insertYoutubeVideo(String url, int width) {
        super.insertYoutubeVideo(url, width);
    }

    @Override
    public void insertYoutubeVideo(String url, int width, int height) {
        super.insertYoutubeVideo(url, width, height);
    }

    @Override
    public void insertLink(String href, String title) {
        super.insertLink(href, title);
    }

    @Override
    public void insertTodo() {
        super.insertTodo();
    }

    @Override
    public void focusEditor() {
        super.focusEditor();
    }

    @Override
    public void clearFocusEditor() {
        super.clearFocusEditor();
    }

    @Override
    protected void exec(String trigger) {
        super.exec(trigger);
    }

    /**
     * You must define all event when click into each item of Text Editor Kit inside this method
     */
    public void initAllEvents() {
        binding.imgbtnBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.imgbtnBrush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CanvasActivity.class);
                ((Activity)getContext()).startActivityForResult(intent, DiaryActivity.REQUEST_CODE_DRAW_CANVAS);
            }
        });


        binding.imgbtnImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        binding.imgbtnList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isSetNumber = !isSetNumber;
                if(isSetNumber){
                    setNumbers();
                }else{
                    setOutdent();
                }
            }
        });

        binding.imgbtnBold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBold();
            }
        });

        binding.imgbtnItalic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setItalic();
            }
        });

        binding.imgbtnUnderline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnderline();
            }
        });

        binding.imgbtnTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.imgbtnTextTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cvTextTool.setVisibility(View.VISIBLE);

            }
        });



        binding.imgbtnVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = false;
                Dialog dialog = new Dialog(getContext(), R.style.Dialog);
                dialog.setContentView(R.layout.dialog_audio_recoder);
                dialog.setCanceledOnTouchOutside(true);
                RadioButton rbtnImageRecoder = dialog.findViewById(R.id.rbtnImageRecoder);
                MaterialButton mbtnStartRecoder = dialog.findViewById(R.id.mbtnStartRecoder);
                MaterialButton mbtnNext = dialog.findViewById(R.id.mbtnNext);
                LinearLayout Root = dialog.findViewById(R.id.Root);
                TextView txtTimerRecorder = dialog.findViewById(R.id.txtTimerRecorder);
                View.OnClickListener recordAudioEvents = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCheckForVoiceDialog = !isCheckForVoiceDialog;
                        rbtnImageRecoder.setChecked(isCheckForVoiceDialog);
                        if(isCheckForVoiceDialog){
                            mbtnNext.setVisibility(View.GONE);
                            mbtnStartRecoder.setText("STOP");
                            audioRecorder = new AudioRecorder("RecordDiary", getContext());
                            try {
                                audioRecorder.start();
                                T=new Timer();
                                T.scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        ((Activity)getContext()).runOnUiThread(new Runnable()
                                        {
                                            @SuppressLint("DefaultLocale")
                                            @Override
                                            public void run()
                                            {
                                                secondCount++;
                                                txtTimerRecorder.setText(String.format("%02d:%02d:%02d", secondCount / 3600,
                                                        (secondCount % 3600) / 60, (secondCount % 60)));
                                            }
                                        });
                                    }
                                }, 1000, 1000);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            mbtnStartRecoder.setText("START");
                            try {
                                audioRecorder.stop();
                                mbtnNext.setVisibility(View.VISIBLE);
                                T.cancel();
                                secondCount = 0;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };
                rbtnImageRecoder.setOnClickListener(recordAudioEvents);
                mbtnStartRecoder.setOnClickListener(recordAudioEvents);
                mbtnNext.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audioRecorder.saveRecorder();
                        try {
                            audioRecorder.playarcoding(audioRecorder.path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        focusEditor();
                        insertHtml("<audio src=\"content://" + audioRecorder.path + "\" controls></audio><br>");
                        //(audioRecorder.path);
                        dialog.dismiss();
                    }
                });
                Root.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void insertHtml(String content) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertHTML('" + content + "');");
    }

    public void closeAllPopup() {
        binding.cvTextTool.setVisibility(View.GONE);
        binding.cvBackgroundDiary.setVisibility(View.GONE);
    }

}
