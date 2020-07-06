package com.wh.mydeskclock.NotifyNode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NotifyListViewModel extends AndroidViewModel {
    private NotifyRepository notifyRepository;
    public NotifyListViewModel(@NonNull Application application) {
        super(application);
        notifyRepository = new NotifyRepository(application);
    }

    public LiveData<List<Notify>> getAllNotifiesLive() {
        return notifyRepository.getAllNotifyLive();
    }

    public void insertNotifies(Notify... notifies) {
        notifyRepository.insertNotifies(notifies);
    }

    void updateNotifies(Notify... notifies){
        notifyRepository.updateNotifies(notifies);
    }

    void deleteNotifies(Notify... notifies){
        notifyRepository.deleteNotifies(notifies);
    }

    public void deleteAllNotifies() {
        notifyRepository.deleteAllNotifies();
    }
}
