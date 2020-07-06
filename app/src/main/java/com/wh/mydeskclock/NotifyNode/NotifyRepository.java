package com.wh.mydeskclock.NotifyNode;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotifyRepository {
    private LiveData<List<Notify>> allNotifiesLive;
    private NotifyDao notifyDao;

    public NotifyRepository(Context context){
        NotifyDatabase notifyDatabase = NotifyDatabase.getDatabase(context.getApplicationContext());
        notifyDao = notifyDatabase.getNotifyDao();
        allNotifiesLive = notifyDao.getAllNotifiesLive();
    }
    public void insertNotifies(Notify... notifies){
        new InsertAsyncTask(notifyDao).execute(notifies);
    }
    public void updateNotifies(Notify... notifies){
        new UpdateAsyncTask(notifyDao).execute(notifies);
    }
    public void deleteNotifies(Notify... notifies){
        new DeleteAsyncTask(notifyDao).execute(notifies);
    }
    public void deleteAllNotifies(){
        new DeleteAllAsyncTask(notifyDao).execute();
    }

    public LiveData<List<Notify>> getAllNotifyLive(){
        return allNotifiesLive;
    }

    public List<Notify> getAllNotifies(){return notifyDao.getAllNotifies();}

    static class InsertAsyncTask extends AsyncTask<Notify,Void,Void>{
        private NotifyDao notifyDao;
        InsertAsyncTask(NotifyDao notifyDao){
            this.notifyDao = notifyDao;
        }
        @Override
        protected Void doInBackground(Notify... notifies) {
            notifyDao.insertNotifies(notifies);
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
            notifyDao.updateNotifies(notifies);
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
            notifyDao.deleteNotifies(notifies);
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
            notifyDao.deleteAllNotifies();
            return null;
        }
    }
}
