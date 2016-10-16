package com.example.agoni.yunmusic.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.adapter.RecommendMVAdapter;
import com.example.agoni.yunmusic.adapter.RecommendRadioAdapter;
import com.example.agoni.yunmusic.adapter.RecommendSonglistAdapter;
import com.example.agoni.yunmusic.bean.FocusImage;
import com.example.agoni.yunmusic.bean.RecommendMV;
import com.example.agoni.yunmusic.bean.RecommendRadio;
import com.example.agoni.yunmusic.bean.RecommendSonglistInfo;
import com.example.agoni.yunmusic.util.LogUtil;
import com.example.agoni.yunmusic.util.NetUitl;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Agoni on 2016/9/25.
 */
public class RecommendFragment extends Fragment {
    private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&" +
            "version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=" +
            "89CF1E1A06826F9AB95A34DC0F6AAA14";
    private List<ImageView> imglist;
    private List<RecommendSonglistInfo> recommendSonglist = new ArrayList<>();
    private List<RecommendRadio> recommendRadioList = new ArrayList<>();
    private List<RecommendMV> recommendMVList = new ArrayList<>();
    private List<FocusImage> focusImageList = new ArrayList<>();

    private RecommendSonglistAdapter recommendSonglistAdapter;
    private RecommendRadioAdapter recommendRadioAdapter;
    private RecommendMVAdapter recommendMVAdapter;

