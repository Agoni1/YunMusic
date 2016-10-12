package com.example.agoni.yunmusic.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/10/12.
 */
public class SongInfoDetail {
    /**
     * distribution	:	0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,1111111111,1111111111,0000000000
     * relate_status	:	0
     * learn	:	0
     * play_type	:	0
     * pic_big	:	http://musicdata.baidu.com/data2/pic/0171bb5aa87aba5a7aabdd2a5b79c727/257807696/257807696.jpg
     * pic_premium	:	http://musicdata.baidu.com/data2/pic/c607139a6ce2c417456c1124751fbaf7/257807681/257807681.jpg
     * artist_480_800	:
     * aliasname	:
     * country	:	欧美
     * artist_id	:	38574
     * album_id	:	257807665
     * share_num	:	2
     * compress_status	:	0
     * versions	:
     * expire	:	36000
     * ting_uid	:	88985
     * artist_1000_1000	:	http://musicdata.baidu.com/data2/pic/3b2e14a57907c1a5cee1c919a190ea0d/114971996/114971996.jpg
     * all_artist_id	:	38574
     * artist_640_1136	:
     * publishtime	:	2015-11-13
     * charge	:	0
     * copy_type	:	0
     * songwriting	:
     * share_url	:	http://music.baidu.com/song/256393709
     * author	:	Justin Bieber
     * has_mv_mobile	:	1
     * all_rate	:	64,128,192,256,320
     * pic_small	:	http://musicdata.baidu.com/data2/pic/052ee47c6782bbafd0cca4a2210acc37/257807705/257807705.jpg
     * album_no	:	4
     * song_id	:	256393709
     * is_charge	:	0
     * resource_type_ext	:	0
     * comment_num	:	4
     * pic_huge	:
     * resource_type	:	0
     * original	:	0
     * del_status	:	0
     * korean_bb_song	:	0
     * album_1000_1000	:
     * pic_singer	:
     * album_500_500	:	http://musicdata.baidu.com/data2/pic/c607139a6ce2c417456c1124751fbaf7/257807681/257807681.jpg
     * havehigh	:	2
     * piao_id	:	0
     * song_source	:	web
     * collect_num	:	1244
     * compose	:
     * toneid	:	0
     * area	:	2
     * original_rate	:
     * bitrate	:	64,128,192,256,320
     * artist_500_500	:	http://musicdata.baidu.com/data2/pic/2ef4a5ef7357c5111d172c0bf6e0706d/114971784/114971784.jpg
     * multiterminal_copytype	:	1110,1101,1011,0111
     * has_mv	:	1
     * file_duration	:	201
     * album_title	:	Purpose
     * sound_effect	:	0
     * title	:	Sorry
     * high_rate	:	320
     * pic_radio	:	http://musicdata.baidu.com/data2/pic/f3eab18314ae81c892f7d52c5d3ad0c8/257807684/257807684.jpg
     * is_first_publish	:	0
     * hot	:	35299
     * language	:	英语
     * lrclink	:	http://musicdata.baidu.com/data2/lrc/8502f6ea981ee33e1da91f1c49d878aa/257813790/257813790.lrc
     */
    private String lrclink;
    private String language;
    private String hot;
    private String is_first_publish;
    private String pic_radio;
    private String high_rate;
    private String title;
    private String sound_effect;
    private String album_title;
    private String file_duration;
    private String has_mv;
    private String multiterminal_copytype;
    private String artist_500_500;
    private String bitrate;
    private String original_rate;
    private String area;
    private String toneid;
    private String compose;
    private String collect_num;
    private String song_source;
    private String piao_id;
    private String havehigh;
    private String album_500_500;
    private String pic_singer;
    private String album_1000_1000;
    private String korean_bb_song;
    private String del_status;
    private String original;
    private String resource_type;
    private String pic_huge;
    private String comment_num;
    private String resource_type_ext;
    private String is_charge;
    private String song_id;
    private String album_no;
    private String pic_small;
    private String all_rate;
    private String has_mv_mobile;
    private String author;
    private String share_url;
    private String songwriting;
    private String copy_type;
    private String charge;
    private String publishtime;
    private String artist_640_1136;
    private String all_artist_id;
    private String artist_1000_1000;
    private String ting_uid;
    private String expire;
    private String versions;
    private String compress_status;
    private String share_num;
    private String album_id;
    private String artist_id;
    private String country;
    private String aliasname;
    private String artist_480_800;
    private String pic_premium;
    private String pic_big;
    private String play_type;
    private String learn;
    private String relate_status;
    private String distribution;

    private List<Song> songs=new ArrayList<>();

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getIs_first_publish() {
        return is_first_publish;
    }

    public void setIs_first_publish(String is_first_publish) {
        this.is_first_publish = is_first_publish;
    }

    public String getPic_radio() {
        return pic_radio;
    }

    public void setPic_radio(String pic_radio) {
        this.pic_radio = pic_radio;
    }

    public String getHigh_rate() {
        return high_rate;
    }

