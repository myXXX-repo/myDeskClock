package com.wh.mydeskclock;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wh.mydeskclock.Utils.MediaUtils;

public class MediaCtrlFragment extends Fragment {
    private String TAG= "WH_"+MediaCtrlFragment.class.getSimpleName();

    private ImageView iv_previous;
    private ImageView iv_play;
    private ImageView iv_next;
//    private AudioManager audioManager;

    int tag_iv_play = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        audioManager = (AudioManager)requireContext().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_ctrl, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        iv_previous = requireActivity().findViewById(R.id.iv_previous);
        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaUtils.previousPlay();
            }
        });
        iv_play = requireActivity().findViewById(R.id.iv_play);
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaUtils.pausePlay();
            }
        });
        iv_next = requireActivity().findViewById(R.id.iv_next);
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final MyHandler myHandler = new MyHandler(iv_play);
                MediaUtils.nextPlay();
//                if(tag_iv_play%2==0){
//                    iv_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
//                }else {
//                    iv_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24));
//                }
//                tag_iv_play+=1;
//                new Thread(){
//                    @Override
//                    public void run() {
//                        try {
//                            sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //TODO 检测是否在播放音乐
//                        Log.d(TAG, "run: "+audioManager.isMusicActive());
//                        if(audioManager.isMusicActive()){
//                            myHandler.sendEmptyMessage(MyHandler.WHAT_SET_IMAGE_PAUSE);
//                        }else {
//                            myHandler.sendEmptyMessage(MyHandler.WHAT_SET_IMAGE_PLAY);
//                        }
//                    }
//                }.start();
            }
        });
    }

    static class MyHandler extends Handler{
        private static final int WHAT_SET_IMAGE_PLAY = 621;
        private static final int WHAT_SET_IMAGE_PAUSE = 887;

        ImageView iv_play;

        public MyHandler(ImageView iv_play) {
            this.iv_play = iv_play;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case WHAT_SET_IMAGE_PLAY:{
                    iv_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    break;
                }
                case WHAT_SET_IMAGE_PAUSE:{
                    iv_play.setImageResource(R.drawable.ic_baseline_pause_24);
                    break;
                }
            }
        }
    }
}