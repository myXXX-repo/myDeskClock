package com.wh.mydeskclock.share;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShareRepo {
    private ShareDao dao;
    private LiveData<List<Share>> allLive;
    private LiveData<List<Share>> allUndoneLive;

    public ShareRepo(Context context) {
        ShareDatabase shareDatabase = ShareDatabase.getDatabase(context.getApplicationContext());
        dao = shareDatabase.getDao();
        allLive = dao.getAllLive();
        allUndoneLive = dao.getAllUndoneLive();
    }

    public void insert(Share... items) {
        new InsertAsyncTask(dao).execute(items);
    }

    public void update(Share... items) {
        new UpdateAsyncTask(dao).execute(items);
    }

    public void delete(Share... items) {
        new DeleteAsyncTask(dao).execute(items);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    public List<Share> getAll() {
        return dao.getAll();
    }

    public Share getById(int id) {
        return dao.getById(id);
    }

    public List<Share> getByType(String type) {
        return dao.getByType(type);
    }

    public List<Share> getByTypeUnDone(String type) {
        return dao.getByTypeUnDone(type);
    }

    public Share getUnDoneWithToken(String token){
        return dao.getUnDoneWithToken(token);
    }

    public List<Share> getAllUndone() {
        return dao.getNotDoneAll();
    }

    public LiveData<List<Share>> getAllLive(){return allLive;}

    public LiveData<List<Share>> getAllUndoneLive(){return allUndoneLive;}

    static class InsertAsyncTask extends AsyncTask<Share, Void, Void> {
        private ShareDao dao;

        InsertAsyncTask(ShareDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Share... items) {
            dao.insert(items);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Share, Void, Void> {
        private ShareDao dao;

        UpdateAsyncTask(ShareDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Share... items) {
            dao.update(items);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Share, Void, Void> {
        private ShareDao dao;

        DeleteAsyncTask(ShareDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Share... items) {
            dao.delete(items);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShareDao dao;

        DeleteAllAsyncTask(ShareDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
