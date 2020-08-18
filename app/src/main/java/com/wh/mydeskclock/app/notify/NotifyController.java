package com.wh.mydeskclock.app.notify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

@RestController
@RequestMapping("/notify")
public class NotifyController {

    String TAG = "WH_" + NotifyController.class.getSimpleName();

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String notify_new(final Context context,
                             @RequestParam(value = "device", defaultValue = "default device", required = false) final String DEVICE,
                             @RequestParam(value = "title", defaultValue = "default title", required = false) final String TITLE,
                             @RequestParam(value = "notify", defaultValue = "blank notify") final String NOTIFY,
                             @RequestHeader("access_token")String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        Bundle bundle = new Bundle();
        bundle.putString("DEVICE", DEVICE);
        bundle.putString("TITLE", TITLE);
        bundle.putString("NOTIFY", NOTIFY);
        Intent intent = new Intent();
        intent.setAction("showNotify");
        intent.putExtra("extra", bundle);
        context.sendBroadcast(intent);
        BaseApp.notifyRepository.insert(new Notify(NOTIFY, TITLE, DEVICE));
        return ReturnDataUtils.successfulJson("notify received");
    }

    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String notify_get_all(@RequestHeader("access_token")String ACCESS_TOKEN) {
        if(!ACCESS_TOKEN.equals(MainServer.access_token)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        return ReturnDataUtils.successfulJson(BaseApp.notifyRepository.getAll());
    }


}
