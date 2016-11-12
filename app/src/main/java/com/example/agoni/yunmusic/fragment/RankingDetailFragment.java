package com.example.agoni.yunmusic.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoni.yunmusic.Myapp;
import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.activity.MainActivity;
import com.example.agoni.yunmusic.adapter.RankingdetailAdapter;
import com.example.agoni.yunmusic.bean.RankingDetailInfo;
import com.example.agoni.yunmusic.bean.RankingSonginfo;
import com.example.agoni.yunmusic.bean.RunkingInfo;
import com.example.agoni.yunmusic.bean.Song;
import com.example.agoni.yunmusic.bean.SongInfoDetail;
import com.example.agoni.yunmusic.net.BMA;
import com.example.agoni.yunmusic.util.BitmapUtil;
import com.example.agoni.yunmusic.util.LogUtil;
import com.example.agoni.yunmusic.util.NetUitl;
import com.example.agoni.yunmusic.util.StaticValue;
import com.example.agoni.yunmusic.view.MyScrollView;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Agoni on 2016/11/2.
 */

public class RankingDetailFragment extends Fragment {
    private Context mContext;
    private RequestQueue requestQueue;

    private RunkingInfo rankingInfo;//榜单粗略信息
    private String type=null;

    private ImageView bgimg;
    private ImageView headview;
    private TextView title;
    private ListView listView;
    private ImageView bgimg_placeholder;
    private ImageView headview_placeholder;
    private ImageView imageView;
    private TextView time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rankingdetailfragment_layout, null);
        mContext = getContext();
        requestQueue = NoHttp.newRequestQueue();
        initView(view);

        LogUtil.i(BMA.Billboard.billSongList(Integer.parseInt(type),0,20)+"hhh");
