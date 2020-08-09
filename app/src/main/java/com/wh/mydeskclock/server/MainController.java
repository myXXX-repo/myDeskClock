package com.wh.mydeskclock.server;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PatchMapping;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

@RestController
@PatchMapping("/api")
public class MainController {

    public MainController() {
        MainServer.apiList.add(new ApiNode("MainServer","/api/get","http://ip:port/api/get","","GET","null","null"));
    }

    @GetMapping(path = "/get",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String api_get(){
        return ReturnDataUtils.successfulJson(MainServer.apiList);
    }
}