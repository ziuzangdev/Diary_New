package sontungmtp.project.diary.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import sontungmtp.project.diary.Control.Activity.MediaActivityControl;
import sontungmtp.project.diary.Control.Adapter.Media.VpMediaAdapter;
import sontungmtp.project.diary.R;
import sontungmtp.project.diary.View.Fragment.MediaFragment;
import sontungmtp.project.diary.databinding.ActivityMediaBinding;

import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {

    private MediaActivityControl control;

    private ActivityMediaBinding binding;

    private ArrayList<String> arrPathImage;

    private ArrayList<String> arrPathVideo;

    private VpMediaAdapter vpMediaAdapter;

    /**
     * .get(0) is Image <br>
     * .get(1) is Video
     */
    private ArrayList<MediaFragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaBinding.inflate(getLayoutInflater());
        control = new MediaActivityControl(MediaActivity.this);
        control.showCustomUI();
        setContentView(binding.getRoot());
        addControls();
        addEvents();
    }

    private void addEvents() {


        binding.vpMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    binding.txtModeImage.setTextColor(getResources().getColor(R.color.black));
                    binding.v1.setVisibility(View.VISIBLE);
                    binding.txtModeVideo.setTextColor(getResources().getColor(R.color.not_choose_state));
                    binding.v2.setVisibility(View.INVISIBLE);
                }else if(position == 1){
                    binding.txtModeImage.setTextColor(getResources().getColor(R.color.not_choose_state));
                    binding.v1.setVisibility(View.INVISIBLE);
                    binding.txtModeVideo.setTextColor(getResources().getColor(R.color.black));
                    binding.v2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.cvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> pathChooseImage = new ArrayList<>();
                ArrayList<String> pathChooseVideo = new ArrayList<>();
                if(fragments.get(0).getRcvMediaAdapter().getPathChose() != null){
                    pathChooseImage.addAll(fragments.get(0).getRcvMediaAdapter().getPathChose());
                }
                if(fragments.get(1).getRcvMediaAdapter().getPathChose() != null){
                    pathChooseVideo.addAll(fragments.get(1).getRcvMediaAdapter().getPathChose());
                }
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("pathChooseImage", pathChooseImage);
                resultIntent.putStringArrayListExtra("pathChooseVideo", pathChooseVideo);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void addControls() {
        initFragments();
        initMediaPath();
        initViewPager();
    }

    private void initViewPager() {
        vpMediaAdapter = new VpMediaAdapter(getSupportFragmentManager(), fragments);
        binding.vpMedia.setAdapter(vpMediaAdapter);
    }

    /**
     * Init two ViewPager(Once to load all Image, Another to load all Video)
     */
    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new MediaFragment(MediaFragment.MODE_IMAGE));
        fragments.add(new MediaFragment(MediaFragment.MODE_VIDEO));
    }

    /**
     * Init and start two Thread to read Image and Video
     */
    private void initMediaPath() {
        Thread threadImage = new Thread(new Runnable() {
            @Override
            public void run() {
                //Start Thread to read all image
                control.generateAllPathImage();
                //Wait until thread work
                while (!control.isGenerateImagePath()){}
                //Wait during thread working
                while ((control.isGenerateImagePath())){
                    arrPathImage = control.getArrPathImage();
                    //Send path images to MediaFragment(Mode Image)
                    fragments.get(0).setMediaPath(arrPathImage);
                }
            }
        });
        Thread threadVideo = new Thread(new Runnable() {
            @Override
            public void run() {
                //Start Thread to read all video
                control.generateAllPathVideo();
                //Wait until thread work
                while (!control.isGenerateVideoPath()){}
                //Wait during thread working
                while ((control.isGenerateVideoPath())){
                    arrPathVideo = control.getArrPathVideo();
                    //Send path images to MediaFragment(Mode Video)
                    fragments.get(1).setMediaPath(arrPathVideo);
                }
            }
        });
        threadImage.start();
        //threadVideo.start();
    }
}