package com.wh.mydeskclock.app.notify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ApiNode;
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

    public NotifyController() {
        MainServer.apiList.add(new ApiNode(
                "notify",
                "/notify",
                "http://ip:port/notify?notify=123",
                "用来向myDC发送notify",
                "GET",
                "",
                "title device notify 均为字符串,notify为必须"
        ));
        MainServer.apiList.add(new ApiNode(
                "notify",
                "/notify/get/all",
                "http://ip:port/notify/get/all",
                "用来获取全部notify内容",
                "GET",
                "",
                ""
        ));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String notify_new(final Context context,
                             @RequestParam(value = "device", defaultValue = "default device", required = false) final String DEVICE,
                             @RequestParam(value = "title", defaultValue = "default title", required = false) final String TITLE,
                             @RequestParam(value = "notify", defaultValue = "blank notify") final String NOTIFY,
                             @RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
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
