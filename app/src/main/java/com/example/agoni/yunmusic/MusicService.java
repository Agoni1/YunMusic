package com.example.agoni.yunmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.agoni.yunmusic.activity.MainActivity;
import com.example.agoni.yunmusic.bean.Song;
import com.example.agoni.yunmusic.bean.SongInfoDetail;

import java.io.IOException;
import java.util.List;

/**
 * Created by Agoni on 2016/10/15.
 */
public class MusicService extends Service {
    private Myapp myapp;
    private MediaPlayer mediaPlayer;

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
                int index = myapp.getIndex(curSongid);
                Log.i("tag", "curSongid=" + curSongid);
                Log.i("tag", myapp.getPlayList().size() + "");
                Log.i("tag", index + "");
                SongInfoDetail songInfoDetail = myapp.getPlayList().get(index);
                initSong(songInfoDetail);
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
            Log.i("tag", "next");
            playNext();

        }

        @Override
        public void previous() throws RemoteException {
            Log.i("tag", "previous");
        }

        @Override
        public void pause() throws RemoteException {
            Log.i("tag", "pause");
            mediaPlayer.pause();
        }

        @Override
        public void restart() throws RemoteException {
            Log.i("tag", "restart");
            mediaPlayer.start();
        }
    };

    private void initSong(SongInfoDetail songInfoDetail) {
        List<Song> songs = songInfoDetail.getSongs();
        Song song = getMostBitrateSong(songs);
        playSong(song);
        myapp.setCurSongid(songInfoDetail.getSong_id());
        Message msg = Message.obtain();
        msg.what = 100;//播放歌曲，通知主线程更新ui
        msg.obj = songInfoDetail;
        MainActivity.handler.sendMessage(msg);
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
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        Log.i("tag", "onCreate");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        myapp = (Myapp) getApplicationContext();
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("tag", "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("tag", "onDestroy");
        mediaPlayer.release();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("tag", "onUnbind");
        return super.onUnbind(intent);

    }
}
