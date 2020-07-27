package com.wh.mydeskclock.app.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wh.mydeskclock.BaseApp;

import java.util.List;

public class TaskListViewModel extends AndroidViewModel {
//    private TaskRepository taskRepository;
    public TaskListViewModel(@NonNull Application application) {
        super(application);
//        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getAllLive() {
        return BaseApp.taskRepository.getAllLive();
    }
    public LiveData<List<Task>> getAllNotDoneLive() {
        return BaseApp.taskRepository.getAllNotDoneLive();
    }

    public void insert(Task... tasks) {
        BaseApp.taskRepository.insert(tasks);
    }

    void update(Task... tasks){
        BaseApp.taskRepository.update(tasks);
    }

    void delete(Task... tasks){
        BaseApp.taskRepository.delete(tasks);
    }

    public void deleteAll() {
        BaseApp.taskRepository.deleteAll();
    }
}
