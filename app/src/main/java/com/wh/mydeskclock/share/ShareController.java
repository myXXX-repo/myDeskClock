package com.wh.mydeskclock.share;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.EncryptionUtils;
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
@RequestMapping("/share")
public class ShareController {
    private String TAG = "WH_" + getClass().getSimpleName();

    /**
     * @path /share/get/{shareId}
     * @describe to get share item by id
     * @method GET
     */
    @GetMapping(path = "/get/{shareId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_by_id(Context context,
                     @PathVariable("shareId") int shareId,
                     @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        Share share = BaseApp.shareRepo.getById(shareId);
        return ReturnDataUtils.successfulJson(share);
    }

    /**
     * @path /share/add
     * @describe to add new share item by get
     * @method GET
     */
    @GetMapping(path = "/add/{type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_add_get(
            @PathVariable(name = "type") String TYPE, // allowed task sticky notify tab
            @RequestParam(name = "title", required = false) String TITLE,
            @RequestParam(name = "task", required = false) String TASK,
            @RequestParam(name = "notify", required = false) String NOTIFY,
            @RequestParam(name = "sticky", required = false) String STICKY,
            @RequestParam(name = "url", required = false) String URL,
//            @RequestParam(name = "fileName", required = false) String FILE_NAME,
//            @RequestParam(name = "fileLocation", required = false) String FILE_LOCATION,
            @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        switch (TYPE) {
            case Share.Type.task: {
                if (!TASK.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().TASK(TITLE, TASK)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share task get wrong params");
                }
                break;
            }
            case Share.Type.notify: {
                if (!NOTIFY.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().NOTIFY(TITLE, NOTIFY)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share notify get wrong params");
                }
                break;
            }
            case Share.Type.sticky: {
                if (!STICKY.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().STICKY(TITLE, STICKY)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share sticky get wrong params");
                }
                break;
            }
            case Share.Type.tab: {
                if (!URL.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().TAB(TITLE, URL)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share sticky get wrong params");
                }
                break;
            }
//            case Share.Type.file:{
            // 技能不全 撤退
//                // 这里只能将已存在的文件放到分享列表中 这里还要判断指定目录的文件是不是存在 不存在则返回错误
//                if(FILE_NAME!=null&&FILE_LOCATION!=null){
//                    BaseApp.shareRepo.insert(new Share(TYPE,JSON.toJSONString(new Share.ShareNode().FILE(FILE_NAME,FILE_LOCATION)),DEVICE));
//                }else {
//                    return ReturnDataUtils.failedJson(400,"share file get wrong params");
//                }
//                break;
//            }
            default: {
                return ReturnDataUtils.failedJson(400, "share " + TYPE + " wrong type");
            }
        }
        return ReturnDataUtils.successfulJson("share " + TYPE + " add done");
    }

