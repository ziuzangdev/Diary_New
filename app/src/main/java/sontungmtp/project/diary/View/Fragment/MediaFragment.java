package sontungmtp.project.diary.View.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sontungmtp.project.diary.Control.Adapter.Media.RcvMediaAdapter;
import sontungmtp.project.diary.databinding.FragmentMediaBinding;

import java.util.ArrayList;
public class MediaFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int MODE_IMAGE = 0;

    public static final int MODE_VIDEO = 1;

    private Context context;

    private FragmentMediaBinding binding;

    /**
     * List of path images if {@link MediaFragment#MODE} is {@link MediaFragment#MODE_IMAGE} <br>
     * List of path videos if {@link MediaFragment#MODE} is {@link MediaFragment#MODE_VIDEO}
     */
    private ArrayList<String> mediaPath;

    private RcvMediaAdapter rcvMediaAdapter;

    private boolean isRunning;

    /**
     * Max item will be load more each loading
     */
    private static final int MAX_ITEM = 50;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * This is real data will be load into RecycleView, it will get more item from {@link MediaFragment#mediaPath} to make "Load More"
     */
    private ArrayList<String> mediaPathTemp;

    private int SIZE_CHECK;

    private int MODE;

    public MediaFragment() {
        // Required empty public constructor
    }

    public MediaFragment(int MODE) {
        this.MODE = MODE;

    }

    public RcvMediaAdapter getRcvMediaAdapter() {
        return rcvMediaAdapter;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public ArrayList<String> getMediaPath() {
        return mediaPath;
    }


    /**
     * Receive data from outside
     * @param mediaPath is {@link MediaFragment#mediaPath}
     */
    public void setMediaPath(ArrayList<String> mediaPath) {
        this.mediaPath = mediaPath;
    }

    public static MediaFragment newInstance(String param1, String param2) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentMediaBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = binding.getRoot();
        addControl();
        addEvents();
        return view;
    }

    private void addEvents() {
        binding.rcvMedia.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    doLoadMore();
                }
            }
        });
    }

    /**
     * This method will do "Load More" task if application need
     */
    @SuppressLint("NotifyDataSetChanged")
    private void doLoadMore() {
        SIZE_CHECK = mediaPathTemp.size();
        for(int i = rcvMediaAdapter.getItemCount()-1; i < rcvMediaAdapter.getItemCount()-1 + MAX_ITEM; i++){
            if(i == mediaPath.size()){
                break;
            }
            try{
                mediaPathTemp.add(mediaPath.get(i));
            }catch (Exception ignore){
                i++;
            }

        }
        if(mediaPathTemp.size() != SIZE_CHECK){
            rcvMediaAdapter.notifyDataSetChanged();
        }
    }

    private void addControl() {
        if(mediaPath == null){
            mediaPath = new ArrayList<>();
        }
        mediaPathTemp = new ArrayList<>();
        //First time load item, it will be load maximum MAX_ITEM item
        for(int i=0; i< MAX_ITEM; i++){
            if(i == mediaPath.size()){
                break;
            }else{
                try{
                    mediaPathTemp.add(mediaPath.get(i));
                }catch (Exception ignore){
                    i++;
                }
            }
        }
        rcvMediaAdapter = new RcvMediaAdapter(mediaPathTemp, MODE);
        binding.rcvMedia.setHasFixedSize(true);
        binding.rcvMedia.setLayoutManager(new GridLayoutManager(context, 4));
        binding.rcvMedia.setItemViewCacheSize(50);
        binding.rcvMedia.setAdapter(rcvMediaAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}