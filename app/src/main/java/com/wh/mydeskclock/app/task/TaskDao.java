package com.wh.mydeskclock.app.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task... tasks);

    @Update
    void update(Task... tasks);

    @Delete
    void delete(Task... tasks);

    @Query("DELETE FROM Task")
    void deleteAll();

    @Query("SELECT * FROM Task ORDER BY ID DESC")
    LiveData<List<Task>> getAllLive();

    @Query("SELECT * FROM Task WHERE done = 0 ORDER BY ID DESC")
    LiveData<List<Task>> getAllNotDoneLive();

    @Query("SELECT * FROM Task WHERE id = :taskId")
    Task getById(int taskId);

    @Query("SELECT * FROM Task ORDER BY ID DESC")
    List<Task> getAll();
}