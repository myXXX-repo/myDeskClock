package com.wh.mydeskclock.app.task;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private LiveData<List<Task>> allLive;
    private LiveData<List<Task>> allNotDoneLive;
    private TaskDao taskDao;

    public TaskRepository(Context context){
        TaskDatabase taskDatabase = TaskDatabase.getDatabase(context.getApplicationContext());
        taskDao = taskDatabase.getDao();
        allLive = taskDao.getAllLive();
        allNotDoneLive = taskDao.getAllNotDoneLive();
    }
    public void insert(Task... tasks){
        new InsertAsyncTask(taskDao).execute(tasks);
    }
    public void update(Task... tasks){
        new UpdateAsyncTask(taskDao).execute(tasks);
    }
    public void delete(Task... tasks){
        new DeleteAsyncTask(taskDao).execute(tasks);
    }
    public void deleteAll(){
        new DeleteAllAsyncTask(taskDao).execute();
    }
    public List<Task> getAll() {return taskDao.getAll();}
    public List<Task> getNotDoneAll(){return taskDao.getNotDoneAll();}
    public Task getById(int taskId) {return taskDao.getById(taskId);}

    public LiveData<List<Task>> getAllLive(){
        return allLive;
    }
    public LiveData<List<Task>> getAllNotDoneLive(){
        return allNotDoneLive;
    }

    static class InsertAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao;
        InsertAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks);
            return null;
        }
    }
    static class UpdateAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao;
        UpdateAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks);
            return null;
        }
    }
    static class DeleteAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao;
        DeleteAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks);
            return null;
        }
    }
    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private TaskDao taskDao;
        DeleteAllAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAll();
            return null;
        }
    }
}
