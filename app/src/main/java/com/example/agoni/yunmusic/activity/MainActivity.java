package com.example.agoni.yunmusic.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.fragment.DrawerContentFragment;
import com.example.agoni.yunmusic.fragment.MainContentFragment;
import com.example.agoni.yunmusic.util.StaticValue;

public class MainActivity extends FragmentActivity {
    private Fragment mainContentFragment;
    private Fragment drawerContentFragment;

    private ImageView playbar_music_image;
    private TextView playbar_music_author;
    private TextView playbar_music_title;
    private ImageButton playbar_btn_playlist;
    private ImageButton playbar_btn_play;
    private ImageButton playbar_btn_next;

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
            if (StaticValue.ISSHOWING) {
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

        if (mainContentFragment == null) {
            Log.i("tag", "mainContentFragment==null");
        }
        if (cur_fragment == null) {
            Log.i("tag", "cur_fragment==null");
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(cur_fragment);
        transaction.show(mainContentFragment);
        transaction.commit();
    }


    private void findView() {
        playbar_music_image = (ImageView) findViewById(R.id.playbar_music_image);
        playbar_music_title = (TextView) findViewById(R.id.playbar_music_title);
        playbar_music_author = (TextView) findViewById(R.id.playbar_music_author);
        playbar_btn_playlist = (ImageButton) findViewById(R.id.playbar_btn_playlist);
        playbar_btn_play = (ImageButton) findViewById(R.id.playbar_btn_play);
        playbar_btn_next = (ImageButton) findViewById(R.id.playbar_btn_next);
    }

}
