package com.example.agoni.yunmusic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/11/2.
 */

public class RunkingInfo {
    /**
     * name	:	新歌榜
     type	:	1
     count	:	4
     comment	:	该榜单是根据百度音乐平台歌曲每日播放量自动生成的数据榜单，统计范围为近期发行的歌曲，每日更新一次
     pic_s192	:	http://b.hiphotos.baidu.com/ting/pic/item/9922720e0cf3d7caf39ebc10f11fbe096b63a968.jpg
     pic_s444	:	http://d.hiphotos.baidu.com/ting/pic/item/78310a55b319ebc4845c84eb8026cffc1e17169f.jpg
     pic_s260	:	http://b.hiphotos.baidu.com/ting/pic/item/e850352ac65c1038cb0f3cb0b0119313b07e894b.jpg
     pic_s210	:	http://business.cdn.qianqian.com/qianqian/pic/bos_client_c49310115801d43d42a98fdc357f6057.jpg
     */

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPic_s192() {
        return pic_s192;
    }

    public void setPic_s192(String pic_s192) {
        this.pic_s192 = pic_s192;
    }

    public String getPic_s444() {
        return pic_s444;
    }

    public void setPic_s444(String pic_s444) {
        this.pic_s444 = pic_s444;
    }

    public String getPic_s260() {
        return pic_s260;
    }

    public void setPic_s260(String pic_s260) {
        this.pic_s260 = pic_s260;
    }

    public String getPic_s210() {
        return pic_s210;
    }

    public void setPic_s210(String pic_s210) {
        this.pic_s210 = pic_s210;
    }

    public List<RunkingTopSonginfo> getRunkingTopSonginfoList() {
        return runkingTopSonginfoList;
    }

    public void setRunkingTopSonginfoList(List<RunkingTopSonginfo> runkingTopSonginfoList) {
        this.runkingTopSonginfoList = runkingTopSonginfoList;
    }

    private String count;
    private String comment;
    private String pic_s192;
    private String pic_s444;
    private String pic_s260;
    private String pic_s210;
    private List<RunkingTopSonginfo> runkingTopSonginfoList=new ArrayList<>();

}