    public void setHigh_rate(String high_rate) {
        this.high_rate = high_rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSound_effect() {
        return sound_effect;
    }

    public void setSound_effect(String sound_effect) {
        this.sound_effect = sound_effect;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getFile_duration() {
        return file_duration;
    }

    public void setFile_duration(String file_duration) {
        this.file_duration = file_duration;
    }

    public String getHas_mv() {
        return has_mv;
    }

    public void setHas_mv(String has_mv) {
        this.has_mv = has_mv;
    }

    public String getMultiterminal_copytype() {
        return multiterminal_copytype;
    }

    public void setMultiterminal_copytype(String multiterminal_copytype) {
        this.multiterminal_copytype = multiterminal_copytype;
    }

    public String getArtist_500_500() {
        return artist_500_500;
    }

    public void setArtist_500_500(String artist_500_500) {
        this.artist_500_500 = artist_500_500;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getOriginal_rate() {
        return original_rate;
    }

    public void setOriginal_rate(String original_rate) {
        this.original_rate = original_rate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getToneid() {
        return toneid;
    }

    public void setToneid(String toneid) {
        this.toneid = toneid;
    }

    public String getCompose() {
        return compose;
    }

    public void setCompose(String compose) {
        this.compose = compose;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getSong_source() {
        return song_source;
    }

    public void setSong_source(String song_source) {
        this.song_source = song_source;
    }

    public String getPiao_id() {
        return piao_id;
    }

    public void setPiao_id(String piao_id) {
        this.piao_id = piao_id;
    }

    public String getHavehigh() {
        return havehigh;
    }

    public void setHavehigh(String havehigh) {
        this.havehigh = havehigh;
    }

    public String getAlbum_500_500() {
        return album_500_500;
    }

    public void setAlbum_500_500(String album_500_500) {
        this.album_500_500 = album_500_500;
    }

    public String getPic_singer() {
        return pic_singer;
    }

    public void setPic_singer(String pic_singer) {
        this.pic_singer = pic_singer;
    }

    public String getAlbum_1000_1000() {
        return album_1000_1000;
    }

    public void setAlbum_1000_1000(String album_1000_1000) {
        this.album_1000_1000 = album_1000_1000;
    }

    public String getKorean_bb_song() {
        return korean_bb_song;
    }

    public void setKorean_bb_song(String korean_bb_song) {
        this.korean_bb_song = korean_bb_song;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getPic_huge() {
        return pic_huge;
    }

    public void setPic_huge(String pic_huge) {
        this.pic_huge = pic_huge;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getResource_type_ext() {
        return resource_type_ext;
    }

    public void setResource_type_ext(String resource_type_ext) {
        this.resource_type_ext = resource_type_ext;
    }

    public String getIs_charge() {
        return is_charge;
    }

    public void setIs_charge(String is_charge) {
        this.is_charge = is_charge;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum_no() {
        return album_no;
    }

    public void setAlbum_no(String album_no) {
        this.album_no = album_no;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getAll_rate() {
        return all_rate;
    }

    public void setAll_rate(String all_rate) {
        this.all_rate = all_rate;
    }

    public String getHas_mv_mobile() {
        return has_mv_mobile;
    }

    public void setHas_mv_mobile(String has_mv_mobile) {
        this.has_mv_mobile = has_mv_mobile;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getSongwriting() {
        return songwriting;
    }

    public void setSongwriting(String songwriting) {
        this.songwriting = songwriting;
    }

    public String getCopy_type() {
        return copy_type;
    }

    public void setCopy_type(String copy_type) {
        this.copy_type = copy_type;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getArtist_640_1136() {
        return artist_640_1136;
    }

    public void setArtist_640_1136(String artist_640_1136) {
        this.artist_640_1136 = artist_640_1136;
    }

    public String getAll_artist_id() {
        return all_artist_id;
    }

    public void setAll_artist_id(String all_artist_id) {
        this.all_artist_id = all_artist_id;
    }

    public String getArtist_1000_1000() {
        return artist_1000_1000;
    }

    public void setArtist_1000_1000(String artist_1000_1000) {
        this.artist_1000_1000 = artist_1000_1000;
    }

    public String getTing_uid() {
        return ting_uid;
    }

    public void setTing_uid(String ting_uid) {
        this.ting_uid = ting_uid;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getCompress_status() {
        return compress_status;
    }

    public void setCompress_status(String compress_status) {
        this.compress_status = compress_status;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAliasname() {
        return aliasname;
    }

    public void setAliasname(String aliasname) {
        this.aliasname = aliasname;
    }

    public String getArtist_480_800() {
        return artist_480_800;
    }

    public void setArtist_480_800(String artist_480_800) {
        this.artist_480_800 = artist_480_800;
    }

    public String getPic_premium() {
        return pic_premium;
    }

    public void setPic_premium(String pic_premium) {
        this.pic_premium = pic_premium;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getPlay_type() {
        return play_type;
    }

    public void setPlay_type(String play_type) {
        this.play_type = play_type;
    }

    public String getLearn() {
        return learn;
    }

    public void setLearn(String learn) {
        this.learn = learn;
    }

    public String getRelate_status() {
        return relate_status;
    }

    public void setRelate_status(String relate_status) {
        this.relate_status = relate_status;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(Song song) {
        songs.add(song);
    }
}
