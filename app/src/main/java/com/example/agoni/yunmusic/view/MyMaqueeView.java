package com.example.agoni.yunmusic.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Agoni on 2016/10/11.
 */
public class MyMaqueeView extends TextView {
    public MyMaqueeView(Context context) {
        super(context);
    }

    public MyMaqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMaqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

    }
}
