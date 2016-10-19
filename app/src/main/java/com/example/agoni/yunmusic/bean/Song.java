package com.example.agoni.yunmusic.bean;

import java.io.Serializable;

/**
 * Created by Agoni on 2016/10/12.
 */
public class Song implements Serializable{
    /**
     show_link	:	http://zhangmenshiting.baidu.com/data2/music/dcf2092999093c0d0460470e770d4783/256405659/256405659.mp3?xcode=82fbc04e500e6c0c2bb21879f3eb947d
     down_type	:	0
     original	:	0
     free	:	1
     replay_gain	:	1.110001
     song_file_id	:	256405659
     file_size	:	1609437
     file_extension	:	mp3
     file_duration	:	201
     can_see	:	1
     can_load	:	true
     preload	:	40
     file_bitrate	:	64
     file_link	:	http://yinyueshiting.baidu.com/data2/music/dcf2092999093c0d0460470e770d4783/256405659/256405659.mp3?xcode=82fbc04e500e6c0c2bb21879f3eb947d
     is_udition_url	:	1
     hash	:	777c2c4e13756d33ab985aa47bce2426542b5e48
     */
    private String show_link ;
    private String down_type;
    private String original;
    private String free;
    private String replay_gain;
    private String song_file_id;
    private String file_size;
    private String file_extension;
    private String file_duration;
    private String can_see;
    private String can_load;
    private String preload;
    private String file_bitrate;
    private String file_link;
    private String is_udition_url;
    private String hash;

    public String getShow_link() {
        return show_link;
    }

    public void setShow_link(String show_link) {
        this.show_link = show_link;
    }

    public String getDown_type() {
        return down_type;
    }

    public void setDown_type(String down_type) {
        this.down_type = down_type;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getReplay_gain() {
        return replay_gain;
    }

    public void setReplay_gain(String replay_gain) {
        this.replay_gain = replay_gain;
    }

    public String getSong_file_id() {
        return song_file_id;
    }

    public void setSong_file_id(String song_file_id) {
        this.song_file_id = song_file_id;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getFile_duration() {
        return file_duration;
    }

    public void setFile_duration(String file_duration) {
        this.file_duration = file_duration;
    }

    public String getCan_see() {
        return can_see;
    }

    public void setCan_see(String can_see) {
        this.can_see = can_see;
    }

    public String getCan_load() {
        return can_load;
    }

    public void setCan_load(String can_load) {
        this.can_load = can_load;
    }

    public String getPreload() {
        return preload;
    }

    public void setPreload(String preload) {
        this.preload = preload;
    }

    public String getFile_bitrate() {
        return file_bitrate;
    }

    public void setFile_bitrate(String file_bitrate) {
        this.file_bitrate = file_bitrate;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getIs_udition_url() {
        return is_udition_url;
    }

    public void setIs_udition_url(String is_udition_url) {
        this.is_udition_url = is_udition_url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
