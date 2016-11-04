package com.example.agoni.yunmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.util.StaticValue;

/**
 * Created by Agoni on 2016/11/2.
 */

public class RankingDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radio_layout,null);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        StaticValue.ISSHOWING = true;
        StaticValue.CUR_FRAGMENT = "rankingDetailFragment";
        Log.i("tag", "rankingDetailFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StaticValue.ISSHOWING = false;
        StaticValue.CUR_FRAGMENT = "null";
        Log.i("tag", "rankingDetailFragment被DestroyView了");
    }

}
