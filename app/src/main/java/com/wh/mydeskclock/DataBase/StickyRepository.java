package com.wh.mydeskclock.DataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StickyRepository {
    private LiveData<List<Sticky>> allStickiesLive;
    private StickyDao stickyDao;

    public StickyRepository(Context context){
        StickyDatabase stickyDatabase = StickyDatabase.getDatabase(context.getApplicationContext());
        stickyDao = stickyDatabase.getStickyDao();
        allStickiesLive = stickyDao.getAllStickiesLive();
    }
    public void insertStickies(Sticky... stickies){
        new InsertAsyncTask(stickyDao).execute(stickies);
    }
    public void updateStickies(Sticky... stickies){
        new UpdateAsyncTask(stickyDao).execute(stickies);
    }
    public void deleteStickies(Sticky... stickies){
        new DeleteAsyncTask(stickyDao).execute(stickies);
    }
    public void deleteAllStickies(){
        new DeleteAllAsyncTask(stickyDao).execute();
    }

    public LiveData<List<Sticky>> getAllStickiesLive(){
        return allStickiesLive;
    }

    static class InsertAsyncTask extends AsyncTask<Sticky,Void,Void>{
        private StickyDao stickyDao;
        InsertAsyncTask(StickyDao stickyDao){
            this.stickyDao = stickyDao;
        }
        @Override
        protected Void doInBackground(Sticky... stickies) {
            stickyDao.insertStickies(stickies);
            return null;
        }
    }
    static class UpdateAsyncTask extends AsyncTask<Sticky,Void,Void>{
        private StickyDao stickyDao;
        UpdateAsyncTask(StickyDao stickyDao){
            this.stickyDao = stickyDao;
        }
        @Override
        protected Void doInBackground(Sticky... stickies) {
            stickyDao.updateStickies(stickies);
            return null;
        }
    }
    static class DeleteAsyncTask extends AsyncTask<Sticky,Void,Void>{
        private StickyDao stickyDao;
        DeleteAsyncTask(StickyDao stickyDao){
            this.stickyDao = stickyDao;
        }
        @Override
        protected Void doInBackground(Sticky... stickies) {
            stickyDao.deleteStickies(stickies);
            return null;
        }
    }
    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private StickyDao stickyDao;
        DeleteAllAsyncTask(StickyDao stickyDao){
            this.stickyDao = stickyDao;
        }
        @Override
        protected Void doInBackground(Void... stickies) {
            stickyDao.deleteAllStickies();
            return null;
        }
    }
}
