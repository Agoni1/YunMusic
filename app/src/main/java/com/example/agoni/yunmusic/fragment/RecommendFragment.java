package com.example.agoni.yunmusic.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.FocusImage;
import com.example.agoni.yunmusic.bean.RecommendMV;
import com.example.agoni.yunmusic.bean.RecommendRadio;
import com.example.agoni.yunmusic.bean.RecommendSonglistInfo;
import com.example.agoni.yunmusic.util.LogUtil;
import com.example.agoni.yunmusic.util.MD5;
import com.example.agoni.yunmusic.util.NetUitl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/9/25.
 */
public class RecommendFragment extends Fragment {
    private List<ImageView> imglist;
    private List<RecommendSonglistInfo> recommendSonglist = new ArrayList<>();
    private List<RecommendRadio> recommendRadioList = new ArrayList<>();
    private List<RecommendMV> recommendMVList = new ArrayList<>();
    private List<FocusImage> focusImageList = new ArrayList<>();
    private File imageCacheDir;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_layout, null);
        imageCacheDir=getContext().getExternalCacheDir();
        if (imageCacheDir.mkdir()){
            Log.i("tag","文件不存在");
            Log.i("tag","创建文件夹");
        }else {
            Log.i("tag","文件存在--->"+imageCacheDir.getAbsolutePath());
            Log.i("tag","内部缓存--->"+getContext().getCacheDir().getAbsolutePath());
        }
        //首先检查有没有网络
        String netState = NetUitl.getNetState(getContext());

        //没有网络
        if (netState.equals("NO_NET")) {
            //检查是否有缓存
            boolean isCache = checkCache();
            if (isCache) {
                //读取缓存，读取排序，按顺序加载数据
            } else {
                //显示无网络界面
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.music_gridview_content);
                View nonet_layout = inflater.inflate(R.layout.nonet_layout, null);
                linearLayout.addView(nonet_layout);
            }
        }

        //数据网络
        if (netState.equals("MOBILE_DATA")) {
            //检查是否有缓存
            boolean isCache = checkCache();
            if (isCache) {
                //读取缓存，读取排序，按顺序加载数据
            } else {
                //显示无网络界面
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.music_gridview_content);
                View nonet_layout = inflater.inflate(R.layout.nonet_layout, null);
                linearLayout.addView(nonet_layout);
            }
        }

        //wifi网络
        if (netState.equals("WIFI")) {
            //检查是否有缓存
            boolean isCache = checkCache();
            if (isCache) {
                //读取缓存，读取排序，按顺序加载数据
            }
            //下载，缓存，加载
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = NetUitl.request("http://tingapi.ting.baidu.com/v1/restserver/" +
                            "ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=" +
                            "baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14");
                    jiexi(result);

                }
            }).start();


            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.music_gridview_content);
            View view1 = inflater.inflate(R.layout.grid_recommend_songlist, null);
            View view2 = inflater.inflate(R.layout.grid_recommend_mv, null);
            View view3 = inflater.inflate(R.layout.grid_recommend_radio, null);

            linearLayout.addView(view2);
            linearLayout.addView(view1);
            linearLayout.addView(view3);

        }


        initData();//初始化数据
        initView(view);//初始化view
        handler.sendEmptyMessageDelayed(SCROLL_START, timelenth);
        return view;

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
            LogUtil.i("tag", recommendSonglist.get(2).getTitle());
            LogUtil.i("tag", recommendRadioList.get(2).getTitle());
            LogUtil.i("tag", recommendMVList.get(2).getTitle());
            LogUtil.i("tag", focusImageList.get(2).getRandpic_desc());

            cacheFocusimage(focusImageList);    //缓存focus图片
            cacheGedanimage(recommendSonglist);      //缓存歌单图片
            cacheRadioimage(recommendRadioList);      //缓存电台图片
            cacheMvimage(recommendMVList);      //缓存MV图片

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cacheMvimage(List<RecommendMV> recommendMVList) {
        for (RecommendMV i:recommendMVList){
            String url = i.getPic();
            new MyAsyncTask(url).execute();
        }
    }

    private void cacheRadioimage(List<RecommendRadio> recommendRadioList) {
        for (RecommendRadio i:recommendRadioList){
            String url = i.getPic();
            new MyAsyncTask(url).execute();
        }
    }

    private void cacheGedanimage(List<RecommendSonglistInfo> recommendSonglist) {
        for (RecommendSonglistInfo i:recommendSonglist){
            String url = i.getPic();
            new MyAsyncTask(url).execute();
        }
    }

    private void cacheFocusimage(List<FocusImage> focusImageList) {
        for (FocusImage i:focusImageList){
            String url = i.getRandpic();
            new MyAsyncTask(url).execute();
        }
    }

    class MyAsyncTask extends AsyncTask<String, Integer, Long> {
        String url;
        MyAsyncTask(String url){
            this.url=url;
        }

        @Override
        protected Long doInBackground(String... params) {
            byte[] bytes = NetUitl.requestforBytebyOkhttp(url);
            try {
                File file = new File(imageCacheDir, MD5.md5Encode(url));
                FileOutputStream outputStream =new FileOutputStream(file,false);
                outputStream.write(bytes);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {}

        protected void onPostExecute(Long result) {}
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
    private boolean checkCache() {
        return false;
    }


    private void initView(View view) {
        //绘制指示器
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.indicatorContainor);
        for (int i = 0; i < imglist.size(); i++) {
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

        viewpager = (ViewPager) view.findViewById(R.id.ad_image_viewpager);
        viewpager.setAdapter(new MyPageradapter());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //监听页面的移动，让指示器跟随移动
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = position % imglist.size();
                if (position < 0) {
                    position = imglist.size() + position;
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
        viewpager.setCurrentItem(100 * imglist.size());//初始时将当前的页面索引值设得比较大，防止往左滑到底
    }


    class MyPageradapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % imglist.size();
            if (position < 0) {
                position = imglist.size() + position;
            }
            ImageView view = imglist.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;
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

    private void initData() {
        imglist = new ArrayList<>();

        ImageView imageView1 = new ImageView(getContext());
        imageView1.setBackgroundResource(R.drawable.p1);
        imglist.add(imageView1);

        ImageView imageView2 = new ImageView(getContext());
        imageView2.setBackgroundResource(R.drawable.p2);
        imglist.add(imageView2);

        ImageView imageView3 = new ImageView(getContext());
        imageView3.setBackgroundResource(R.drawable.p3);
        imglist.add(imageView3);

        ImageView imageView4 = new ImageView(getContext());
        imageView4.setBackgroundResource(R.drawable.p4);
        imglist.add(imageView4);

        ImageView imageView5 = new ImageView(getContext());
        imageView5.setBackgroundResource(R.drawable.p5);
        imglist.add(imageView5);

        ImageView imageView6 = new ImageView(getContext());
        imageView6.setBackgroundResource(R.drawable.p6);
        imglist.add(imageView6);

    }
}
