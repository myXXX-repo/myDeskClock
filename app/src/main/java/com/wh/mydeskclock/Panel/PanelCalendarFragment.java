package com.wh.mydeskclock.Panel;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;


public class PanelCalendarFragment extends Fragment {
//    private CalendarView calendarView;
    private MainActivity mParent;


    public PanelCalendarFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    public static PanelCalendarFragment newInstance() {
        PanelCalendarFragment fragment = new PanelCalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        View view = getView();
//        if(view == null){
//            return;
//        }

//        calendarView = view.findViewById(R.id.calendarView);
    }
}
