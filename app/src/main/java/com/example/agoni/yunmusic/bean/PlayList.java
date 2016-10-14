package com.example.agoni.yunmusic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/10/12.
 */
public class PlayList {
    private static List<SongInfoDetail> playlist=new ArrayList<>();
    public static int lastAddedIsSong=1;//用来判断最后一次添加进来的是歌曲还是列表

    public static List<SongInfoDetail> getPlaylist() {
        return playlist;
    }

    public static void addSongToList(SongInfoDetail songInfoDetail) {
        playlist.add(songInfoDetail);
        lastAddedIsSong=1;
    }

    public static void setPlaylist(List<SongInfoDetail> songInfoDetailList) {
        playlist = songInfoDetailList;
        lastAddedIsSong=0;
    }

}