package com.wh.mydeskclock.Panel.Sticky;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wh.mydeskclock.DataBase.Sticky;
import com.wh.mydeskclock.MainActivity;
import com.wh.mydeskclock.R;

import java.util.List;

public class PanelStickyFragment extends Fragment {
    private StickyViewModel stickyViewModel;
    private RecyclerView recyclerView;
    private StickyAdapter stickyAdapter;
    private List<Sticky> allStickies;
    private LiveData<List<Sticky>> allStickiesLive;

    private MainActivity mParent;


    public PanelStickyFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel_sticky, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stickyViewModel = ViewModelProviders.of(mParent).get(StickyViewModel.class);
        recyclerView = mParent.findViewById(R.id.sticky_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mParent));
        stickyAdapter = new StickyAdapter(stickyViewModel);
        recyclerView.setAdapter(stickyAdapter);
        allStickiesLive = stickyViewModel.getAllStickiesLive();
        allStickiesLive.observe(getViewLifecycleOwner(), new Observer<List<Sticky>>() {
            @Override
            public void onChanged(List<Sticky> stickies) {
                int count = stickyAdapter.getItemCount();
                allStickies = stickies;
                if(count != stickies.size()){
                    stickyAdapter.submitList(stickies);
                }
            }
        });

//        Sticky sticky = new Sticky();
//        sticky.setStickyCon("this is a sticky");
//        sticky.setStickyTitle("this is a  title");
//        sticky.setStickyCreateTime("2020-02-20 20:20");
//        stickyViewModel.insertStickies(sticky);
    }
}
