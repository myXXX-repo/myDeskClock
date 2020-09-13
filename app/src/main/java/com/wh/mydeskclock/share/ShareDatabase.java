package com.wh.mydeskclock.share;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Share.class},version = 1,exportSchema = false)
public abstract class ShareDatabase extends RoomDatabase {

    private static ShareDatabase INSTANCE;
    static synchronized ShareDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ShareDatabase.class,"share_database")
                    .build();
        }
        return INSTANCE;
    }
    public abstract ShareDao getDao();
}