    /**
     * @path /share/add/{type}
     * @describe to add new share item by POST
     * @method POST
     */
    @PostMapping(path = "/add/{type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_add_post(
            @PathVariable(name = "type") String TYPE,
            @RequestParam(name = "title", required = false, defaultValue = "default title") String TITLE,
            @RequestParam(name = "task", required = false, defaultValue = "not allowed") String TASK,
            @RequestParam(name = "notify", required = false, defaultValue = "not allowed") String NOTIFY,
            @RequestParam(name = "sticky", required = false, defaultValue = "not allowed") String STICKY,
            @RequestParam(name = "url", required = false, defaultValue = "not allowed") String URL,
//            @RequestParam(name = "fileName", required = false) String FILE_NAME,
//            @RequestParam(name = "fileLocation", required = false) String FILE_LOCATION,
            @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        String token;
        switch (TYPE) {
            case Share.Type.task: {
                if (!TASK.equals("not allowed")) {
                    Share share = new Share(TYPE,JSON.toJSONString(new Share.ShareNode().TASK(TITLE, TASK)), DEVICE);
                    token = share.getToken();
                    BaseApp.shareRepo.insert(share);
                } else {
                    return ReturnDataUtils.failedJson(400, "share task get wrong params");
                }
                break;
            }
            case Share.Type.notify: {
                if (!NOTIFY.equals("not allowed")) {
                    Share share = new Share(TYPE,JSON.toJSONString(new Share.ShareNode().NOTIFY(TITLE, NOTIFY)), DEVICE);
                    token = share.getToken();
                    BaseApp.shareRepo.insert(share);
                } else {
                    return ReturnDataUtils.failedJson(400, "share notify get wrong params");
                }
                break;
            }
            case Share.Type.sticky: {
                if (!STICKY.equals("not allowed")) {
                    Share share = new Share(TYPE, JSON.toJSONString(new Share.ShareNode().STICKY(TITLE, STICKY)), DEVICE);
                    token = share.getToken();
                    BaseApp.shareRepo.insert(share);
                } else {
                    return ReturnDataUtils.failedJson(400, "share sticky get wrong params");
                }
                break;
            }
            case Share.Type.tab: {
                if (!URL.equals("not allowed")) {
                    Share share = new Share(TYPE,JSON.toJSONString(new Share.ShareNode().TAB(TITLE,URL)),DEVICE);
                    token = share.getToken();
                    BaseApp.shareRepo.insert(share);
                } else {
                    return ReturnDataUtils.failedJson(400, "share sticky get wrong params");
                }
                break;
            }
//            case Share.Type.file: {
            // 技能不全 撤退
//                // 这里控制上传文件 首先判断文件是否存在 文件存在即重命名并换用新的文件名记录到数据库
//                if (FILE_NAME != null && FILE_LOCATION != null) {
//                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().FILE(FILE_NAME, FILE_LOCATION)), DEVICE));
//                } else {
//                    return ReturnDataUtils.failedJson(400, "share file get wrong params");
//                }
//                break;
//            }
            default: {
                return ReturnDataUtils.failedJson(400, "share " + TYPE + " wrong type");
            }
        }
        return ReturnDataUtils.successfulJson(token);
    }

    /**
     * @path /share/get/{type}/{done_type}
     * @describe to get by type and done type
     * @method GET
     */
    @GetMapping(path = "/get/{type}/{done_type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String get_by_type(
            @PathVariable("type") String TYPE, // allowed task tab notify sticky tab file uni
            @PathVariable("done_type") String DONE_TYPE, // allowed all undone
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        List<Share> shares;
        switch (TYPE) {
            case Share.Type.task:
            case Share.Type.tab:
            case Share.Type.sticky:
            case Share.Type.notify:
            case Share.Type.file: {
                if (DONE_TYPE.equals("all")) {
                    shares = BaseApp.shareRepo.getByType(TYPE);
                } else if (DONE_TYPE.equals("undone")) {
                    shares = BaseApp.shareRepo.getByTypeUnDone(TYPE);
                } else {
                    return ReturnDataUtils.failedJson(400, "wrong type");
                }
                break;
            }
            case "uni": {
                if (DONE_TYPE.equals("all")) {
                    shares = BaseApp.shareRepo.getAll();
                    break;
                } else if (DONE_TYPE.equals("undone")) {
                    shares = BaseApp.shareRepo.getAllUndone();
                } else {
                    return ReturnDataUtils.failedJson(400, "wrong done type");
                }
                break;
            }
            default: {
                return ReturnDataUtils.failedJson(400, "wrong type");
            }
        }
        return ReturnDataUtils.successfulJson(shares);
    }

