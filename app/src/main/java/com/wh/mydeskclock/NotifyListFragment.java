package com.wh.mydeskclock;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wh.mydeskclock.NotifyNode.Notify;
import com.wh.mydeskclock.NotifyNode.NotifyListAdapter;
import com.wh.mydeskclock.NotifyNode.NotifyListViewModel;

import java.util.List;

public class NotifyListFragment extends Fragment {
    private NotifyListViewModel notifyListViewModel;
    private RecyclerView rv_notify;
    private NotifyListAdapter notifyListAdapter;
    private LiveData<List<Notify>> allNotifiesLive;
    private List<Notify> allNotifies;

    private AppCompatActivity mParent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notifyListViewModel = ViewModelProviders.of(requireActivity()).get(NotifyListViewModel.class);
        rv_notify = requireActivity().findViewById(R.id.rv_notify);
        rv_notify.setLayoutManager(new LinearLayoutManager(requireContext()));
        notifyListAdapter = new NotifyListAdapter(notifyListViewModel,mParent);
        rv_notify.setAdapter(notifyListAdapter);

        allNotifiesLive = notifyListViewModel.getAllNotifiesLive();
        allNotifiesLive.observe(getViewLifecycleOwner(), new Observer<List<Notify>>() {
            @Override
            public void onChanged(List<Notify> notifies) {
                int count = notifyListAdapter.getItemCount();
                allNotifies = notifies;
                if(count!=notifies.size()){
                    notifyListAdapter.submitList(notifies);
                }
            }
        });
    }
}