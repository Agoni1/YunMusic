package com.example.agoni.yunmusic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/11/5.
 */

public class RankingDetailInfo {
    /**
     * billboard_type	:	21
     billboard_no	:	1514
     update_date	:	2016-11-05
     billboard_songnum	:	197
     havemore	:	1
     name	:	欧美金曲榜
     comment	:	实时展现百度音乐最热门欧美歌曲排行
     pic_s640	:	http://c.hiphotos.baidu.com/ting/pic/item/6a600c338744ebf89ac498cadbf9d72a6159a78a.jpg
     pic_s444	:	http://a.hiphotos.baidu.com/ting/pic/item/8d5494eef01f3a291bf6bec89b25bc315c607cfd.jpg
     pic_s260	:	http://a.hiphotos.baidu.com/ting/pic/item/8b13632762d0f7035cb3feda0afa513d2697c5b7.jpg
     pic_s210	:	http://business.cdn.qianqian.com/qianqian/pic/bos_client_29a7a75276f7435a289b36421a676719.jpg
     web_url	:	http://music.baidu.com/top/oumei
     */

    private String billboard_type;
    private String billboard_no;
    private String update_date;
    private String billboard_songnum;
    private String havemore;
    private String name;
    private String comment;
    private String pic_s640;
    private String pic_s444;
    private String pic_s260;
    private String pic_s210;
    private String web_url;
    private List<RankingSonginfo> rankingSonginfoList=new ArrayList<>();

    public String getBillboard_type() {
        return billboard_type;
    }

    public void setBillboard_type(String billboard_type) {
        this.billboard_type = billboard_type;
    }

    public String getBillboard_no() {
        return billboard_no;
    }

    public void setBillboard_no(String billboard_no) {
        this.billboard_no = billboard_no;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getBillboard_songnum() {
        return billboard_songnum;
    }

    public void setBillboard_songnum(String billboard_songnum) {
        this.billboard_songnum = billboard_songnum;
    }

    public String getHavemore() {
        return havemore;
    }

    public void setHavemore(String havemore) {
        this.havemore = havemore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPic_s640() {
        return pic_s640;
    }

    public void setPic_s640(String pic_s640) {
        this.pic_s640 = pic_s640;
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

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<RankingSonginfo> getRankingSonginfoList() {
        return rankingSonginfoList;
    }

    public void setRankingSonginfoList(List<RankingSonginfo> rankingSonginfoList) {
        this.rankingSonginfoList = rankingSonginfoList;
    }
}
