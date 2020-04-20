package com.wh.mydeskclock.Panel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wh.mydeskclock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelDashBoardFragment extends Fragment {

    public PanelDashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel_dashboard, container, false);
    }
}
