package com.example.agoni.yunmusic.bean;

/**
 * Created by Agoni on 2016/11/2.
 */

public class RunkingTopSonginfo {
    /**
     * title	:	演员
     author	:	薛之谦
     song_id	:	242078437
     album_id	:	241838068
     album_title	:	初学者
     rank_change	:	0
     all_rate	:	64,128,192,256,320,flac
     */
    private String title;
    private String author;
    private String song_id;
    private String album_id;
    private String album_title;
    private String rank_change;
    private String all_rate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getRank_change() {
        return rank_change;
    }

    public void setRank_change(String rank_change) {
        this.rank_change = rank_change;
    }

    public String getAll_rate() {
        return all_rate;
    }

    public void setAll_rate(String all_rate) {
        this.all_rate = all_rate;
    }
}
