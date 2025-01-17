package com.wh.mydeskclock.app.sticky;

import android.util.Log;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.util.List;

@RestController
@RequestMapping("/sticky")
public class StickyController {
    String TAG = "WH_" + getClass().getSimpleName();

    public StickyController() {
        MainServer.apiList.add(new ApiNode(
                "sticky",
                "/sticky/get/{stickyId}",
                "http://ip:port/sticky/11",
                "用来获取指定id的sticky",
                "GET",
                "stickyId int类型",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "sticky",
                "/sticky/get/all",
                "http://ip:port/sticky/get/all",
                "用来获取全部sticky内容",
                "GET",
                "",
                ""
        ));
        MainServer.apiList.add(new ApiNode(
                "sticky",
                "/sticky/add",
                "http://ip:port/sticky/add",
                "用来创建一个sticky",
                "POST",
                "",
                "title sticky device 均为string类型"
        ));
    }

    /**
     * api 0
     *
     * @path /sticky/get/{taskId}
     * @describe to get sticky by id
     * @method GET
     */
    @GetMapping(path = "/get/{stickyId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_sticky_id(@PathVariable("stickyId") int stickyId,
                         @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        Sticky sticky = BaseApp.stickyRepository.getById(stickyId);
        return ReturnDataUtils.successfulJson(sticky);
    }

    /**
     * api 1
     *
     * @path /sticky/get/all
     * @describe to get all stickies
     * @method GET
     */
    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_sticky_all(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        List<Sticky> stickies = BaseApp.stickyRepository.getAll();
        Log.d(TAG, "get_sticky_all: " + stickies.size());
        return ReturnDataUtils.successfulJson(stickies);
    }

    /**
     * api 5
     *
     * @param returnData 1 2
     *                   1 返回全部sticky数据
     *                   2 返回undone的sticky
     * @path = /sticky/add
     * @describe add sticky by post
     * @method GET
     */
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String add_sticky_with_post(
            @RequestParam(name = "return", defaultValue = "1", required = false) int returnData,
            @RequestParam(name = "sticky") String STICKY,
            @RequestParam(name = "title", required = false, defaultValue = "DefaultTitle") String TITLE,
            @RequestParam(name = "device", required = false, defaultValue = "DefaultDevice") String DEVICE_NAME,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        Sticky sticky = new Sticky(STICKY, TITLE, DEVICE_NAME);
        BaseApp.stickyRepository.insert(sticky);
        switch (returnData) {
            case 2: {
                return ReturnDataUtils.successfulJson(BaseApp.stickyRepository.getNotDoneAll());
            }
            default: {
                return ReturnDataUtils.successfulJson(BaseApp.stickyRepository.getAll());
            }
        }
    }

}
