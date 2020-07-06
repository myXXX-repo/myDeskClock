package com.wh.mydeskclock.TaskNode;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "con")
    private String Con;

    @ColumnInfo(name = "title")
    private String Title;

    @ColumnInfo(name = "createTime")
    private String CreateTime;

    @ColumnInfo(name = "device")
    private String DeviceName;

    @ColumnInfo(name = "done", defaultValue = "false")
    private boolean ReadDone;

    public Task() {
        this.CreateTime = String.valueOf(System.currentTimeMillis());
        this.ReadDone = false;
    }

    @Ignore
    public Task(int id) {
        this.id = id;
    }

    @Ignore
    public Task(int id,String con, String title, String deviceName,String createTime,boolean isDone) {
        this.id = id;
        this.Con = con;
        this.Title = title;
        this.DeviceName = deviceName;
        this.CreateTime = createTime;
        this.ReadDone = isDone;
    }

    @Ignore
    public Task(String con, String title, String deviceName) {
        this.Con = con;
        this.Title = title;
        this.DeviceName = deviceName;
        this.CreateTime = String.valueOf(System.currentTimeMillis());
        this.ReadDone = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCon(String con) {
        Con = con;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public void setReadDone(boolean readDone) {
        ReadDone = readDone;
    }

    public int getId() {
        return id;
    }

    public String getCon() {
        return Con;
    }

    public String getTitle() {
        return Title;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public boolean isReadDone() {
        return ReadDone;
    }
}