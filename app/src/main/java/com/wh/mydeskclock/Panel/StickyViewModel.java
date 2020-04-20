package com.wh.mydeskclock.Panel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wh.mydeskclock.DataBase.Sticky;
import com.wh.mydeskclock.DataBase.StickyRepository;

import java.util.List;

public class StickyViewModel extends AndroidViewModel {
    private StickyRepository stickyRepository;
    public StickyViewModel(@NonNull Application application) {
        super(application);
        stickyRepository = new StickyRepository(application);
    }

    LiveData<List<Sticky>> getAllStickiesLive(){
        return stickyRepository.getAllStickiesLive();
    }

    void insertStickies(Sticky... stickies){
        stickyRepository.insertStickies(stickies);
    }

    void updateStickies(Sticky... stickies){
        stickyRepository.updateStickies(stickies);
    }

    void deleteStickies(Sticky... stickies){
        stickyRepository.deleteStickies(stickies);
    }

    void deleteAllStickies(){
        stickyRepository.deleteAllStickies();
    }
}
