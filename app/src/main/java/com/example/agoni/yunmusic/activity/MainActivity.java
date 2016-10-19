package com.example.agoni.yunmusic.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoni.yunmusic.AidlIterface;
import com.example.agoni.yunmusic.MusicService;
import com.example.agoni.yunmusic.Myapp;
import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.adapter.PlayListAdapter;
import com.example.agoni.yunmusic.bean.SongInfoDetail;
import com.example.agoni.yunmusic.fragment.DrawerContentFragment;
import com.example.agoni.yunmusic.fragment.MainContentFragment;
import com.example.agoni.yunmusic.util.DensityUtil;
import com.example.agoni.yunmusic.util.ScreenUtils;
import com.example.agoni.yunmusic.util.StaticValue;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

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
        Myapp myapp= (Myapp) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化侧边栏和主布局
        findView();
        initPlayList();
        if (myapp.getCurSongid()!=null){
            initPlaybarUI(myapp.getPlayList().get(myapp.getIndex(myapp.getCurSongid())));
            playbar_btn_play.setImageResource(R.drawable.playbar_btn_play);
            isPause=false;
            isPlaying=false;
        }
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

    private void initPlayList() {
        Myapp myapp= (Myapp) getApplication();
        List<SongInfoDetail> playList = readPlayList();
        HashMap<String, Integer> indexOfList = readIndexOfList();
        SharedPreferences sp =getSharedPreferences("config",MODE_PRIVATE);
        String curSongid = sp.getString("curSongid", null);
        int currentPosition = sp.getInt("currentPosition", 0);
        if (playList!=null&&indexOfList!=null&&curSongid!=null){
            myapp.setPlayList(playList);
            myapp.setIndexOfList(indexOfList);
            myapp.setCurSongid(curSongid);
            myapp.setCurrentPosition(currentPosition);
        }
    }

    private HashMap<String, Integer> readIndexOfList() {
        File file=new File(getCacheDir(),"indexOfList.dat");
        HashMap<String, Integer> indexOfList = (HashMap<String, Integer>) readObjectFromFile(file);
        return indexOfList;
    }

    private List<SongInfoDetail> readPlayList() {
        File file=new File(getCacheDir(),"playList.dat");
        List<SongInfoDetail> playList = (List<SongInfoDetail>) readObjectFromFile(file);
        return playList;
    }

    public Object readObjectFromFile(File file)
    {
        Object temp=null;
        if (file.exists()){
            FileInputStream in;
            try {
                in = new FileInputStream(file);
                ObjectInputStream objIn=new ObjectInputStream(in);
                temp=objIn.readObject();
                objIn.close();
                return temp;
            } catch (IOException e) {
                Log.i("tag","read object failed");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
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
        if (!TextUtils.isEmpty(songInfoDetail.getPic_small())){
            Picasso.with(this).load(songInfoDetail.getPic_small())
                    .placeholder(R.drawable.a8c)
                    .error(R.drawable.a8c).into(playbar_music_image);
        }else {
            playbar_music_image.setImageResource(R.drawable.a8c);
        }
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
                    if (myapp.getPlayList().size()<=1){
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
        playbar_btn_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myapp myapp= (Myapp) getApplication();
                List<SongInfoDetail> playList = myapp.getPlayList();
                if (playList!=null){
                    showPlayList(playList);
                }else {
                    Toast.makeText(getApplicationContext(),"播放列表为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showPlayList(List<SongInfoDetail> playList) {
        View view = LayoutInflater.from(this).inflate(R.layout.playlist, null);
        ListView listView= (ListView) view.findViewById(R.id.playlist_listview);
        listView.setAdapter(new PlayListAdapter(getApplicationContext(),playList));
        Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width= ScreenUtils.getScreenWidth(getApplicationContext());
        lp.height = DensityUtil.dip2px(getApplicationContext(), 390);

//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

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
        super.onDestroy();
        try {
            aidlIterface.saveState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(conn);//解绑服务
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
