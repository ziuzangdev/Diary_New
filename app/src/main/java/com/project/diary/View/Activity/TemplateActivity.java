package com.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.diary.Control.Activity.ActivityTemplateControl;
import com.project.diary.Control.Activity.IThemeManager;
import com.project.diary.Control.Activity.MineActivityControl;
import com.project.diary.Control.Adapter.Diary.RcvTemplateDiaryAdapter;
import com.project.diary.Model.TemplateManager.Template;
import com.project.diary.R;
import com.project.diary.databinding.ActivityMineBinding;
import com.project.diary.databinding.ActivityTemplateBinding;

public class TemplateActivity extends AppCompatActivity implements IThemeManager {
    private ActivityTemplateControl control;
    private ActivityTemplateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        control = new ActivityTemplateControl(TemplateActivity.this);
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
    }

    private void addControls() {
        initTheme();
        initRcvTemplateDiary();
    }


    private void initRcvTemplateDiary() {
        control.setRcvTemplateDiaryAdapter(new RcvTemplateDiaryAdapter(TemplateActivity.this));
        binding.rcvTemplateDiary.setLayoutManager(new LinearLayoutManager(TemplateActivity.this));
        binding.rcvTemplateDiary.setHasFixedSize(true);
        binding.rcvTemplateDiary.setAdapter(control.getRcvTemplateDiaryAdapter());
    }

    public void addTemplate(Template template){
        Bundle bundle = getIntent().getExtras();
        String NAME_ACTIVITY = bundle.getString("NAME_ACTIVITY");
        if(NAME_ACTIVITY.equals("Diary")){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("HTML_DATA", template.getHtmlData());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }else{
            Intent intent = new Intent(TemplateActivity.this, DiaryActivity.class);
            intent.putExtra("HTML_DATA", template.getHtmlData());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void initTheme() {

    }
}