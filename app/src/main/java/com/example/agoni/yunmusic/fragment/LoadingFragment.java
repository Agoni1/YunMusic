package com.example.agoni.yunmusic.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.agoni.yunmusic.R;

/**
 * Created by Agoni on 2016/9/25.
 */
public class LoadingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_layout, null);
        ImageView loadingimg= (ImageView) view.findViewById(R.id.loadingimg);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingimg.getDrawable();
        animationDrawable.start();
        return view;
    }

}
