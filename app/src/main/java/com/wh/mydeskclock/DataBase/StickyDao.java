package com.wh.mydeskclock.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StickyDao {
    @Insert
    void insertStickies(Sticky... stickies);

    @Update
    void updateStickies(Sticky... stickies);

    @Delete
    void deleteStickies(Sticky... stickies);

    @Query("DELETE FROM STICKY")
    void deleteAllStickies();

    @Query("SELECT * FROM Sticky ORDER BY ID DESC")
    LiveData<List<Sticky>>getAllStickiesLive();
}
