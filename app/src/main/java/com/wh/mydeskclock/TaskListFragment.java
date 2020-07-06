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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wh.mydeskclock.TaskNode.Task;
import com.wh.mydeskclock.TaskNode.TaskListAdapter;
import com.wh.mydeskclock.TaskNode.TaskListViewModel;

import java.util.List;

public class TaskListFragment extends Fragment {
    private TaskListViewModel taskListViewModel;
    private RecyclerView rv_notify;
    private TaskListAdapter taskListAdapter;
    private LiveData<List<Task>> allNotifiesLive;
    private List<Task> allNotifies;

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
        taskListViewModel = ViewModelProviders.of(requireActivity()).get(TaskListViewModel.class);
        rv_notify = requireActivity().findViewById(R.id.rv_notify);
        rv_notify.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskListAdapter = new TaskListAdapter(taskListViewModel,mParent);
        rv_notify.setAdapter(taskListAdapter);

        allNotifiesLive = taskListViewModel.getAllLive();
        allNotifiesLive.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> notifies) {
                int count = taskListAdapter.getItemCount();
                allNotifies = notifies;
                if(count!=notifies.size()){
                    taskListAdapter.submitList(notifies);
                }
            }
        });
    }
}