package com.example.agoni.yunmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Agoni on 2016/10/10.
 */
public class MyScrollView extends ScrollView {
    private ScrollListener mListener;

    public static interface ScrollListener {
        //声明接口，用于传递数据
        public void scrollOritention(int l, int t, int oldl, int oldt);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.scrollOritention(l, t, oldl, oldt);
        }
    }

    public void setScrollListener(ScrollListener l) {

        this.mListener = l;
    }
}