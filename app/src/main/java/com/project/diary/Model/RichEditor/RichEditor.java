package com.project.diary.Model.RichEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;

import com.project.diary.R;
import com.project.diary.databinding.ActivityDiaryBinding;

import java.io.ByteArrayOutputStream;

public class RichEditor extends jp.wasabeef.richeditor.RichEditor {
    private RichEditor mEditor;

    private ActivityDiaryBinding binding;

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

    @Override
    public void insertVideo(String url) {
        super.insertVideo(url);
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

            }
        });

        binding.imgbtnEmoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });

        binding.imgbtnSticker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });

        binding.imgbtnVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