//        LogUtil.i(BMA.Song.songInfo("27191882"));

        return view;
    }

    private void initView(View view) {
        bgimg = (ImageView) view.findViewById(R.id.rankingdetail_layout_bgimg);
        headview = (ImageView) view.findViewById(R.id.rankingdetail_layout_headview);
        title = (TextView) view.findViewById(R.id.rankingdetail_layout_title);
        listView = (ListView) view.findViewById(R.id.rankingdetail_layout_listview);
        bgimg_placeholder=(ImageView)view.findViewById(R.id.rankingdetail_layout_bgimg_placeholder);
        headview_placeholder=(ImageView)view.findViewById(R.id.rankingdetail_layout_headview_placeholder);
        imageView= (ImageView) (ImageView) view.findViewById(R.id.rankingdetail_layout_image);
        final TextView rankingname= (TextView) view.findViewById(R.id.rankingdetail_layout_rankingname);
        time= (TextView) view.findViewById(R.id.rankingdetail_layout_time);

        rankingname.setText(rankingInfo.getName());

        Request<Bitmap> request = NoHttp.createImageRequest(rankingInfo.getPic_s192());
        requestQueue.add(0, request, listener);//请求图片

        Request<JSONObject> jsObject=NoHttp.createJsonObjectRequest(BMA.Billboard.billSongList(
                Integer.parseInt(rankingInfo.getType()),0,20));
        requestQueue.add(1,jsObject,listener);//请求此榜单的数据

        MyScrollView scrollView= (MyScrollView) view.findViewById(R.id.rankingdetail_layout_scrollview);
        scrollView.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                LogUtil.i(l+"\t"+t);
                if (t<=390){
                    headview.setAlpha(t/390.0f);
                    if (!title.getText().toString().equals(rankingInfo.getName())){
                        title.setText(rankingInfo.getName());
                    }
                    if (t>250){
                        title.setAlpha((t-250.0f)/(390.0f-250.0f));
                    }else{
                        title.setAlpha(0);
                    }
                }else {
                    headview.setAlpha(1.0f);
                    title.setAlpha(1.0f);
                }
            }
        });

    }

    OnResponseListener listener = new OnResponseListener() {
        @Override
        public void onStart(int what) {}

        @Override
        public void onSucceed(int what, Response response) {
            switch (what) {
                case 0: {//获取图片
                    Bitmap bitmap = (Bitmap) response.get();
                    useBitmapToUI(bitmap);
                }break;
                case 1: {//获取此榜单的数据
                    JSONObject object = (JSONObject) response.get();
                    RankingDetailInfo rankingDetailInfo = jieXi(object);
                    loadView(rankingDetailInfo);
                }
                break;
                case 2: {//获取该歌曲数据
                    JSONObject object = (JSONObject) response.get();
                    jieXiSong(object);

                }
                break;
            }

        }

        @Override
        public void onFailed(int what, Response response) {
            Toast.makeText(mContext,"网络请求失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish(int what) {}
    };

    private void jieXiSong(JSONObject object) {
        Gson gson = new Gson();
        try {
            String songinfo = object.getString("songinfo");
            SongInfoDetail songInfoDetail = gson.fromJson(songinfo, SongInfoDetail.class);
            JSONObject songurl = object.getJSONObject("songurl");
            JSONArray songArray = songurl.getJSONArray("url");
            for (int i = 0; i < songArray.length(); i++) {
                Song song = gson.fromJson(songArray.getString(i), Song.class);
                songInfoDetail.setSongs(song);
            }
            //将当前歌曲添加到播放列表中，并设置当前songid
            Myapp myapp = (Myapp) (getActivity().getApplication());
            boolean hadAdd = hadAddtoPlayList(songInfoDetail);
            if (!hadAdd) {
                int size = myapp.getPlayList().size();
                myapp.addToPlayList(songInfoDetail);//添加到列表中
                myapp.addIndex(songInfoDetail.getSong_id(),size);
            }
            myapp.setCurSongid(songInfoDetail.getSong_id());//设置当前songid
            MainActivity activity = (MainActivity) getActivity();
            activity.playsong();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean hadAddtoPlayList(SongInfoDetail songInfoDetail) {
        Myapp myapp = (Myapp) (getActivity().getApplication());
        if (myapp.getIndexOfList().get(songInfoDetail.getSong_id()) == null) {
            return false;
        }
        return true;
    }

    private void loadView(final RankingDetailInfo rankingDetailInfo) {
        if (rankingDetailInfo.getUpdate_date()!=null&&!rankingDetailInfo.getUpdate_date().equals("null")){
            time.setText("更新时间:"+rankingDetailInfo.getUpdate_date());
        }
        RankingdetailAdapter adapter = new RankingdetailAdapter(mContext, rankingDetailInfo);
        listView.setAdapter(adapter);
        View head_view = View.inflate(mContext, R.layout.songlist_head_view, null);
        TextView songnum = (TextView) head_view.findViewById(R.id.listview_head_songnumber);
        songnum.setText("("+rankingDetailInfo.getRankingSonginfoList().size()+"首)");

        listView.addHeaderView(head_view);
        listView.addFooterView(View.inflate(mContext,R.layout.songlist_bottom_space,null));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RankingSonginfo rankingSonginfo = (RankingSonginfo) parent.getItemAtPosition(position);
                requestSonginfo(rankingSonginfo);
            }
        });
    }

    private void requestSonginfo(RankingSonginfo rankingSonginfo) {
        boolean b = netIsAllow();
        if (b) {
            requestjson(rankingSonginfo);
        }
    }

    private void requestjson(RankingSonginfo rankingSonginfo) {
        Request<JSONObject> jsObject=NoHttp.createJsonObjectRequest(BMA.Song.songInfo(rankingSonginfo.getSong_id()));
        requestQueue.add(2,jsObject,listener);//请求此歌曲的数据
        LogUtil.i(BMA.Song.songInfo(rankingSonginfo.getSong_id()));
    }

    private boolean netIsAllow() {
        String netState = NetUitl.getNetState(getContext());
        if (netState.equals("NO_NET")) {
            Toast.makeText(getContext(), "网络没有连接，请连接Wifi后重试~", Toast.LENGTH_SHORT).show();
            return false;
        } else if (netState.equals("MOBILE_DATA")) {
            Toast.makeText(getContext(), "当前是数据网络，为保护您的流量，请连接Wifi后重试~", Toast.LENGTH_SHORT).show();
            return false;
        } else if (netState.equals("WIFI")) {
            return true;
        }
        Toast.makeText(getContext(), "当前网络不允许，请连接Wifi后重试~", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 解析此榜单的数据
     * @param object
     */
    private RankingDetailInfo jieXi(JSONObject object) {
        try {
            String billboard = object.getString("billboard");
            Gson gson = new Gson();
            RankingDetailInfo rankingDetailInfo = gson.fromJson(billboard, RankingDetailInfo.class);
            List<RankingSonginfo> rankingSonginfoList = rankingDetailInfo.getRankingSonginfoList();
            JSONArray song_list = object.getJSONArray("song_list");
            for (int i=0;i<song_list.length();i++){
                String string = song_list.getString(i);
                RankingSonginfo rankingSonginfo = gson.fromJson(string, RankingSonginfo.class);
                rankingSonginfoList.add(rankingSonginfo);
            }
            return rankingDetailInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void useBitmapToUI(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, h / 3 * 2, w, h / 3);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, h / 12 * 11, w, h / 12);

        Bitmap blurbitmap1 = null;
        Bitmap blurbitmap2 = null;
        if (Build.VERSION.SDK_INT < 17) {

            blurbitmap1 = BitmapUtil.fastblur(bitmap1, 25);
            blurbitmap2 = BitmapUtil.fastblur(bitmap2, 25);

        } else {

            blurbitmap1 = BitmapUtil.blur(mContext, bitmap1, 25);
            blurbitmap2 = BitmapUtil.blur(mContext, bitmap2, 25);

        }

        imageView.setImageBitmap(bitmap);
        bgimg.setImageBitmap(blurbitmap1);
        headview.setImageBitmap(blurbitmap2);

        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        bgimg_placeholder.startAnimation(animation);


    }

    @Override
    public void onResume() {
        super.onResume();
        StaticValue.ISSHOWING = true;
        StaticValue.CUR_FRAGMENT = "rankingDetailFragment";
        LogUtil.i("tag", "rankingDetailFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StaticValue.ISSHOWING = false;
        StaticValue.CUR_FRAGMENT = "null";
        LogUtil.i("tag", "rankingDetailFragment被DestroyView了");
    }

    public void setRankingInfo(RunkingInfo rankingInfo) {
        this.rankingInfo = rankingInfo;
        this.type=rankingInfo.getType();
    }
}
