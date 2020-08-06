package com.wh.mydeskclock.app.sticky;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wh.mydeskclock.app.task.Task;

import java.util.List;

@Dao
public interface StickyDao {
    @Insert
    void insert(Sticky... stickies);

    @Update
    void update(Sticky... stickies);

    @Delete
    void delete(Sticky... stickies);

    @Query("DELETE FROM Sticky")
    void deleteAll();

    @Query("SELECT * FROM Sticky ORDER BY ID DESC")
    LiveData<List<Sticky>> getAllLive();

    @Query("SELECT * FROM Sticky WHERE done = 0 ORDER BY ID DESC")
    LiveData<List<Sticky>> getAllNotDoneLive();

    @Query("SELECT * FROM Sticky WHERE id = :stickyId")
    Sticky getById(int stickyId);

    @Query("SELECT * FROM Sticky ORDER BY ID DESC")
    List<Sticky> getAll();

    @Query("SELECT * FROM Sticky WHERE done = 0 ORDER BY ID DESC")
    List<Sticky> getNotDoneAll();
}
