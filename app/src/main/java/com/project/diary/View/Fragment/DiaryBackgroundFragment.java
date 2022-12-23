package com.project.diary.View.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.diary.Control.Adapter.BackgroundDiary.RcvDiaryBackgroundAdapter;
import com.project.diary.R;
import com.project.diary.databinding.FragmentDiaryBackgroundBinding;
import com.project.diary.databinding.FragmentMediaBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryBackgroundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryBackgroundFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    private FragmentDiaryBackgroundBinding binding;

    private RcvDiaryBackgroundAdapter rcvDiaryBackgroundAdapter;

    private int position;
    
    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiaryBackgroundFragment() {
        // Required empty public constructor
    }

    public DiaryBackgroundFragment(Bundle args) {
        this.position = args.getInt("POSITION");
        System.out.println("Nhan: " + position);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryBackgroundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryBackgroundFragment newInstance(String param1, String param2) {
        DiaryBackgroundFragment fragment = new DiaryBackgroundFragment();
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
        binding = FragmentDiaryBackgroundBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = binding.getRoot();
        Bundle args = getArguments();
        this.position = args.getInt("POSITION");
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {

    }

    private void addControls() {
        rcvDiaryBackgroundAdapter = new RcvDiaryBackgroundAdapter(context, position);
        binding.rcvDiaryBackground.setLayoutManager(new GridLayoutManager(context, 4));
        binding.rcvDiaryBackground.setHasFixedSize(true);
        binding.rcvDiaryBackground.setAdapter(rcvDiaryBackgroundAdapter);
    }
}