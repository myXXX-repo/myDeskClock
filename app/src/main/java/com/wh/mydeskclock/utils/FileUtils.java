package com.wh.mydeskclock.utils;

public class FileUtils {
    public static class MimeType{
        public static class APPLICATION{
            public static String HEAD = "application";
            public static String ALL = "application/*";
            public static String OCTET_STREAM = "application/octet-stream";
            public static String APK = "application/vnd.android.package-archive";
            public static String ZIP = HEAD+"/zip";
            public static String TAR = HEAD+"/x-tar";
            public static String SH = HEAD+"/x-sh";
        }
        public static class TEXT{
            public static String ALL = "text/*";
            public static String PLAIN = "test/plain";
        }
    }
}
