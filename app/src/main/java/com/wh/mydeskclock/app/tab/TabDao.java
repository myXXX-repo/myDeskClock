package com.wh.mydeskclock.app.tab;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface TabDao {
    @Insert
    void insert(Tab... items);

    @Update
    void update(Tab... items);

    @Delete
    void delete(Tab... items);

    @Query("DELETE FROM Tab")
    void deleteAll();

    @Query("SELECT * FROM Tab WHERE id = :id")
    Tab getById(int id);

    @Query("SELECT * FROM Tab ORDER BY ID DESC")
    List<Tab> getAll();

    @Query("SELECT * FROM Tab WHERE Done = 0 ORDER BY ID DESC")
    List<Tab> getNotDoneAll();
}
