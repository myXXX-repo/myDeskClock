package com.wh.mydeskclock.app.mediaCtrl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.MediaUtils;
import com.wh.mydeskclock.utils.Utils;

import java.util.Objects;

public class MediaCtrlFragment extends Fragment {
    private String TAG = "WH_" + MediaCtrlFragment.class.getSimpleName();

    private ConstraintLayout cl_media_ctrl_info;
    private boolean isCLShow = true;
    private boolean SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO;
    private ImageView iv_mini_mode;
    private TextView tv_media_info_title;
    private TextView tv_media_info_artist;
    private TextView tv_media_info_album;

    private BroadcastReceiver music_broadcastReceiver;
    private ImageView iv_play;

    public static String ARTIST;
    public static String TRACK;
    public static String ALBUM;
    public static boolean PLAYING;
    public static long ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO, false);
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

        tv_media_info_album = requireActivity().findViewById(R.id.tv_media_info_album);
        tv_media_info_artist = requireActivity().findViewById(R.id.tv_media_info_artist);
        tv_media_info_title = requireActivity().findViewById(R.id.tv_media_info_title);

        ImageView iv_previous = requireActivity().findViewById(R.id.iv_previous);
        if (iv_previous != null) {
            iv_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaUtils.previousPlay();
                }
            });
        }

        iv_play = requireActivity().findViewById(R.id.iv_play);
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

        music_broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: ACTION" + intent.getAction());

                String ACTION = intent.getAction();
                if (Objects.equals(intent.getAction(), "com.amazon.mp3.metachanged")) {
                    ARTIST = intent.getStringExtra("com.amazon.mp3.artist");
                    TRACK = intent.getStringExtra("com.amazon.mp3.track");
                } else {
                    ARTIST = intent.getStringExtra("artist");
                    TRACK = intent.getStringExtra("track");
                }
                ALBUM = intent.getStringExtra("album");
                PLAYING = intent.getBooleanExtra("playing",false);
                ID = intent.getLongExtra("id",-1);

                tv_media_info_artist.setText(ARTIST);
                tv_media_info_album.setText(ALBUM);
                tv_media_info_title.setText(TRACK);
                if(PLAYING){
                    iv_play.setImageResource(R.drawable.ic_baseline_pause_24);
                }else {
                    iv_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }


//                Bundle bundle = intent.getExtras();
//                if (bundle != null) {
//                    Utils.unPackBundle(bundle,TAG);
//                }

            }
        };
        IntentFilter intentFilter_music = new IntentFilter();
        intentFilter_music.addAction("com.android.music.metachanged");
        intentFilter_music.addAction("com.android.music.com.android.music.musicservicecommand");
        intentFilter_music.addAction("com.android.music.playstatechanged");
        intentFilter_music.addAction("com.android.music.playbackcomplete");
        intentFilter_music.addAction("com.android.music.queuechanged");
        intentFilter_music.addAction("com.android.music.metachanged");
        intentFilter_music.addAction("com.htc.music.metachanged");
        intentFilter_music.addAction("fm.last.android.metachanged");
        intentFilter_music.addAction("com.sec.android.app.music.metachanged");
        intentFilter_music.addAction("com.nullsoft.winamp.metachanged");
        intentFilter_music.addAction("com.amazon.mp3.metachanged");
        intentFilter_music.addAction("com.miui.player.metachanged");
        intentFilter_music.addAction("com.real.IMP.metachanged");
        intentFilter_music.addAction("com.sonyericsson.music.metachanged");
        intentFilter_music.addAction("com.rdio.android.metachanged");
        intentFilter_music.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter_music.addAction("com.andrew.apollo.metachanged");
        intentFilter_music.addAction("com.meizu.media.music");
//        intentFilter_music.addAction("com.android.music.queuechanged");
//        intentFilter_music.addAction("com.android.music.playbackcomplete");
//        intentFilter_music.addAction("com.android.music.playstatechanged");
        requireContext().registerReceiver(music_broadcastReceiver, intentFilter_music);
    }

    @Override
    public void onResume() {
        super.onResume();
        SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (music_broadcastReceiver != null) {
            requireContext().unregisterReceiver(music_broadcastReceiver);
        }
    }
}