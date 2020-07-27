package com.wh.mydeskclock.app.mediaCtrl;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wh.mydeskclock.App;
import com.wh.mydeskclock.SharedPreferenceUtils;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.MediaUtils;

public class MediaCtrlFragment extends Fragment {
    private String TAG = "WH_" + MediaCtrlFragment.class.getSimpleName();

    private ConstraintLayout cl_media_ctrl_info;
    private boolean isCLShow = true;
    private boolean SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO;
//    private SharedPreferences sharedPreferences;
    private ImageView iv_mini_mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO = App.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO, false);
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
        if (iv_previous != null) {
            iv_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaUtils.previousPlay();
                }
            });
        }

        ImageView iv_play = requireActivity().findViewById(R.id.iv_play);
        if (iv_play != null) {
            iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaUtils.pausePlay();
                }
            });
        }

        ImageView iv_next = requireActivity().findViewById(R.id.iv_next);
        if (iv_next != null) {
            iv_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaUtils.nextPlay();
                }
            });
        }

        iv_mini_mode = requireActivity().findViewById(R.id.iv_mini_mode);
        if (iv_mini_mode != null) {
            iv_mini_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isCLShow) {
                        cl_media_ctrl_info.setVisibility(View.GONE);
                    } else {
                        cl_media_ctrl_info.setVisibility(View.VISIBLE);
                    }
                    isCLShow = !isCLShow;
                }
            });

            if (SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO) {
                iv_mini_mode.setVisibility(View.VISIBLE);
            } else {
                iv_mini_mode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO = App.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO, false);
        if (SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO) {
            if (iv_mini_mode != null) {
                iv_mini_mode.setVisibility(View.VISIBLE);
            }
        } else {
            if (iv_mini_mode != null) {
                iv_mini_mode.setVisibility(View.GONE);
            }
        }
    }
}