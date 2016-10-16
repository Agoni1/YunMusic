package com.example.agoni.yunmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoni.yunmusic.AidlIterface;
import com.example.agoni.yunmusic.MusicService;
import com.example.agoni.yunmusic.Myapp;
import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.SongInfoDetail;
import com.example.agoni.yunmusic.fragment.DrawerContentFragment;
import com.example.agoni.yunmusic.fragment.MainContentFragment;
import com.example.agoni.yunmusic.util.StaticValue;
import com.squareup.picasso.Picasso;

public class MainActivity extends FragmentActivity {
    private boolean isPlaying = false;
    private boolean isPause = false;
    public static Handler handler;
    private Fragment mainContentFragment;
    private Fragment drawerContentFragment;

    private ImageView playbar_music_image;
    private TextView playbar_music_author;
    private TextView playbar_music_title;
    private ImageButton playbar_btn_playlist;
    private ImageButton playbar_btn_play;
    private ImageButton playbar_btn_next;

    private AidlIterface aidlIterface;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlIterface = AidlIterface.Stub.asInterface(service);//获取服务传来的binder
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化侧边栏和主布局
        findView();
        initplaybarlistener();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 100) {
                    SongInfoDetail songInfoDetail = (SongInfoDetail) msg.obj;
                    initPlaybarUI(songInfoDetail);
                }
            }
        };
    }

    private void findView() {
        playbar_music_image = (ImageView) findViewById(R.id.playbar_music_image);
        playbar_music_title = (TextView) findViewById(R.id.playbar_music_title);
        playbar_music_author = (TextView) findViewById(R.id.playbar_music_author);
        playbar_btn_playlist = (ImageButton) findViewById(R.id.playbar_btn_playlist);
        playbar_btn_play = (ImageButton) findViewById(R.id.playbar_btn_play);
        playbar_btn_next = (ImageButton) findViewById(R.id.playbar_btn_next);
    }

    private void initPlaybarUI(SongInfoDetail songInfoDetail) {
        Picasso.with(this).load(songInfoDetail.getPic_small())
                .placeholder(R.drawable.a8c)
                .error(R.drawable.a8c).into(playbar_music_image);
        playbar_music_title.setText(songInfoDetail.getTitle());
        playbar_music_author.setText(songInfoDetail.getAuthor());
        isPlaying = true;
        isPause = false;
        playbar_btn_play.setImageResource(R.drawable.playbar_btn_pause);
    }

    //设置监听事件
    private void initplaybarlistener() {
        playbar_btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isPlaying) {
                        aidlIterface.pause();
                        isPause = true;
                        isPlaying = false;
                        playbar_btn_play.setImageResource(R.drawable.playbar_btn_play);

                    } else if (isPause) {
                        aidlIterface.restart();
                        isPlaying = true;
                        isPause = false;
                        playbar_btn_play.setImageResource(R.drawable.playbar_btn_pause);
                    } else {
                        Myapp myapp = (Myapp) getApplication();
                        if (myapp.getCurSongid() != null) {
                            aidlIterface.play();
                        } else {
                            Toast.makeText(getApplicationContext(), "播放列表为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        playbar_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myapp myapp = (Myapp) getApplication();
                if (myapp.getCurSongid() != null){
                    if (myapp.getSongList().size()<=1){
                        Toast.makeText(getApplicationContext(), "没有更多歌曲", Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            aidlIterface.next();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "播放列表为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    @Override
    protected void onDestroy() {
        unbindService(conn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public void playsong() {
        try {
            aidlIterface.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
