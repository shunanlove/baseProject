package com.shunan.baseproject.test;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import com.orhanobut.logger.Logger;
import com.shunan.baseproject.R;

public class SingASongService extends Service {

    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("onCreate");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.no_kill);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlaySong();
            }
        }).start();
        return START_STICKY;
    }

    //开始、暂停播放
    private void startPlaySong() {
        Logger.d("startPlaySong");
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.no_kill);
            mMediaPlayer.start();
        } else {
            mMediaPlayer.start();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        mMediaPlayer.pause();
        stopPlaySong();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), SingASongService.class));
        } else {
            startService(new Intent(getApplicationContext(), SingASongService.class));
        }
    }

    //停止播放销毁对象
    private void stopPlaySong() {
        Logger.d("stopPlaySong");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
