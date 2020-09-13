package com.wh.mydeskclock.share;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.wh.mydeskclock.utils.EncryptionUtils;


@Entity
public class Share {
    public static class Type {
        public final static String task = "task";
        public final static String notify = "notify";
        public final static String tab = "tab";
        public final static String sticky = "sticky";
        public final static String file = "file";

    }

    @PrimaryKey(autoGenerate = true)
    int id;

    /**
     * @describe usage
     * task notify tab sticky （file）
     * task: con 保存 title task
     * notify con 保存 title notify
     * tab con 保存 title url
     * sticky con 保存 title sticky
     * file con 保存 fileName fileLocation
     * @value type: task notify tab sticky file
     */
    @ColumnInfo(name = "type")
    String type;

    @ColumnInfo(name = "con")
    String con;

    @ColumnInfo(name = "shareTime")
    Long shareTime;

    @ColumnInfo(name = "deviceName")
    String deviceName;

    @ColumnInfo(name = "done", defaultValue = "false")
    boolean done;

    @ColumnInfo(name = "token")
    String token;//createTime的md5

    // 这个用于满足room库的要求
    public Share() {
        this.shareTime = System.currentTimeMillis();
        this.token = EncryptionUtils.getMD5Str(String.valueOf(this.shareTime));
    }

    // 这个用于删除share条目
    @Ignore
    public Share(int id) {
        this.id = id;
    }

    // 这个用于创建share条目
    @Ignore
    public Share(String type, String con, String deviceName) {
        this.type = type;
        this.con = con;
        this.shareTime = System.currentTimeMillis();
        this.deviceName = deviceName;
        this.token = EncryptionUtils.getMD5Str(String.valueOf(this.shareTime));
    }

    // 这个用于修改share条目
    @Ignore
    public Share(int id, String type, String con, Long shareTime, String deviceName, boolean isDone, String token) {
        this.id = id;
        this.type = type;
        this.con = con;
        this.shareTime = shareTime;
        this.deviceName = deviceName;
        this.done = isDone;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public Long getShareTime() {
        return shareTime;
    }

    public void setShareTime(Long shareTime) {
        this.shareTime = shareTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class ShareNode {
        String title;
        String task;
        String notify;
        String sticky;
        String url;
        String fileName;
        String fileLocation;

        public ShareNode TASK(String title, String task) {
            this.title = title;
            this.task = task;
            return this;
        }

        public ShareNode NOTIFY(String title, String notify) {
            this.title = title;
            this.notify = notify;
            return this;
        }

        public ShareNode STICKY(String title, String sticky) {
            this.title = title;
            this.sticky = sticky;
            return this;
        }

        public ShareNode TAB(String title, String url) {
            this.title = title;
            this.url = url;
            return this;
        }

        public ShareNode FILE(String fileName, String fileLocation) {
            this.fileName = fileName;
            this.fileLocation = fileLocation;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public String getNotify() {
            return notify;
        }

        public void setNotify(String notify) {
            this.notify = notify;
        }

        public String getSticky() {
            return sticky;
        }

        public void setSticky(String sticky) {
            this.sticky = sticky;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }
    }
}
