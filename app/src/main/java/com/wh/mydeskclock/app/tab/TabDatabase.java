package com.wh.mydeskclock.app.tab;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tab.class},version = 1,exportSchema = false)
public abstract class TabDatabase extends RoomDatabase {
    private static TabDatabase INSTANCE;
    static synchronized TabDatabase getDatabase(Context context){
        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TabDatabase.class,"tab_database")
                    .build();
        }
        return INSTANCE;
    }
    public abstract TabDao getDao();
}
