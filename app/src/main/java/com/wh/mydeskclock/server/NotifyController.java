package com.wh.mydeskclock.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.app.notify.Notify;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

@RestController
@RequestMapping("/notify")
public class NotifyController {
//    private NotifyRepository notifyRepository;

    String TAG = "WH_" + NotifyController.class.getSimpleName();

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String notify_new(final Context context,
                             @RequestParam(value = "device", defaultValue = "default device", required = false) final String DEVICE,
                             @RequestParam(value = "title", defaultValue = "default title", required = false) final String TITLE,
                             @RequestParam(value = "notify", defaultValue = "blank notify") final String NOTIFY) {
        Bundle bundle = new Bundle();
        bundle.putString("DEVICE", DEVICE);
        bundle.putString("TITLE", TITLE);
        bundle.putString("NOTIFY", NOTIFY);
        Intent intent = new Intent();
        intent.setAction("showNotify");
        intent.putExtra("extra", bundle);
        context.sendBroadcast(intent);

//        if (notifyRepository == null) {
//            notifyRepository = new NotifyRepository(context);
//        }

        BaseApp.notifyRepository.insert(new Notify(NOTIFY, TITLE, DEVICE));
        return ReturnDataUtils.successfulJson("notify received");
    }

    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String notify_get_all(final Context context) {

//        if (notifyRepository == null) {
//            notifyRepository = new NotifyRepository(context);
//        }

        return ReturnDataUtils.successfulJson(BaseApp.notifyRepository.getAll());
    }


}