    private FragmentManager manager;
    private View bottomView;
    private ViewPager viewpager;
    private long timelenth = 5000;//轮播的间隔时长
    private final int SCROLL_START = 0x12;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCROLL_START: {
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                }
                break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recommend_layout, null);
        bottomView = view.findViewById(R.id.bottom_tips);
        manager = getActivity().getSupportFragmentManager();

        //检查是否有缓存
        final String cache = getCache();

        //检查有没有网络
        String netState = NetUitl.getNetState(getContext());
        Log.i("tag", "net--->" + netState);

        //没有网络
        if (netState.equals("NO_NET")) {
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.music_gridview_content);
            if (cache != null) {
                View view_loading = inflater.inflate(R.layout.loading_layout, null);
                linearLayout.addView(view_loading);
                //读取缓存，读取排序，按顺序加载数据
                LogUtil.i("tag", "有缓存");
                jiexi(cache);
                initAdapter();
                initView(view, inflater, linearLayout, view_loading);
            } else {
                View view_nonet = inflater.inflate(R.layout.nonet_layout, null);
                linearLayout.addView(view_nonet);
            }
        }

        //数据网络
        if (netState.equals("MOBILE_DATA")) {

        }

        //wifi网络
        if (netState.equals("WIFI")) {
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.music_gridview_content);
            final View view_loading = inflater.inflate(R.layout.loading_layout, null);
            linearLayout.addView(view_loading);

            //下载，缓存，加载
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取服务端的json
                    String result = NetUitl.request(url);
                    //如果数据变化就缓存新数据
                    if (!result.equals(cache)) {
                        cacheJson(result);
                    }
                    jiexi(result);
                    initAdapter();
                    //更新界面
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView(view, inflater, linearLayout, view_loading);
                        }
                    });
                }
            }).start();
        }
        return view;

    }


    private void initView(View view, final LayoutInflater inflater, LinearLayout linearLayout, View view_loading) {
        View view1 = inflater.inflate(R.layout.grid_recommend_songlist, null);
        View view2 = inflater.inflate(R.layout.grid_recommend_mv, null);
        View view3 = inflater.inflate(R.layout.grid_recommend_radio, null);

        GridView recommend_songlist_gridview = (GridView) view1.findViewById(R.id.tv_recommend_songlist_gridview);
        GridView recommend_mv_gridview = (GridView) view2.findViewById(R.id.tv_recommend_mv_gridview);
        GridView recommend_radio_gridview = (GridView) view3.findViewById(R.id.tv_recommend_radio_gridview);

        recommend_songlist_gridview.setAdapter(recommendSonglistAdapter);
        recommend_mv_gridview.setAdapter(recommendMVAdapter);
        recommend_radio_gridview.setAdapter(recommendRadioAdapter);

        recommend_songlist_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecommendSonglistInfo songlistInfo = recommendSonglist.get(position);

                List<Fragment> fragments = manager.getFragments();
                String tag = fragments.get(0).getTag();
                Log.i("tag", tag);

                FragmentTransaction transaction = manager.beginTransaction();
                RecommendSonlistDetailFragment recommendSonlistDetailFragment = new RecommendSonlistDetailFragment();
                recommendSonlistDetailFragment.setSonglistInfo(recommendSonglist.get(position));
                transaction.add(R.id.mainContentFramLayout,recommendSonlistDetailFragment,"recommendSonlistDetailFragment");
                transaction.hide(fragments.get(0));
                transaction.show(recommendSonlistDetailFragment);
                transaction.commit();
            }
        });

        recommend_mv_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean allow = netIsAllow();
            }
        });

        recommend_radio_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean allow = netIsAllow();
            }
        });

        linearLayout.removeView(view_loading);
        linearLayout.addView(view1);
        linearLayout.addView(view2);
        linearLayout.addView(view3);

        bottomView.setVisibility(View.VISIBLE);

        reloadViewPager(view);
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

    private void initAdapter() {
        recommendSonglistAdapter = new RecommendSonglistAdapter(getContext(), recommendSonglist);
        recommendRadioAdapter = new RecommendRadioAdapter(getContext(), recommendRadioList);
        recommendMVAdapter = new RecommendMVAdapter(getContext(), recommendMVList);
    }


    /**
     * 解析获取的json数据
     *
     * @param result
     */
    private void jiexi(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray_focus = jsonObject.getJSONObject("result").
                    getJSONObject("focus").getJSONArray("result");//轮播图
            JSONArray jsonArray_gedan = jsonObject.getJSONObject("result").
                    getJSONObject("diy").getJSONArray("result");//推荐歌单
            JSONArray jsonArray_mv = jsonObject.getJSONObject("result").
                    getJSONObject("mix_5").getJSONArray("result");//最新mv
            JSONArray jsonArray_radio = jsonObject.getJSONObject("result").
                    getJSONObject("radio").getJSONArray("result");//电台

            //将jsonarray解析到cls对应的bean对象中，并将bean添加到list集合中
            getBeanFromJson(jsonArray_gedan, RecommendSonglistInfo.class, recommendSonglist);
            getBeanFromJson(jsonArray_radio, RecommendRadio.class, recommendRadioList);
            getBeanFromJson(jsonArray_mv, RecommendMV.class, recommendMVList);
            getBeanFromJson(jsonArray_focus, FocusImage.class, focusImageList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cacheJson(String result) {
        SharedPreferences sp = getContext().getSharedPreferences("cache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("json", result);
        editor.apply();
    }

    /**
     * 将jsonarray解析到cls对应的bean对象中，并将bean添加到list集合中
     *
     * @param jsonArray
     * @param cls
     * @param list
     */
    private void getBeanFromJson(JSONArray jsonArray, Class<?> cls, List list) {
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(gson.fromJson(jsonArray.getString(i), cls));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查缓存状态
     */
    private String getCache() {
        SharedPreferences sp = getContext().getSharedPreferences("cache", Context.MODE_PRIVATE);
        String json = sp.getString("json", null);
        if (json != null) {
            return json;
        }
        return null;
    }


    private void reloadViewPager(View view) {
        //绘制指示器
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.indicatorContainor);
        for (int i = 0; i < focusImageList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.shape_point_grey);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
            imageView.setLayoutParams(layoutParams);
            linearLayout.addView(imageView);
        }

        //初始化指示器的红点
        final ImageView indicatorPoint = (ImageView) view.findViewById(R.id.indicatorPoint);
        indicatorPoint.setVisibility(View.VISIBLE);

        viewpager = (ViewPager) view.findViewById(R.id.ad_image_viewpager);
        viewpager.setAdapter(new MyPageradapter());
        viewpager.setCurrentItem(100 * focusImageList.size());//初始时将当前的页面索引值设得比较大，防止往左滑到底
        handler.sendEmptyMessageDelayed(SCROLL_START, timelenth);//发送轮播消息
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //监听页面的移动，让指示器跟随移动
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = position % focusImageList.size();
                if (position < 0) {
                    position = focusImageList.size() + position;
                }
                //计算两个圆点的间距
                final int distance = linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft();

                //移动小红点的位置
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = distance * position;
                indicatorPoint.setLayoutParams(layoutParams);
            }

            //监听状态变化，viewpager每次切换都会触发此方法
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING: {

                    }
                    break;
                    case ViewPager.SCROLL_STATE_SETTLING: {
                        handler.removeMessages(SCROLL_START);//移除轮播消息
                    }
                    break;
                    case ViewPager.SCROLL_STATE_IDLE: {
                        handler.sendEmptyMessageDelayed(SCROLL_START, timelenth);//发送轮播消息
                    }
                    break;
                }


            }
        });

        //监听viewpager的触摸事件，当按住的时候，停止轮播，抬起时继续。注意将返回值设为flase
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        handler.removeMessages(SCROLL_START);
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {

                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        handler.sendEmptyMessageDelayed(SCROLL_START, timelenth);
                    }
                    break;
                }
                return false;
            }
        });
    }


    class MyPageradapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % focusImageList.size();
            if (position < 0) {
                position = focusImageList.size() + position;
            }
            try {
//                Bitmap bitmap = BitmapFactory.decodeFile(getContext().getExternalCacheDir().getAbsolutePath() +
//                        "/" + MD5.md5Encode(focusImageList.get(position).getRandpic()));
//                ImageView view = new ImageView(getContext());
//                view.setScaleType(ImageView.ScaleType.FIT_XY);
//                view.setImageBitmap(bitmap);
                ImageView view = new ImageView(getContext());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(getContext()).load(focusImageList.get(position).getRandpic())
                        .placeholder(R.drawable.a8c)
                .error(R.drawable.a8c).into(view);


                //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = view.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(view);
                }
                container.addView(view);
                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Warning：不要在这里调用removeView
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
