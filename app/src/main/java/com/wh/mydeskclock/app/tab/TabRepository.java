package com.wh.mydeskclock.app.tab;

import android.content.Context;
import android.os.AsyncTask;


import java.util.List;

public class TabRepository {
    private TabDao dao;

    public TabRepository(Context context){
        TabDatabase tabDatabase = TabDatabase.getDatabase(context.getApplicationContext());
        dao = tabDatabase.getDao();
    }

    public void insert(Tab... items){
        new InsertAsyncTask(dao).execute(items);
    }
    public void update(Tab... items){
        new UpdateAsyncTask(dao).execute(items);
    }
    public void delete(Tab... items){
        new DeleteAsyncTask(dao).execute(items);
    }
    public void deleteAll(){
        new DeleteAllAsyncTask(dao).execute();
    }

    public List<Tab> getAll(){return dao.getAll();}

    static class InsertAsyncTask extends AsyncTask<Tab,Void,Void> {
        private TabDao dao;
        InsertAsyncTask(TabDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Tab... items) {
            dao.insert(items);
            return null;
        }
    }
    static class UpdateAsyncTask extends AsyncTask<Tab,Void,Void>{
        private TabDao dao;
        UpdateAsyncTask(TabDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Tab... items) {
            dao.update(items);
            return null;
        }
    }
    static class DeleteAsyncTask extends AsyncTask<Tab,Void,Void>{
        private TabDao dao;
        DeleteAsyncTask(TabDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Tab... items) {
            dao.delete(items);
            return null;
        }
    }
    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private TabDao dao;
        DeleteAllAsyncTask(TabDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
