package com.wh.mydeskclock.app.sticky;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Sticky.class},version = 1,exportSchema = false)
public abstract class StickyDatabase extends RoomDatabase {
    private static StickyDatabase INSTANCE;
    static synchronized StickyDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    StickyDatabase.class,"sticky_database")
                    .build();
        }
        return INSTANCE;
    }
    public abstract StickyDao getDao();
}