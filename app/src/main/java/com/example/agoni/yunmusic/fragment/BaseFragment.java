package com.example.agoni.yunmusic.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Agoni on 2016/11/1.
 */

public abstract class BaseFragment extends Fragment {
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            delayLoad();
        } else {
            isVisible = false;
        }
    }

    protected abstract void delayLoad();
}