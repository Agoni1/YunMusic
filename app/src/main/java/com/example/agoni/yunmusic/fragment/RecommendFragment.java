package com.example.agoni.yunmusic.fragment;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/9/25.
 */
public class RecommendFragment extends Fragment {
    private List<ImageView> imglist;
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
        initData();//初始化数据
        initView(view);//初始化view
        handler.sendEmptyMessageDelayed(SCROLL_START, timelenth);
        return view;

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
                Log.i("tag", distance + "");

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
//                            Log.i("tag","SCROLL_STATE_DRAGGING");
                    }
                    break;
                    case ViewPager.SCROLL_STATE_SETTLING: {
//                            Log.i("tag","SCROLL_STATE_SETTLING");
                        handler.removeMessages(SCROLL_START);//移除轮播消息
                    }
                    break;
                    case ViewPager.SCROLL_STATE_IDLE: {
                        Log.i("tag", "SCROLL_STATE_IDLE");
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
