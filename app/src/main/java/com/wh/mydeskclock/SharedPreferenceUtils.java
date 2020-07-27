package com.wh.mydeskclock;

import com.wh.mydeskclock.utils.ApiNode;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceUtils {
    public static class sp_coast {
        public static final String FILENAME = "COAST";
        public static final String COAST_FLASH_SCREEN = "COAST_FLASH_SCREEN";
        public static final String COAST_LIGHT_OFF = "COAST_LIGHT_OFF";
        public static final String COAST_LIGHT_ON = "COAST_LIGHT_ON";
    }

    public static class sp_default {
        // Setting UI
        public static final String SETTING_UI_SHOW_SERVER_ADDRESS = "setting_ui_show_server_address";
        public static final String SETTING_UI_RE_LAND = "setting_ui_re_land";
        public static final String SETTING_UI_LAND = "setting_ui_land";
        public static final String SETTING_UI_AUTO_FLASH_SCREEN= "setting_ui_auto_flash_screen";

        // Setting Task
        public static final String SETTING_TASK_HIDE_DONE = "setting_task_hide_done";

        // Setting Media Ctrl
        public static final String SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = "setting_media_ctrl_enable_media_ctrl";
        public static final String SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO = "setting_media_ctrl_show_media_info";

        // Setting About
        public static final String SETTING_ABOUT_ME_GITHUB = "setting_about_me_github";
        public static final String SETTING_ABOUT_ME_COOLAPK = "setting_about_me_coolapk";
        public static final String SETTING_ABOUT_GOTO_APP_COOLAPK = "setting_about_goto_app_coolapk";
        public static final String SETTING_ABOUT_VERSION = "setting_about_version";

        // Setting Http Server
        public static final String SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER = "setting_http_server_enable_http_server";
        public static final String SETTING_HTTP_SERVER_PORT = "setting_http_server_port";
    }

//    public static List<ApiNode> HttpServerApis = new ArrayList<>();
}
