package com.wh.mydeskclock;

import android.content.Context;
import android.util.Log;

import com.wh.mydeskclock.NotifyNode.Notify;
import com.wh.mydeskclock.NotifyNode.NotifyRepository;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class Controller {
    String TAG= "WH_"+ Controller.class.getSimpleName();


    @GetMapping("/notify")
    public String notify_new(
            final Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false) final String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false) final String TITLE,
            @RequestParam(value = "notify",defaultValue = "blank notify") final String NOTIFY){
        Log.d(TAG, "notify_new: new notify");
        NotifyRepository notifyRepository = new NotifyRepository(context);
        notifyRepository.insertNotifies(new Notify(NOTIFY,TITLE,DEVICE));
        return "notify recvd";
    }
}