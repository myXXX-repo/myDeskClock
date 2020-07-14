package com.wh.mydeskclock.app.notify;

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
    void insert(Notify... notifies);

    @Update
    void update(Notify... notifies);

    @Delete
    void delete(Notify... notifies);

    @Query("DELETE FROM Notify")
    void deleteAll();

    @Query("SELECT * FROM Notify ORDER BY ID DESC")
    LiveData<List<Notify>> getAllLive();

    @Query("SELECT * FROM Notify WHERE done = 0 ORDER BY ID DESC")
    LiveData<List<Notify>> getAllNotDoneLive();

    @Query("SELECT * FROM Notify WHERE id = :notifyId")
    Notify getById(int notifyId);

    @Query("SELECT * FROM Notify ORDER BY ID DESC")
    List<Notify> getAll();
}