    /**
     * @path /share/get/{type}
     * @describe public get by type, get all
     * @method GET
     */
    @GetMapping(path = "/public/get/{type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String public_get_all(
            @PathVariable("type") String TYPE // allowed task tab notify sticky tab file uni
    ) {
        switch (TYPE) {
            case Share.Type.task: {
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getByTypeUnDone(Share.Type.task));
            }
            case Share.Type.tab: {
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getByTypeUnDone(Share.Type.tab));
            }
            case Share.Type.sticky: {
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getByTypeUnDone(Share.Type.sticky));
            }
            case Share.Type.notify: {
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getByTypeUnDone(Share.Type.notify));
            }
            case Share.Type.file: {
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getByTypeUnDone(Share.Type.file));
            }
            case "all":
            case "uni": {
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getAllUndone());
            }
            case "last": {
                // 这里保留了一个逻辑问题 latest应该是绝对的最后一个share项目,而这里是最后一个未完成的项目,所以命名为last,意为最后一个undone 又不是不能用系列
                return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getAllUndone().get(0));
            }
            default: {
                return ReturnDataUtils.failedJson(400, "wrong type");
            }
        }
    }

    /**
     * @path /share/get/{token}
     * @describe public get by token, undone
     * @method GET
     */
    @GetMapping(path = "/public/get/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String public_get_by_token(
            @PathVariable(name = "token") String TOKEN
    ) {
        if (!EncryptionUtils.isMD5(TOKEN)) {
            return ReturnDataUtils.failedJson(400, "wrong token");
        }
        return ReturnDataUtils.successfulJson(BaseApp.shareRepo.getUnDoneWithToken(TOKEN));
    }


    // 安全性未知所以暂时不启用
    @GetMapping(path = "/public/add/{type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String public_add_get( //不支持上传文件
                                  @PathVariable(name = "type") String TYPE,
                                  @RequestParam(name = "title", required = false) String TITLE,
                                  @RequestParam(name = "task", required = false) String TASK,
                                  @RequestParam(name = "notify", required = false) String NOTIFY,
                                  @RequestParam(name = "sticky", required = false) String STICKY,
                                  @RequestParam(name = "url", required = false) String URL,
                                  @RequestParam(name = "device", required = false, defaultValue = "default device") String DEVICE) {
        if (true) {
            return ReturnDataUtils.failedJson(404, "not found");
        }
        switch (TYPE) {
            case Share.Type.task: {
                if (!TASK.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().TASK(TITLE, TASK)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share task get wrong params");
                }
                break;
            }
            case Share.Type.notify: {
                if (!NOTIFY.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().NOTIFY(TITLE, NOTIFY)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share notify get wrong params");
                }
                break;
            }
            case Share.Type.sticky: {
                if (!STICKY.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().STICKY(TITLE, STICKY)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share sticky get wrong params");
                }
                break;
            }
            case Share.Type.tab: {
                if (!URL.equals("not allowed")) {
                    BaseApp.shareRepo.insert(new Share(TYPE, JSON.toJSONString(new Share.ShareNode().TAB(TITLE, URL)), DEVICE));
                } else {
                    return ReturnDataUtils.failedJson(400, "share sticky get wrong params");
                }
                break;
            }
            default: {
                return ReturnDataUtils.failedJson(400, "share " + TYPE + " wrong type");
            }
        }
        return ReturnDataUtils.successfulJson("share " + TYPE + " public add done");
    }

    /**
     * @path /share/set/{done}/{shareId}
     * @describe to get by type and done type
     * @method GET
     */
    @GetMapping("/set/{done}/{shareId}")
    public String set_done_undone(
            @PathVariable(name = "done") String DONE,
            @PathVariable(name = "shareId") int shareId,
            @RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        if (DONE.equals("done")) {
            Share share = BaseApp.shareRepo.getById(shareId);
            if (share.isDone()) {
                return ReturnDataUtils.failedJson(400, "set " + shareId + " done done");
            }
            share.setDone(true);
            BaseApp.shareRepo.update(share);
            return ReturnDataUtils.failedJson(400, "set " + shareId + " done done");
        } else if (DONE.equals("undone")) {
            Share share = BaseApp.shareRepo.getById(shareId);
            if (share.isDone()) {
                share.setDone(false);
                BaseApp.shareRepo.update(share);
            }
            return ReturnDataUtils.failedJson(400, "set " + shareId + " done done");
        } else {
            return ReturnDataUtils.failedJson(400, "wrong operation " + DONE);
        }
    }
}
