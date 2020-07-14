package com.wh.mydeskclock.app.notify;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotifyRepository {
    private LiveData<List<Notify>> allLive;
    private LiveData<List<Notify>> allNotDoneLive;
    private NotifyDao notifyDao;

    public NotifyRepository(Context context){
        NotifyDatabase notifyDatabase = NotifyDatabase.getDatabase(context.getApplicationContext());
        notifyDao = notifyDatabase.getDao();
        allLive = notifyDao.getAllLive();
        allNotDoneLive = notifyDao.getAllNotDoneLive();
    }
    public void insert(Notify... notifies){
        new InsertAsyncTask(notifyDao).execute(notifies);
    }
    public void update(Notify... notifies){
        new UpdateAsyncTask(notifyDao).execute(notifies);
    }
    public void delete(Notify... notifies){
        new DeleteAsyncTask(notifyDao).execute(notifies);
    }
    public void deleteAll(){
        new DeleteAllAsyncTask(notifyDao).execute();
    }
    public List<Notify> getAll() {return notifyDao.getAll();}
    public Notify getById(int taskId) {return notifyDao.getById(taskId);}

    public LiveData<List<Notify>> getAllLive(){
        return allLive;
    }
    public LiveData<List<Notify>> getAllNotDoneLive(){
        return allNotDoneLive;
    }

    static class InsertAsyncTask extends AsyncTask<Notify,Void,Void>{
        private NotifyDao notifyDao;
        InsertAsyncTask(NotifyDao notifyDao){
            this.notifyDao = notifyDao;
        }
        @Override
        protected Void doInBackground(Notify... notifies) {
            notifyDao.insert(notifies);
            return null;
        }
    }
    static class UpdateAsyncTask extends AsyncTask<Notify,Void,Void>{
        private NotifyDao notifyDao;
        UpdateAsyncTask(NotifyDao notifyDao){
            this.notifyDao = notifyDao;
        }
        @Override
        protected Void doInBackground(Notify... notifies) {
            notifyDao.update(notifies);
            return null;
        }
    }
    static class DeleteAsyncTask extends AsyncTask<Notify,Void,Void>{
        private NotifyDao notifyDao;
        DeleteAsyncTask(NotifyDao notifyDao){
            this.notifyDao = notifyDao;
        }
        @Override
        protected Void doInBackground(Notify... notifies) {
            notifyDao.delete(notifies);
            return null;
        }
    }
    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private NotifyDao notifyDao;
        DeleteAllAsyncTask(NotifyDao notifyDao){
            this.notifyDao = notifyDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            notifyDao.deleteAll();
            return null;
        }
    }
}
