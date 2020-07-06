package com.wh.mydeskclock.NotifyNode;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Notify {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "notify")
    private String NotifyCon;

    @ColumnInfo(name = "title")
    private String NotifyTitle;

    @ColumnInfo(name = "createTime")
    private String NotifyCreateTime;

    @ColumnInfo(name = "device")
    private String DeviceName;

    @ColumnInfo(name = "done", defaultValue = "false")
    private boolean ReadDone;

    // 可能用于后续的追加朗读
//    @ColumnInfo(name = "runId")
//    private int runId;

    public Notify() {
        this.NotifyCreateTime = String.valueOf(System.currentTimeMillis());
        this.ReadDone = false;
    }

    @Ignore
    public Notify(int id) {
        this.id = id;
    }

    @Ignore
    public Notify(String notifyCon, String notifyTitle,String deviceName) {
        this.NotifyCon = notifyCon;
        this.NotifyTitle = notifyTitle;
        this.DeviceName = deviceName;
        this.NotifyCreateTime = String.valueOf(System.currentTimeMillis());
        this.ReadDone = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNotifyCon(String notifyCon) {
        NotifyCon = notifyCon;
    }

    public void setNotifyTitle(String notifyTitle) {
        NotifyTitle = notifyTitle;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public void setNotifyCreateTime(String notifyCreateTime) {
        NotifyCreateTime = notifyCreateTime;
    }

    public void setReadDone(boolean readDone) {
        ReadDone = readDone;
    }

//    public void setRunId(int runId){
//        this.runId = runId;
//    }

    public int getId() {
        return id;
    }

    public String getNotifyCon() {
        return NotifyCon;
    }

    public String getNotifyTitle() {
        return NotifyTitle;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public String getNotifyCreateTime() {
        return NotifyCreateTime;
    }

    public boolean isReadDone() {
        return ReadDone;
    }


//    public int getRunId() {
//        return runId;
//    }
}