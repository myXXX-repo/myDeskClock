package com.wh.mydeskclock.TaskNode;

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

    @Query("DELETE FROM Notify")
    void deleteAll();

    @Query("SELECT * FROM Notify ORDER BY ID DESC")
    LiveData<List<Task>> getAllLive();

//    @Query("SELECT * FROM Notify ORDER BY ID DESC")
//    List<Task> getAll();
}