package com.wh.mydeskclock.server;

import android.util.Log;

import com.wh.mydeskclock.MainActivityLand;
import com.wh.mydeskclock.utils.MediaUtils;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/rmc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RemoteCtrlController {
    private List<RMC> rmcs;

    public RemoteCtrlController() {
        rmcs= new ArrayList<>();
        rmcs.add(new RMC("FlashScreen","fs","used to flash screen",RMC.TYPE.BUTTON,""));
        rmcs.add(new RMC("SwitchScreenLight","sl","used to turn on or off screen light",RMC.TYPE.BUTTON,""));
        rmcs.add(new RMC("PlayPause","mc_pp","used to play pause or replay media",RMC.TYPE.BUTTON,""));
        rmcs.add(new RMC("PlayPrevious","mc_ppp","used to play previous media",RMC.TYPE.BUTTON,""));
        rmcs.add(new RMC("PlayNext","mc_pn","used to play next media",RMC.TYPE.BUTTON,""));
        Log.d("WH_RM", "RemoteCtrlController: ");
    }

    @GetMapping(path = "/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String get_all(@RequestHeader(value = "access_token",required = false)String ACCESS_TOKEN){
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        return ReturnDataUtils.successfulJson(rmcs);
    }

    @GetMapping(path = "/fs")
    public String rm_flash_screen(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
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

    @GetMapping(path = "/sl")
    public String rm_screen_light(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        // used to switch screen light
        return ReturnDataUtils.successfulJson("switch done");
    }

    @GetMapping(path = "/mc_pp")
    public String rm_media_ctrl_play_pause(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        new Thread(){
            @Override
            public void run() {
                MediaUtils.pausePlay();
            }
        }.start();
        return ReturnDataUtils.successfulJson("done");
    }

    @GetMapping(path = "/mc_ppp")
    public String rm_media_ctrl_play_previous(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        new Thread(){
            @Override
            public void run() {
                MediaUtils.previousPlay();
            }
        }.start();
        return ReturnDataUtils.successfulJson("done");
    }

    @GetMapping(path = "/mc_pn")
    public String rm_media_ctrl_play_next(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        new Thread(){
            @Override
            public void run() {
                MediaUtils.nextPlay();
            }
        }.start();
        return ReturnDataUtils.successfulJson("done");
    }




    public static class RMC{
        public static class TYPE {
            public static final String SWITCH = "SWITCH";
            public static final String BUTTON = "BUTTON";
            public static final String VALUES = "VALUES";
        }
        public static class PARAM{
            public static final String SWITCH = "status";
            public static final String BUTTON = "none";
            public static final String VALUES = "value";
        }
        String name; // name to display
        String path; // used behind http://ip:port/rmc/{path}
        String describe; // obviously
        String type; // switch button values
        String param;
        // switch : ?status = on / off
        // button : none
        // values : ?value=?
        String extra;
        // (json)value : max min etc...
        String ico_name;


        public RMC(String name, String path, String describe, String type, String extra) {
            this.name = name;
            this.path = path;
            this.describe = describe;
            this.type = type;
            judgeParam(type);
            this.extra = extra;
            this.ico_name = "default";
        }

        public RMC(String name, String path, String describe, String type, String ico_name,String extra) {
            this.name = name;
            this.path = path;
            this.describe = describe;
            this.type = type;
            judgeParam(type);
            this.extra = extra;
            this.ico_name = ico_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public void judgeParam(String type){
            switch (type){
                case TYPE.SWITCH:{
                    this.param=PARAM.SWITCH;
                    break;
                }
                case TYPE.BUTTON:{
                    this.param=PARAM.BUTTON;
                    break;
                }
                default:{
                    this.param=PARAM.VALUES;
                }
            }
        }
    }
}
