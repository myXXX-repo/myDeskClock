package com.wh.mydeskclock.Panel.DashBoard;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.Utils;

public class PanelDashBoardFragment extends Fragment {
    ImageButton previous, play, next;
    private MainActivity mParent;

    public PanelDashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            previous = view.findViewById(R.id.btn_play_previous);
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mParent, "previous", Toast.LENGTH_SHORT).show();
                    Utils.previousPlay();
                }
            });
            play = view.findViewById(R.id.btn_play);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mParent, "play", Toast.LENGTH_SHORT).show();
                    Utils.pausePlay();
                }
            });
            next = view.findViewById(R.id.btn_play_next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mParent, "next", Toast.LENGTH_SHORT).show();
                    Utils.nextPlay();
                }
            });
        }
    }


}
