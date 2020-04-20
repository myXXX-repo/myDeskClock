package com.wh.mydeskclock.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sticky {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String StickyTitle;

    @ColumnInfo(name = "con")
    private String StickyCon;

    @ColumnInfo(name = "createTime")
    private String StickyCreateTime;

    @ColumnInfo(name = "done")
    private boolean StickyDone;

    public Sticky(){

    }

//    public Sticky(String stickyTitle, String stickyCon,
//                  String stickyCreateTime, boolean stickyDone) {
//        StickyTitle = stickyTitle;
//        StickyCon = stickyCon;
//        StickyCreateTime = stickyCreateTime;
//        StickyDone = stickyDone;
//    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStickyTitle(String stickyTitle) {
        StickyTitle = stickyTitle;
    }

    public void setStickyCon(String stickyCon) {
        StickyCon = stickyCon;
    }

    public void setStickyCreateTime(String stickyCreateTime) {
        StickyCreateTime = stickyCreateTime;
    }

    public void setStickyDone(boolean stickyDone) {
        StickyDone = stickyDone;
    }

    public int getId() {
        return id;
    }

    public String getStickyTitle() {
        return StickyTitle;
    }

    public String getStickyCon() {
        return StickyCon;
    }

    public String getStickyCreateTime() {
        return StickyCreateTime;
    }

    public boolean isStickyDone() {
        return StickyDone;
    }
}
