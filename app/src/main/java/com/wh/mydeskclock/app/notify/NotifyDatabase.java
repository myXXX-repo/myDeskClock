package com.wh.mydeskclock.app.notify;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Notify.class},version = 1,exportSchema = false)
public abstract class NotifyDatabase extends RoomDatabase {
    private static NotifyDatabase INSTANCE;
    static synchronized NotifyDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NotifyDatabase.class,"notify_database")
                    .build();
        }
        return INSTANCE;
    }
    public abstract NotifyDao getDao();
}
