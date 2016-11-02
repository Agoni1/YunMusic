package com.example.agoni.yunmusic;

import android.app.Application;

import com.example.agoni.yunmusic.bean.SongInfoDetail;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Agoni on 2016/10/15.
 */
public class Myapp extends Application {
    private String curSongid = null;//记录当前播放的song_id
    private List<SongInfoDetail> playList = new ArrayList<>();//播放列表
    private HashMap<String, Integer> indexOfList = new HashMap<String, Integer>();//每首歌在列表中的索引
    private int currentPosition=0;

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpSample");// 设置NoHttp打印Log的tag。
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<SongInfoDetail> getPlayList() {
        return playList;
    }

    public void addToPlayList(SongInfoDetail songInfoDetail) {
        playList.add(songInfoDetail);
    }

    public void setPlayList(List<SongInfoDetail> playList) {
        this.playList = playList;
    }

    public void setIndexOfList(HashMap<String, Integer> indexOfList) {
        this.indexOfList = indexOfList;
    }

    public HashMap<String, Integer> getIndexOfList() {
        return indexOfList;
    }

    public void addIndex(String songid, int index) {
        indexOfList.put(songid, index);
    }

    public int getIndex(String songid) {
        return indexOfList.get(songid);
    }

    public String getCurSongid() {
        return curSongid;
    }

    public void setCurSongid(String curSongid) {
        this.curSongid = curSongid;
    }

}
