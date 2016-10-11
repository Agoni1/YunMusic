package com.example.agoni.yunmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.adapter.SonglistAdapter;
import com.example.agoni.yunmusic.bean.RecommendSonglistInfo;
import com.example.agoni.yunmusic.bean.Song;
import com.example.agoni.yunmusic.util.NetUitl;
import com.example.agoni.yunmusic.util.StaticValue;
import com.example.agoni.yunmusic.view.MyScrollView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/10/9.
 */
public class RecommendSonlistDetailFragment extends Fragment {
    private RecommendSonglistInfo recommendSonglistInfo;
    private List<Song> songList;
    SonglistAdapter adapter;

    private MyScrollView scrollView;
    private ImageButton imgbtn_back, imgbtn_search, imgbtn_menu_more;
    private TextView titlebar_title, titlebar_catagory, songlist_title, songlist_desc;
    private TextView tv_img_collect, tv_img_share, tv_img_comments, tv_img_download;
    private ImageView songlist_img;
    private TextView tv_songlist_listennum;
    private ImageButton imgbtn_songlistinfo;       //图像右下角角标
    private ListView listView;

    private String gedanurl="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version" +
            "=5.6.5.6&format=json&method=baidu.ting.diy.gedanInfo&listid=";

    private String gedan_desc;
    private String title_default;//默认标题

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songlist_detail_layout, null);
        findView(view);
        initView();
        initListener();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = NetUitl.requestbyOkhttp(gedanurl + recommendSonglistInfo.getListid());
                jiexi(json);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reloadView();
                    }
                });

            }
        }).start();
        return view;
    }

    private void reloadView() {
        songlist_desc.setText(gedan_desc);//显示歌单描述
        adapter=new SonglistAdapter(getContext(),songList);
        listView.setAdapter(adapter);
        listView.addFooterView(View.inflate(getContext(),R.layout.songlist_bottom_space,null));
    }

    private void jiexi(String json) {
        try {
            JSONObject object=new JSONObject(json);
            gedan_desc = object.getString("desc");
            JSONArray content = object.getJSONArray("content");
            Gson gson =new Gson();
            songList=new ArrayList<>();
            for (int i=0;i<content.length();i++){
                Song song = gson.fromJson(content.getString(i), Song.class);
                songList.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initListener() {
        imgbtn_back.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_back: { //返回图标
                    back();
                }
                break;
                case R.id.songlist_search: {//搜索

                }
                break;
                case R.id.songlist_menu_more: {//more

                }
                break;
                case R.id.songlist_image_info: {//更多信息

                }
                break;
                case R.id.list_image: {//歌单图片

                }
                break;
                case R.id.collect_icon: {//收藏

                }
                break;
                case R.id.comments_icon: {//评论

                }
                break;
                case R.id.share_icon: {//分享

                }
                break;
                case R.id.download_icon: {//下载

                }
                break;
            }
        }
    };

    public void back() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        //找到当前的fragment，和之前被隐藏的fragment
        Fragment recommendSonlistDetailFragment = manager.findFragmentByTag("recommendSonlistDetailFragment");
        Fragment mainContentFragment = manager.findFragmentByTag("mainContentFragment");

        FragmentTransaction transaction = manager.beginTransaction();   //开启事务
        transaction.remove(recommendSonlistDetailFragment);           //移除当前fragment
        transaction.show(mainContentFragment);                       //显示被隐藏的fragment
        transaction.commit();                                        //提交

    }

    private void initView() {
        //加载歌单分类
        titlebar_catagory.setText(recommendSonglistInfo.getTag());
        //设置歌单图片、标题、听众数
        Picasso.with(getContext()).load(recommendSonglistInfo.getPic())
                .placeholder(R.drawable.a8c)
                .error(R.drawable.a8c)
                .into(songlist_img);
        songlist_title.setText(recommendSonglistInfo.getTitle());
        tv_songlist_listennum.setText(getlistenum());
    }

    private String getlistenum() {
        String num;
        int listenum = Integer.parseInt(recommendSonglistInfo.getListenum());
        if (listenum > 9999) {
            listenum = listenum / 10000;
            num = listenum + "万";
        } else {
            num = listenum + "";
        }
        return num;
    }

    private void findView(View view) {
        scrollView = (MyScrollView) view.findViewById(R.id.songlist_scrollview);
        imgbtn_back = (ImageButton) view.findViewById(R.id.img_back);
        imgbtn_search = (ImageButton) view.findViewById(R.id.songlist_search);
        imgbtn_menu_more = (ImageButton) view.findViewById(R.id.songlist_menu_more);
        titlebar_title = (TextView) view.findViewById(R.id.titlebar_title);
        titlebar_title.setSelected(true);
        titlebar_catagory = (TextView) view.findViewById(R.id.songlist_catagory);
        songlist_title = (TextView) view.findViewById(R.id.songlist_title);
        songlist_desc = (TextView) view.findViewById(R.id.songlist_desc);
        tv_img_collect = (TextView) view.findViewById(R.id.collect_icon);
        tv_img_comments = (TextView) view.findViewById(R.id.comments_icon);
        tv_img_share = (TextView) view.findViewById(R.id.share_icon);
        tv_img_download = (TextView) view.findViewById(R.id.download_icon);
        songlist_img = (ImageView) view.findViewById(R.id.list_image);
        tv_songlist_listennum = (TextView) view.findViewById(R.id.list_listennum);
        imgbtn_songlistinfo = (ImageButton) view.findViewById(R.id.songlist_image_info);
        listView= (ListView) view.findViewById(R.id.songlist_listview);
        listView.setFocusable(false);//防止listview加载完数据自动对齐到顶部
        title_default = titlebar_title.getText().toString();//默认标题
        final View headview = view.findViewById(R.id.songlist_detail_headview);


        scrollView.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                Log.i("tag","\tl:"+l+"\tt:"+t+"\toldl:"+oldl+"\toldt:"+oldt);
                //设置标题栏透明度
                float alpha = t / 260.0f;
                if (alpha>1.0f){
                    alpha=1.0f;
                }
                headview.setAlpha(alpha);
                //设置标题文字
                if(t>260){
                    boolean b = titlebar_title.getText().toString().equals(title_default);
                    if (b){
                        titlebar_title.setText(recommendSonglistInfo.getTitle());
                    }
                }else {
                    titlebar_title.setText(title_default);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void setSonglistInfo(RecommendSonglistInfo recommendSonglistInfo) {
        this.recommendSonglistInfo = recommendSonglistInfo;
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticValue.ISSHOWING = true;
        StaticValue.CUR_FRAGMENT = "recommendSonlistDetailFragment";
        Log.i("tag", "RecommendSonlistDetailFragment正在显示");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StaticValue.ISSHOWING = false;
        StaticValue.CUR_FRAGMENT = "null";
        Log.i("tag", "RecommendSonlistDetailFragment被DestroyView了");
    }

}
