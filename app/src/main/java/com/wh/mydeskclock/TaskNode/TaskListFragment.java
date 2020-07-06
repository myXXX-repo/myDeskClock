package com.wh.mydeskclock.TaskNode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.wh.mydeskclock.R;

import java.util.List;

public class TaskListFragment extends Fragment {
    private TaskListViewModel taskListViewModel;
    private RecyclerView rv_notify;
    private TaskListAdapter taskListAdapter;
    private List<Task> allTasks;

    private AppCompatActivity mParent;

    private BroadcastReceiver taskReceiver;

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
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskListViewModel = ViewModelProviders.of(requireActivity()).get(TaskListViewModel.class);
        rv_notify = requireActivity().findViewById(R.id.rv_task);
        rv_notify.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskListAdapter = new TaskListAdapter(taskListViewModel,mParent);
        rv_notify.setAdapter(taskListAdapter);

        LiveData<List<Task>> allTasksLive = taskListViewModel.getAllLive();
        allTasksLive.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                int count = taskListAdapter.getItemCount();
                allTasks = tasks;
                if(count!= allTasks.size()){
                    taskListAdapter.submitList(tasks);
                    rv_notify.smoothScrollBy(0,-200);
                }
            }
        });

        taskReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra("extra");
                if(bundle!=null){
                    String DEVICE = bundle.getString("DEVICE");
                    String TITLE = bundle.getString("TITLE");
                    String TASK = bundle.getString("TASK");
                    Task task = new Task(TASK,TITLE,DEVICE);
                    taskListViewModel.insert(task);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendTask");
        requireContext().registerReceiver(taskReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(taskReceiver!=null){
            requireContext().unregisterReceiver(taskReceiver);
        }
    }
}