package com.example.agoni.yunmusic.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.fragment.DrawerContentFragment;
import com.example.agoni.yunmusic.fragment.MainContentFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar;
    }

    private void initView() {
        //加载侧滑菜单布局和主界面布局
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainContentFramLayout,new MainContentFragment());
        transaction.add(R.id.drawerContentFramLayout,new DrawerContentFragment());
        transaction.commit();


    }
}
