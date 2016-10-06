package com.example.agoni.yunmusic.bean;

/**
 * Created by Agoni on 2016/10/6.
 */
public class RecommendSonglistInfo {
    /*
     position	:	1
     tag	:	华语,流行,励志
     songidlist		[0]
     pic	:	http://musicugc.cdn.qianqian.com/ugcdiy/pic/ef0cb6ec4d3b449a4629d29bd9edf676.jpg
     title	:	前有梦想可奔赴，后有岁月可回头
     collectnum	:	28
     type	:	gedan
     listenum	:	11442
     listid	:	354615498
     */

    private int position;
    private String tag;
    private int[] songidlist;
    private String pic;
    private String title;
    private int collectnum;
    private String type;
    private String listenum;
    private String listid;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int[] getSongidlist() {
        return songidlist;
    }

    public void setSongidlist(int[] songidlist) {
        this.songidlist = songidlist;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCollectnum() {
        return collectnum;
    }

    public void setCollectnum(int collectnum) {
        this.collectnum = collectnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getListenum() {
        return listenum;
    }

    public void setListenum(String listenum) {
        this.listenum = listenum;
    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }
}
