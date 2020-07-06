package com.wh.mydeskclock.NotifyNode;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotifyDao {
    @Insert
    void insertNotifies(Notify... notifies);

    @Update
    void updateNotifies(Notify... notifies);

    @Delete
    void deleteNotifies(Notify... notifies);

    @Query("DELETE FROM Notify")
    void deleteAllNotifies();

    @Query("SELECT * FROM Notify ORDER BY ID DESC")
    LiveData<List<Notify>> getAllNotifiesLive();

    @Query("SELECT * FROM Notify ORDER BY ID DESC")
    List<Notify> getAllNotifies();
}