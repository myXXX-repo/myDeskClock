package com.wh.mydeskclock.app.sticky;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Sticky {
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

    @ColumnInfo(name = "modifyTime")
    private String ModifyTime;

    public Sticky() {
        this.CreateTime = String.valueOf(System.currentTimeMillis());
        this.ModifyTime = this.CreateTime;
        this.ReadDone = false;
    }

    @Ignore
    public Sticky(int id) {
        this.id = id;
    }

    @Ignore
    public Sticky(int id, String con, String title, String deviceName, String createTime, boolean isDone) {
        this.id = id;
        this.Con = con;
        this.Title = title;
        this.DeviceName = deviceName;
        this.CreateTime = createTime;
        this.ModifyTime = createTime;
        this.ReadDone = isDone;
    }

    @Ignore
    public Sticky(int id, String con, String title, String deviceName, String createTime, String modifyTime,boolean isDone) {
        this.id = id;
        this.Con = con;
        this.Title = title;
        this.DeviceName = deviceName;
        this.CreateTime = createTime;
        this.ModifyTime = modifyTime;
        this.ReadDone = isDone;
    }

    @Ignore
    public Sticky(String con, String title, String deviceName) {
        this.Con = con;
        this.Title = title;
        this.DeviceName = deviceName;
        this.CreateTime = String.valueOf(System.currentTimeMillis());
        this.ModifyTime = this.CreateTime;
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

    public void setModifyTime(String modifyTime) {
        ModifyTime = modifyTime;
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

    public String getModifyTime() {
        return ModifyTime;
    }

    public boolean isReadDone() {
        return ReadDone;
    }
}
