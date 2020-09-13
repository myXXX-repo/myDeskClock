package com.wh.mydeskclock.share;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShareDao {
    @Insert
    void insert(Share... items);

    @Update
    void update(Share... items);

    @Delete
    void delete(Share... items);

    @Query("DELETE FROM Share")
    void deleteAll();

    @Query("SELECT * FROM Share ORDER BY ID DESC")
    LiveData<List<Share>> getAllLive();

    @Query("SELECT * FROM Share WHERE done = 0 ORDER BY ID DESC")
    LiveData<List<Share>> getAllNotDoneLive();

    @Query("SELECT * FROM Share WHERE id = :shareId")
    Share getById(int shareId);

    @Query("SELECT * FROM Share ORDER BY ID DESC")
    List<Share> getAll();

    @Query("SELECT * FROM Share WHERE done = 0 ORDER BY ID DESC")
    List<Share> getNotDoneAll();

    @Query("SELECT * FROM Share WHERE done = 0 ORDER BY ID DESC")
    LiveData<List<Share>> getAllUndoneLive();

    @Query("SELECT * FROM Share WHERE type = :shareType ORDER BY ID DESC")
    List<Share> getByType(String shareType);

    @Query("SELECT * FROM Share WHERE type = :shareType AND done = 0 ORDER BY ID DESC")
    List<Share> getByTypeUnDone(String shareType);

    @Query("SELECT * FROM Share WHERE token = :shareToken AND done = 0")
    Share getUnDoneWithToken(String shareToken);
}
