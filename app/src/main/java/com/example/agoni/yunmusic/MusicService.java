package com.example.agoni.yunmusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
            Log.i("tag", "next");
            am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                playNext();
            }

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
            am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer.start();
            }
        }

        @Override
        public void saveState() throws RemoteException {
            savePlayList();//保存播放列表
            savePlayInfo();//保存播放信息
            mediaPlayer.release();
        }
    };

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
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                Message msg = Message.obtain();
                msg.what = 200;//播放歌曲，通知主线程更新ui
                MainActivity.handler.sendMessage(msg);

                Log.i("tag","AUDIOFOCUS_LOSS_TRANSIENT");
            }
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume playback
            if (!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                Message msg = Message.obtain();
                msg.what = 300;//继续播放歌曲，通知主线程更新ui
                MainActivity.handler.sendMessage(msg);

                Log.i("tag","AUDIOFOCUS_LOSS_TRANSIENT");
                Log.i("tag","AUDIOFOCUS_GAIN");

            }
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            am.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) mContext);
            // Stop playback
            mediaPlayer.pause();
            Message msg = Message.obtain();
            msg.what = 200;//播放歌曲，通知主线程更新ui
            MainActivity.handler.sendMessage(msg);
            Log.i("tag","AUDIOFOCUS_LOSS");

        }
        else if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.setVolume(0.1f, 0.1f);
                Log.i("tag","AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
            }
        }
    }
}
