package com.example.agoni.yunmusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.agoni.yunmusic.activity.MainActivity;
import com.example.agoni.yunmusic.bean.Song;
import com.example.agoni.yunmusic.bean.SongInfoDetail;
import com.example.agoni.yunmusic.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

/**
 * Created by Agoni on 2016/10/15.
 */
public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener {
    private Myapp myapp;
    private AudioManager am;
    private MediaPlayer mediaPlayer;
    private Context mContext;
    private final int PLAY = 0x00;
    private final int PAUSE = 0x01;
    private final int STOP = 0x02;
    public final static String ACTION_PLAY = "com.example.agoni.yunmusic.action.play";
    public final static String ACTION_NEXT = "com.example.agoni.yunmusic.action.next";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    AidlIterface.Stub binder = new AidlIterface.Stub() {
        @Override
        public void play() throws RemoteException {
            String curSongid = myapp.getCurSongid();
            if (curSongid != null) {
                // Request audio focus for playback
                am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                int result = am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // 开始播放音乐文件
                    int index = myapp.getIndex(curSongid);
                    Log.i("tag", "curSongid=" + curSongid);
                    Log.i("tag", myapp.getPlayList().size() + "");
                    Log.i("tag", index + "");
                    SongInfoDetail songInfoDetail = myapp.getPlayList().get(index);
                    initSong(songInfoDetail);
                }
            } else {
                MainActivity.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "当前列表为空", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void next() throws RemoteException {
            musicNext();
        }

        @Override
        public void previous() throws RemoteException {
            Log.i("tag", "previous");
        }

        @Override
        public void pause() throws RemoteException {
            musicPause();
        }

        @Override
        public void restart() throws RemoteException {
            musicRestart();
        }

        @Override
        public void saveState() throws RemoteException {
            savePlayList();//保存播放列表
            savePlayInfo();//保存播放信息
            mediaPlayer.release();
        }
    };

    private void musicNext() {
        Log.i("tag", "next");
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            playNext();
        }
    }

    private void musicRestart() {
        Log.i("tag", "restart");
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.start();
            showNotification(PLAY);
            MainActivity.isPlaying = true;
            MainActivity.isPause = false;
            updatePlayUi();
        }
    }

    private void musicPause() {
        Log.i("tag", "pause");
        mediaPlayer.pause();
        showNotification(PAUSE);
        MainActivity.isPlaying = false;
        MainActivity.isPause = true;
        updatePlayUi();
    }

    private void updatePlayUi() {
        MainActivity mainActivity = (MainActivity) MainActivity.context;
        mainActivity.updatePlayUi();
    }

    private void savePlayInfo() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String curSongid = myapp.getCurSongid();
        if (curSongid != null) {
            edit.putString("curSongid", curSongid);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            int currentPosition = mediaPlayer.getCurrentPosition();
            Log.i("tag", "currentPosition=" + currentPosition);
            edit.putInt("currentPosition", currentPosition);
        }
        edit.apply();
    }

    private void savePlayList() {
        List<SongInfoDetail> playList = myapp.getPlayList();
        if (playList != null) {
            File file1 = new File(getCacheDir(), "playList.dat");
            writeObjToFile(playList, file1);//写入歌单
        }
        HashMap<String, Integer> indexOfList = myapp.getIndexOfList();
        if (indexOfList != null) {
            File file2 = new File(getCacheDir(), "indexOfList.dat");//写入索引
            writeObjToFile(indexOfList, file2);
        }
    }

    private void writeObjToFile(Object obj, File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objout = new ObjectOutputStream(out);
            objout.writeObject(obj);
            objout.flush();
            objout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSong(SongInfoDetail songInfoDetail) {
        List<Song> songs = songInfoDetail.getSongs();
        Song song = getMostBitrateSong(songs);
        playSong(song);
        myapp.setCurSongid(songInfoDetail.getSong_id());

        //播放歌曲，通知主线程更新ui
        Message msg = Message.obtain();
        msg.what = 100;
        msg.obj = songInfoDetail;
        MainActivity.handler.sendMessage(msg);

        //发送通知
        showNotification(PLAY);

    }

    private void showNotification(final int flag) {
        final SongInfoDetail songInfoDetail = myapp.getPlayList().get(myapp.getIndex(myapp.getCurSongid()));
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.yunmusic_icon)//设置状态栏的小图标，在某些手机上不会生效，如华为，强制使用应用图标
                .setTicker("正在播放音乐")
                .setContentTitle("通知标题")
                .setContentText("通知内容")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(false)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true);
        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_small_layout);
        remoteViews.setTextViewText(R.id.notification_songName, songInfoDetail.getTitle() + "");
        remoteViews.setTextViewText(R.id.notification_songAuthor, songInfoDetail.getAuthor() + "");
        //开个线程去获取歌曲图片，并设置给notification中去
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(songInfoDetail.getPic_small()).openStream());
                    remoteViews.setImageViewBitmap(R.id.notification_image, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    remoteViews.setImageViewResource(R.id.notification_image, R.drawable.a8c);
                }finally {
                    if (flag == PLAY) {
                        remoteViews.setImageViewResource(R.id.notification_play, R.drawable.playbar_btn_pause);
                    }
                    if (flag == PAUSE) {
                        remoteViews.setImageViewResource(R.id.notification_play, R.drawable.playbar_btn_play);
                    }

                    //注册notification中的播放、暂停按钮的点击事件————发送广播
                    Intent btn_paly_intent = new Intent(ACTION_PLAY);
                    PendingIntent pi1 = PendingIntent.getBroadcast(mContext, 1, btn_paly_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.notification_play, pi1);

                    //注册notification中的下一曲按钮的点击事件————发送广播
                    Intent btn_next_intent = new Intent(ACTION_NEXT);
                    PendingIntent pi2 = PendingIntent.getBroadcast(mContext, 2, btn_next_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.notification_next,pi2);

                    builder.setContent(remoteViews);//设置自定义布局

                    Intent intent = new Intent(mContext, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    startForeground(100, notification);
                }
            }
        }).start();
    }

    //获取比特率最大的歌
    private Song getMostBitrateSong(List<Song> songs) {
        int index = 0;
        int mostBitrate = 0;
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            if (TextUtils.isEmpty(song.getFile_link())) {
                continue;
            } else if (song.getFile_bitrate().equals("flac")) {
                index = i;
                break;
            } else {
                int bitrate = Integer.parseInt(song.getFile_bitrate());
                if (bitrate > mostBitrate) {
                    mostBitrate = bitrate;
                    index = i;
                }
            }
        }
        return songs.get(index);
    }

    private void playSong(final Song song) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.getFile_link());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.seekTo(myapp.getCurrentPosition());
                    mediaPlayer.start();
                    myapp.setCurrentPosition(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        Log.i("tag", "seronCreate");
        mContext = this;
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        myapp = (Myapp) getApplicationContext();
        //注册广播接收者
        NotificationButtonReceiver receiver = new NotificationButtonReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PLAY);
        mContext.registerReceiver(receiver, intentFilter);
        //注册广播接收者
        NotificationNextReceiver nextReceiver=new NotificationNextReceiver();
        IntentFilter intentFilter2=new IntentFilter(ACTION_NEXT);
        mContext.registerReceiver(nextReceiver,intentFilter2);

    }

    private void playNext() {
        int curindex = myapp.getIndex(myapp.getCurSongid());
        if (curindex == myapp.getPlayList().size() - 1) {
            curindex = 0;
        } else {
            curindex = curindex + 1;
        }
        SongInfoDetail songInfoDetail = myapp.getPlayList().get(curindex);
        initSong(songInfoDetail);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Message msg = Message.obtain();
                msg.what = 200;//播放歌曲，通知主线程更新ui
                MainActivity.handler.sendMessage(msg);

                Log.i("tag", "AUDIOFOCUS_LOSS_TRANSIENT");
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume playback
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Message msg = Message.obtain();
                msg.what = 300;//继续播放歌曲，通知主线程更新ui
                MainActivity.handler.sendMessage(msg);

                Log.i("tag", "AUDIOFOCUS_LOSS_TRANSIENT");
                Log.i("tag", "AUDIOFOCUS_GAIN");

            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            am.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext);
            // Stop playback
            mediaPlayer.pause();
            Message msg = Message.obtain();
            msg.what = 200;//播放歌曲，通知主线程更新ui
            MainActivity.handler.sendMessage(msg);
            Log.i("tag", "AUDIOFOCUS_LOSS");

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.setVolume(0.1f, 0.1f);
                Log.i("tag", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
            }
        }
    }

    /**
     * notification播放、暂停按钮的广播接收者
     */
    public class NotificationButtonReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i("收到广播");
            if (MainActivity.isPlaying) {
                LogUtil.i("音乐暂停");
                musicPause();
            } else if (MainActivity.isPause) {
                LogUtil.i("音乐续播");
                musicRestart();
            }
        }
    }

    /**
     * notification下一曲按钮的广播接收者
     */
    public class NotificationNextReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i("收到下一曲广播");
            musicNext();
        }
    }
}
