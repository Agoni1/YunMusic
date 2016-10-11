package com.example.agoni.yunmusic.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.fragment.DrawerContentFragment;
import com.example.agoni.yunmusic.fragment.MainContentFragment;
import com.example.agoni.yunmusic.util.StaticValue;

public class MainActivity extends FragmentActivity {
    private Fragment mainContentFragment;
    private Fragment drawerContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mainContentFragment = new MainContentFragment();
        drawerContentFragment = new DrawerContentFragment();

        //加载侧滑菜单布局和主界面布局
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainContentFramLayout, mainContentFragment, "mainContentFragment");
        transaction.add(R.id.drawerContentFramLayout, drawerContentFragment, "drawerContentFragment");
        transaction.commit();

    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (StaticValue.ISSHOWING){
                backMainActivity();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出当前显示的fragment，回到主activity
    private void backMainActivity() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment mainContentFragment = manager.findFragmentByTag("mainContentFragment");
        Fragment cur_fragment = manager.findFragmentByTag(StaticValue.CUR_FRAGMENT);

        if (mainContentFragment==null){
            Log.i("tag","mainContentFragment==null");
        }
        if (cur_fragment==null){
            Log.i("tag","cur_fragment==null");
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(cur_fragment);
        transaction.show(mainContentFragment);
        transaction.commit();
    }
}
