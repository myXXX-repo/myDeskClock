package com.wh.mydeskclock.app.mediaCtrl;


import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.MediaUtils;
import com.wh.mydeskclock.utils.ReturnDataUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestHeader;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

@RestController
@RequestMapping("/mc")
public class MediaCtrlController {
    private String TAG = "WH_" + getClass().getSimpleName();

    MediaCtrlController(){
        MainServer.apiList.add(new ApiNode(
                "mediaCtrl",
                "/mc/get/info",
                "http://ip:port/mc/get/info",
                "用来获取当前设备正在播放音乐的信息",
                "GET",
                "",
                ""
        ));
    }

    @GetMapping(value = "/get/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String get_info(@RequestHeader(name = "access_token", required = false)String ACCESS_TOKEN) {
        if(MainServer.authNotGot(ACCESS_TOKEN)){
            return ReturnDataUtils.failedJson(401,"Unauthorized");
        }
        MusicInfo musicInfo = new MusicInfo(
                MediaCtrlFragment.ARTIST,
                MediaCtrlFragment.TRACK,
                MediaCtrlFragment.ALBUM,
                MediaCtrlFragment.PLAYING,
                MediaCtrlFragment.ID
        );
        return ReturnDataUtils.successfulJson(musicInfo);
    }

    @GetMapping(path = "/mc_pp")
    public String rm_media_ctrl_play_pause(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        new Thread() {
            @Override
            public void run() {
                MediaUtils.pausePlay();
            }
        }.start();
        return ReturnDataUtils.successfulJson("done");
    }

    @GetMapping(path = "/mc_ppp")
    public String rm_media_ctrl_play_previous(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        new Thread() {
            @Override
            public void run() {
                MediaUtils.previousPlay();
            }
        }.start();
        return ReturnDataUtils.successfulJson("done");
    }

    @GetMapping(path = "/mc_pn")
    public String rm_media_ctrl_play_next(@RequestHeader(name = "access_token", required = false) String ACCESS_TOKEN) {
        if (MainServer.authNotGot(ACCESS_TOKEN)) {
            return ReturnDataUtils.failedJson(401, "Unauthorized");
        }
        new Thread() {
            @Override
            public void run() {
                MediaUtils.nextPlay();
            }
        }.start();
        return ReturnDataUtils.successfulJson("done");
    }

    public static class MusicInfo{
        public String artist;
        public String track;
        public String album;
        public boolean playing;
        public long id;

        public MusicInfo(String artist, String track, String album, boolean playing, long id) {
            this.artist = artist;
            this.track = track;
            this.album = album;
            this.playing = playing;
            this.id = id;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public boolean isPlaying() {
            return playing;
        }

        public void setPlaying(boolean playing) {
            this.playing = playing;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
