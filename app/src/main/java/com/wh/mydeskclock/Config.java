package com.wh.mydeskclock;

public class Config {
    public static class SharedPreferenceKey{
        public static final String SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME";
        public static final String SPV_COAST_MINUTE = "SPV_COAST_MINUTE";
        public static final String SPV_COAST_BATTERY = "SPV_COAST_BATTERY";
        public static final String SPV_COAST_SCREEN_BRIGHT_SWITCH = "SPV_COAST_SCREEN_BRIGHT_SWITCH";
        public static final String SPV_COAST_SCREEN_OR_SWITCH = "SPV_COAST_SCREEN_OR_SWITCH";
        public static final String SPV_COAST_COLOR_SWITCH = "SPV_COAST_COLOR_SWITCH";
    }

    public static class DefaultSharedPreferenceKey{
        // Setting UI
        public static final String SETTING_UI_SHOW_SERVER_ADDRESS = "setting_ui_show_server_address";
        public static final String SETTING_UI_RE_LAND = "setting_ui_re_land";
        public static final String SETTING_UI_LAND="setting_ui_land";
//        public static final String
//        public static final String
        // Setting Task
        public static final String SETTING_TASK_HIDE_DONE = "setting_task_hide_done";
        // Setting Media Ctrl
        public static final String SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = "setting_media_ctrl_enable_media_ctrl";
        public static final String SETTING_MEDIA_CTRL_SHOW_MEDIA_INFO = "setting_media_ctrl_show_media_info";
    }
}
