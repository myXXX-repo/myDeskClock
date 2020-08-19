package com.wh.mydeskclock.app.tab;

import android.util.Log;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.util.List;

@RestController
@RequestMapping("/tab")
public class TabController {
    String TAG = "WH_" + getClass().getSimpleName();

    public TabController() {
        MainServer.apiList.add(new ApiNode(
                "tab",
                "/tab/get/all",
                "http://ip:port/tab/get/all",
                "用来获取全部的tab列表",
                "GET",
                "",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "tab",
                "/tab/add",
                "http://ip:port/tab/add",
                "用来获取全部的tab列表",
                "GET",
                "",
                "con json数组字符串,数组元素内含title 和 url, device string类型"
        ));
    }

    /**
     * api 1
     *
     * @path /tab/get/all
     * @describe to get all tasks
     * @method GET
     */
    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_all(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        List<Tab> tabs = BaseApp.tabRepository.getAll();
        Log.d(TAG, "get_task_all: " + tabs.size());
        return ReturnDataUtils.successfulJson(tabs);
    }

    /**
     * api 2
     *
     * @path /tab/add
     * @describe to get all tabs
     * @method POST
     */
    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String add_with_post(
            @RequestParam(name = "con")String CON,
            @RequestParam(name = "device",required = false,defaultValue = "default device")String DEVICE,
            @RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN
    ){
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        Tab tab = new Tab(CON,DEVICE);
        BaseApp.tabRepository.insert(tab);
        return ReturnDataUtils.successfulJson("add new tabs done");
    }
}
