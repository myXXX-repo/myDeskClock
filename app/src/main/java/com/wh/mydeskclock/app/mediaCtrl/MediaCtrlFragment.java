package com.wh.mydeskclock.app.mediaCtrl;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.MediaUtils;

public class MediaCtrlFragment extends Fragment {
    private String TAG= "WH_"+MediaCtrlFragment.class.getSimpleName();

    private ConstraintLayout cl_media_ctrl_info;
    private boolean isCLShow = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_ctrl, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cl_media_ctrl_info = requireActivity().findViewById(R.id.cl_media_ctrl_info);

        ImageView iv_previous = requireActivity().findViewById(R.id.iv_previous);
        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaUtils.previousPlay();
            }
        });
        ImageView iv_play = requireActivity().findViewById(R.id.iv_play);
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaUtils.pausePlay();
            }
        });
        ImageView iv_next = requireActivity().findViewById(R.id.iv_next);
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaUtils.nextPlay();
            }
        });
        ImageView iv_mini_mode = requireActivity().findViewById(R.id.iv_mini_mode);
        iv_mini_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCLShow){
                    cl_media_ctrl_info.setVisibility(View.GONE);
                }else {
                    cl_media_ctrl_info.setVisibility(View.VISIBLE);
                }
                isCLShow=!isCLShow;
            }
        });
    }
}