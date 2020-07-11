package com.wh.mydeskclock.app.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskListViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    public TaskListViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getAllLive() {
        return taskRepository.getAllLive();
    }
    public LiveData<List<Task>> getAllNotDoneLive() {
        return taskRepository.getAllNotDoneLive();
    }

    public void insert(Task... tasks) {
        taskRepository.insert(tasks);
    }

    void update(Task... tasks){
        taskRepository.update(tasks);
    }

    void delete(Task... tasks){
        taskRepository.delete(tasks);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
