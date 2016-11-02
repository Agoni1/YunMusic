package com.example.agoni.yunmusic.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.adapter.RunkingInfoAdapter;
import com.example.agoni.yunmusic.bean.RunkingInfo;
import com.example.agoni.yunmusic.bean.RunkingTopSonginfo;
import com.example.agoni.yunmusic.net.BMA;
import com.example.agoni.yunmusic.util.NetUitl;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/9/25.
 */
public class RankingFragment extends BaseFragment {
    private boolean hadloadNetView = false;
    private View rootview;
    private RequestQueue requestQueue;
    private View netView;//网络内容的布局view

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.loading_layout, null);
        ImageView loadingimg = (ImageView) rootview.findViewById(R.id.loadingimg);
        loadingimg.setImageResource(R.drawable.loadinganimation);
        AnimationDrawable animation = (AnimationDrawable) loadingimg.getDrawable();
        animation.start();

        // 如果要指定并发值，传入数字即可：NoHttp.newRequestQueue(3);
        requestQueue = NoHttp.newRequestQueue();
        return rootview;
    }

    @Override
    protected void delayLoad() {
        if (NetUitl.IsWifi(getContext()) || NetUitl.IsMOBILEDATA(getContext())) {
            if (!hadloadNetView) {
                Log.i("tag", "yes");
                loadNetView();
            }
        } else {
            if (!hadloadNetView) {
                Log.i("tag", "no");
                loadNoNetView();
            }
        }
    }

    private void loadNoNetView() {
        LinearLayout layout = (LinearLayout) rootview.findViewById(R.id.rootcontainer);
        layout.removeAllViews();
        View view = View.inflate(getContext(), R.layout.nonet_layout, null);
        layout.addView(view);
        hadloadNetView = false;
        TextView nonetlayoutretry = (TextView) view.findViewById(R.id.nonet_layout_retry);
        nonetlayoutretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delayLoad();
            }
        });

    }

    //加载布局
    private void loadNetView() {
        String billCategoryUrl = BMA.Billboard.billCategory();
        Log.i("tag", billCategoryUrl);
        requestString(billCategoryUrl);

    }

    private void requestString(String url) {
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        requestQueue.add(0, request, listener);
    }

    ;

    private void requestData() {

        String billSongListurl = BMA.Billboard.billSongList(1, 0, 100);
        Log.i("tag", billSongListurl);

    }

    OnResponseListener listener = new OnResponseListener() {

        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response response) {
            switch (what) {
                //获取所有榜单
                case 0: {
                    //加载布局
                    LinearLayout layout = (LinearLayout) rootview.findViewById(R.id.rootcontainer);
                    layout.removeAllViews();
                    netView = View.inflate(getContext(), R.layout.ranking_layout, null);
                    layout.addView(netView);
                    hadloadNetView = true;

                    //解析数据到runkingInfoList中
                    try {
                        List<RunkingInfo> runkingInfoList = new ArrayList<>();
                        Gson gson = new Gson();
                        String result = (String) response.get();
                        JSONObject obj = new JSONObject(result);
                        JSONArray content = obj.getJSONArray("content");
                        for (int i = 0; i < content.length(); i++) {
                            String strJson = content.getString(i);
                            RunkingInfo runkingInfo = gson.fromJson(strJson, RunkingInfo.class);

                            List<RunkingTopSonginfo> topSonginfoList = runkingInfo.getRunkingTopSonginfoList();
                            JSONObject obj1 = new JSONObject(strJson);
                            JSONArray content1 = obj1.getJSONArray("content");
                            for (int j = 0; j < content1.length(); j++) {
                                String strJson1 = content1.getString(j);
                                RunkingTopSonginfo topSonginfo = gson.fromJson(strJson1, RunkingTopSonginfo.class);
                                topSonginfoList.add(topSonginfo);
                            }
                            runkingInfoList.add(runkingInfo);
                        }

                        //加载listview数据
                        initListView(runkingInfoList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        @Override
        public void onFailed(int what, Response response) {
            loadNoNetView();
            Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void initListView(List<RunkingInfo> runkingInfoList) {
        ListView listView = (ListView) netView.findViewById(R.id.ranking_layout_listview);
        listView.setAdapter(new RunkingInfoAdapter(getContext(), runkingInfoList));
        listView.addFooterView(View.inflate(getContext(), R.layout.songlist_bottom_space, null));
    }

}
