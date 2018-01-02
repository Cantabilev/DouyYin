package org.cantabile.douyin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.comm.PlayModeEnum;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.constant.Action;
import org.cantabile.douyin.interfaces.EventCallback;
import org.cantabile.douyin.interfaces.OnPlayerEventListener;
import org.cantabile.douyin.receiver.NoisyAudioStreamReceiver;
import org.cantabile.douyin.util.MusicCommUtil;
import org.cantabile.douyin.util.Preferences;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 音乐播放后台服务
 * Created by simple on 2017/12/19.
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    private static final String TAG = PlayService.class.getSimpleName();
    private static final long TIME_UPDATE = 300L;
    private PlayBinder mPlayBinder = new PlayBinder();

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;
    private int mPlayState = STATE_IDLE;


    private final NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private final IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private final Handler mHandler = new Handler();
    private MediaPlayer mPlayer = new MediaPlayer();
    private AudioFocusManager mAudioFocusManager;
    private MediaSessionManager mMediaSessionManager;
    private OnPlayerEventListener mListener;
    // 正在播放的歌曲[本地|网络]
    private MusicInfoBean mPlayingMusic;
    // 正在播放的本地歌曲的序号
    private int mPlayingPosition = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        mAudioFocusManager = new AudioFocusManager(this);
        mMediaSessionManager = new MediaSessionManager(this);
        mPlayer.setOnCompletionListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mPlayBinder;
    }

    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Action.ACTION_MEDIA_PLAY_PAUSE:
                    playPause();
                    break;
                case Action.ACTION_MEDIA_NEXT:
                    next();
                    break;
                case Action.ACTION_MEDIA_PREVIOUS:
                    prev();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * 扫描音乐
     */
    public void updateMusicList(final EventCallback<Void> callback) {
        new AsyncTask<Void, Void, List<MusicInfoBean>>() {
            @Override
            protected List<MusicInfoBean> doInBackground(Void... params) {
                return MusicCommUtil.scanMusic(PlayService.this);
            }

            @Override
            protected void onPostExecute(List<MusicInfoBean> musicList) {
                AppCache.getMusicList().clear();
                AppCache.getMusicList().addAll(musicList);

                if (!AppCache.getMusicList().isEmpty()) {
                    updatePlayingPosition();
                    mPlayingMusic = AppCache.getMusicList().get(mPlayingPosition);
                }

                if (mListener != null) {
                    mListener.onMusicListUpdate();
                }

                if (callback != null) {
                    callback.onEvent(null);
                }
            }
        }.execute();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    public OnPlayerEventListener getOnPlayerEventListener() {
        return mListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener mListener) {
        this.mListener = mListener;
    }

    public void play(int position) {
        if (AppCache.getMusicList().isEmpty()) {
            return;
        }

        if (position < 0) {
            position = AppCache.getMusicList().size() - 1;
        } else if (position >= AppCache.getMusicList().size()) {
            position = 0;
        }

        mPlayingPosition = position;
        MusicInfoBean music = AppCache.getMusicList().get(mPlayingPosition);
        Preferences.saveCurrentSongId(music.getMusicId());
        play(music);
    }

    public void play(MusicInfoBean music) {
        mPlayingMusic = music;
        try {
            mPlayer.reset();
            mPlayer.setDataSource(music.getPathUrl());
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            if (mListener != null) {
                mListener.onChange(music);
            }
//            Notifier.showPlay(music);// TODO 通知栏 Notifier by Cantabile
            mMediaSessionManager.updateMetaData(mPlayingMusic);
            mMediaSessionManager.updatePlaybackState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isPreparing()) {
                start();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mListener != null) {
                mListener.onBufferingUpdate(percent);
            }
        }
    };

    public void playPause() {
        if (isPreparing()) {
            stop();
        } else if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            start();
        } else {
            play(getPlayingPosition());
        }
    }

    protected void start() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        if (mAudioFocusManager.requestAudioFocus()) {
            mPlayer.start();
            mPlayState = STATE_PLAYING;
            mHandler.post(mPublishRunnable);
//            Notifier.showPlay(mPlayingMusic);// TODO 通知栏 by Cantabile
            mMediaSessionManager.updatePlaybackState();
            registerReceiver(mNoisyReceiver, mNoisyFilter);

            if (mListener != null) {
                mListener.onPlayerStart();
            }
        }
    }

    protected void pause() {
        if (!isPlaying()) {
            return;
        }
        mPlayer.pause();
        mPlayState = STATE_PAUSE;
        mHandler.removeCallbacks(mPublishRunnable);
//        Notifier.showPause(mPlayingMusic);// TODO 通知栏 by Cantabile
        mMediaSessionManager.updatePlaybackState();
        unregisterReceiver(mNoisyReceiver);

        if (mListener != null) {
            mListener.onPlayerPause();
        }
    }

    public void stop() {
        if (isIdle()) {
            return;
        }
        pause();
        mPlayer.reset();
        mPlayState = STATE_IDLE;
    }

    public void next() {
        if (AppCache.getMusicList().isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(AppCache.getMusicList().size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition + 1);
                break;
        }
    }

    public void prev() {
        if (AppCache.getMusicList().isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(AppCache.getMusicList().size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition - 1);
                break;
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mPlayer.seekTo(msec);
            mMediaSessionManager.updatePlaybackState();
            if (mListener != null) {
                mListener.onPublish(mPlayer.getDuration(), msec);
            }
        }
    }

    public boolean isPlaying() {
        return mPlayState == STATE_PLAYING;
    }

    public boolean isPausing() {
        return mPlayState == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return mPlayState == STATE_PREPARING;
    }

    public boolean isIdle() {
        return mPlayState == STATE_IDLE;
    }

    /**
     * 获取正在播放的本地歌曲的序号
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public MusicInfoBean getPlayingMusic() {
        return mPlayingMusic;
    }

    /**
     * 删除或下载歌曲后     刷新正在播放的本地歌曲的序号
     */
    public void updatePlayingPosition() {
        int position = 0;
        long id = Preferences.getCurrentSongId();
        for (int i = 0; i < AppCache.getMusicList().size(); i++) {
            if (AppCache.getMusicList().get(i).getMusicId() == id) {
                position = i;
                break;
            }
        }
        mPlayingPosition = position;
        Preferences.saveCurrentSongId(AppCache.getMusicList().get(mPlayingPosition).getMusicId());
    }

    public int getAudioSessionId() {
        return mPlayer.getAudioSessionId();
    }

    public long getCurrentPosition() {
        if (isPlaying() || isPausing()) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onPublish(mPlayer.getDuration(), mPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    @Override
    public void onDestroy() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        mAudioFocusManager.abandonAudioFocus();
        mMediaSessionManager.release();
//        Notifier.cancelAll(); // TODO 通知栏 by Cantabile
        AppCache.setPlayService(null);
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + getClass().getSimpleName());
    }

    public void quit() {
        stop();
//        QuitTimer.getInstance().stop();
        stopSelf();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
}
