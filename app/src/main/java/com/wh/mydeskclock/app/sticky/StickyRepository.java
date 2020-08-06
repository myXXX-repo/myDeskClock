package com.wh.mydeskclock.app.sticky;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StickyRepository {

    private LiveData<List<Sticky>> allLive;
    private LiveData<List<Sticky>> allNotDoneLive;
    private StickyDao stickyDao;

    public StickyRepository(Context context) {
        StickyDatabase stickyDatabase = StickyDatabase.getDatabase(context.getApplicationContext());
        stickyDao = stickyDatabase.getDao();
        allLive = stickyDao.getAllLive();
        allNotDoneLive = stickyDao.getAllNotDoneLive();
    }

    public void insert(Sticky... items) {
        new StickyRepository.InsertAsyncTask(stickyDao).execute(items);
    }

    public void update(Sticky... items) {
        new StickyRepository.UpdateAsyncTask(stickyDao).execute(items);
    }

    public void delete(Sticky... items) {
        new StickyRepository.DeleteAsyncTask(stickyDao).execute(items);
    }

    public void deleteAll() {
        new StickyRepository.DeleteAllAsyncTask(stickyDao).execute();
    }

    public List<Sticky> getAll() {
        return stickyDao.getAll();
    }

    public List<Sticky> getNotDoneAll() {
        return stickyDao.getNotDoneAll();
    }

    public Sticky getById(int taskId) {
        return stickyDao.getById(taskId);
    }

    public LiveData<List<Sticky>> getAllLive() {
        return allLive;
    }

    public LiveData<List<Sticky>> getAllNotDoneLive() {
        return allNotDoneLive;
    }

    static class InsertAsyncTask extends AsyncTask<Sticky, Void, Void> {
        private StickyDao dao;

        InsertAsyncTask(StickyDao stickyDao) {
            this.dao = stickyDao;
        }

        @Override
        protected Void doInBackground(Sticky... stickies) {
            dao.insert(stickies);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Sticky, Void, Void> {
        private StickyDao dao;

        UpdateAsyncTask(StickyDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Sticky... stickies) {
            dao.update(stickies);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Sticky, Void, Void> {
        private StickyDao dao;

        DeleteAsyncTask(StickyDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Sticky... stickies) {
            dao.delete(stickies);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private StickyDao dao;

        DeleteAllAsyncTask(StickyDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

}
