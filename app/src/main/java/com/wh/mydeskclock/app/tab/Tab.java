package com.wh.mydeskclock.app.tab;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Tab{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "con")
    private String Con;

    @ColumnInfo(name = "createTime")
    private String CreateTime;

    @ColumnInfo(name = "device")
    private String DeviceName;

    @ColumnInfo(name = "done", defaultValue = "false")
    private boolean Done;

    public Tab() {
        this.CreateTime = String.valueOf(System.currentTimeMillis());
        this.Done = false;
    }

    @Ignore
    public Tab(int id) {
        this.id = id;
    }

    @Ignore
    public Tab(int id,String con, String deviceName,String createTime,boolean isDone) {
        this.id = id;
        this.Con = con;
        this.DeviceName = deviceName;
        this.CreateTime = createTime;
        this.Done = isDone;
    }

    @Ignore
    public Tab(String con, String deviceName) {
        this.Con = con;
        this.DeviceName = deviceName;
        this.CreateTime = String.valueOf(System.currentTimeMillis());
        this.Done = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCon() {
        return Con;
    }

    public void setCon(String con) {
        Con = con;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public boolean isDone() {
        return Done;
    }

    public void setDone(boolean done) {
        Done = done;
    }
}
