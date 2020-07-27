package com.wh.mydeskclock.app.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wh.mydeskclock.App;

import java.util.List;

public class TaskListViewModel extends AndroidViewModel {
//    private TaskRepository taskRepository;
    public TaskListViewModel(@NonNull Application application) {
        super(application);
//        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getAllLive() {
        return App.taskRepository.getAllLive();
    }
    public LiveData<List<Task>> getAllNotDoneLive() {
        return App.taskRepository.getAllNotDoneLive();
    }

    public void insert(Task... tasks) {
        App.taskRepository.insert(tasks);
    }

    void update(Task... tasks){
        App.taskRepository.update(tasks);
    }

    void delete(Task... tasks){
        App.taskRepository.delete(tasks);
    }

    public void deleteAll() {
        App.taskRepository.deleteAll();
    }
}
