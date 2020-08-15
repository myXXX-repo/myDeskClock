package com.wh.mydeskclock.server;

import com.wh.mydeskclock.MainActivityLand;
import com.wh.mydeskclock.MainFragmentLand;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.wh.mydeskclock.utils.UiUtils;
import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PatchMapping;
import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

@RestController
@RequestMapping(path = "/rmc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RemoteCtrlController {

    @GetMapping(path = "/fs")
    public String rm_flash_screen(@RequestHeader("access_token")String ACCESS_TOKEN) {
        if(!ACCESS_TOKEN.equals(MainServer.access_token)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        new Thread() {
            @Override
            public void run() {
                MainActivityLand.flash();
            }
        }.start();
        return ReturnDataUtils.successfulJson("flash done");
    }

//    @GetMapping(path = "/light/{status}")
//    public String set_light(@PathVariable("status")String status){
//        switch (status){
//            case "1":
//            case "on":{
//                MainFragmentLand.setBrightness(1);
//                break;
//            }
//            case "0":
//            case "off":{
//                MainFragmentLand.setBrightness(0);
//                break;
//            }
//        }
//        return ReturnDataUtils.successfulJson("set done");
//    }
}
