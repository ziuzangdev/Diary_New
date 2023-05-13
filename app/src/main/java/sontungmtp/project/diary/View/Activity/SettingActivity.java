package sontungmtp.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import sontungmtp.project.diary.Control.Activity.ActivitySettingControl;
import sontungmtp.project.diary.Control.PreferencesManager.PreferencesManagerAutoMood;

import sontungmtp.project.diary.R;
import sontungmtp.project.diary.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;

    private ActivitySettingControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        control = new ActivitySettingControl(SettingActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {
        binding.sStateMood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManagerAutoMood.initializeInstance(getApplicationContext());
                PreferencesManagerAutoMood prefManager = PreferencesManagerAutoMood.getInstance();
                boolean isAuto = !isChecked;
                prefManager.saveAutoMood(isAuto);
            }
        });

        binding.llRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = getPackageName();
                String url = "https://play.google.com/store/apps/details?id=" + packageName + "&hl=en";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        binding.llSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetEmail = "daoxuanbinh.hp65@gmail.com";
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + targetEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feed back From App " + getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Feedback: ");
            }
        });

        binding.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://sites.google.com/site/fallinlovestudioimages/privacy-policy";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        PreferencesManagerAutoMood.initializeInstance(getApplicationContext());
        PreferencesManagerAutoMood prefManager = PreferencesManagerAutoMood.getInstance();
        boolean isAuto = !prefManager.readAutoMood();
        binding.sStateMood.setChecked(isAuto);
    }
}