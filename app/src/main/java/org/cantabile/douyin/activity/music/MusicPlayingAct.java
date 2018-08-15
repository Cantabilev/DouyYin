package org.cantabile.douyin.activity.music;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseActivity;
import org.cantabile.douyin.activity.fragment.musicplaying.MusicPlayingAlbumFra;
import org.cantabile.douyin.activity.fragment.musicplaying.MusicPlayingLrcFra;
import org.cantabile.douyin.comm.PlayModeEnum;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.interfaces.OnPlayerEventListener;
import org.cantabile.douyin.service.PlayService;
import org.cantabile.douyin.util.CoverLoader;
import org.cantabile.douyin.util.Preferences;
import org.cantabile.douyin.util.SystemUtil;
import org.cantabile.douyin.util.ToastUtil;

import static org.cantabile.douyin.application.AppCache.getPlayService;

public class MusicPlayingAct extends BaseActivity implements OnPlayerEventListener {

    private static String MUSIC_TOKEN = "MUSIC_TOKEN";
    private MusicInfoBean mMusic;
    private PlayService mPlayService;

    private ImageView iconBg;
    private ImageView iconBack;
    private TextView tvTitle, tvArtists;
    private TextView tvCurrTime, tvTotalTime;
    private SeekBar seekBarMusic;
    private ImageView iconMode, iconPrev, iconPlay, iconNext, iconPlayList;
    private int mLastProgress;
    private boolean isDraggingProgress;
    private AsyncTask<MusicInfoBean, Void, Bitmap> asyncTask;

    private FrameLayout frameContent;
    private MusicPlayingAlbumFra mMusicPlayingAlbumFra;
    private MusicPlayingLrcFra mMusicPlayingLrcFra;
    private boolean isAlbum = true;// 是否是 专辑页面 flag  [专辑页面 / 歌词页面]

    @Override
    public void initVariables() {
        mPlayService = getPlayService();
        mPlayService.setOnPlayerEventListener(this);
        mMusicPlayingAlbumFra = new MusicPlayingAlbumFra();
        mMusicPlayingLrcFra = new MusicPlayingLrcFra();
    }

    @Override
    public void initView() {
        setContentView(R.layout.act_music_playing);

        iconBg = (ImageView) findViewById(R.id.iconBg);
        iconBack = (ImageView) findViewById(R.id.iconBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtists = (TextView) findViewById(R.id.tvArtists);
        tvCurrTime = (TextView) findViewById(R.id.tvCurrTime);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        seekBarMusic = (SeekBar) findViewById(R.id.seekBarMusic);
        iconMode = (ImageView) findViewById(R.id.iconMode);
        iconPrev = (ImageView) findViewById(R.id.iconPrev);
        iconPlay = (ImageView) findViewById(R.id.iconPlay);
        iconNext = (ImageView) findViewById(R.id.iconNext);
        iconPlayList = (ImageView) findViewById(R.id.iconPlayList);

        frameContent = (FrameLayout) findViewById(R.id.frameContent);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameContent, mMusicPlayingAlbumFra).commit();
    }

    @Override
    public void initEvent() {
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iconMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlayMode();
            }
        });

        frameContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (isAlbum){
                    isAlbum = false;
                    ft.replace(R.id.frameContent, mMusicPlayingLrcFra).commit();
                }else {
                    isAlbum = true;
                    ft.replace(R.id.frameContent, mMusicPlayingAlbumFra).commit();
                }
            }
        });

        iconPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        iconPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        iconNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        // TODO 播放列表 bottom sheet
        iconPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (seekBar == seekBarMusic) {
                    if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                        tvCurrTime.setText(formatTime(progress));
                        mLastProgress = progress;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (seekBar == seekBarMusic) {
                    isDraggingProgress = true;
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar == seekBarMusic) {
                    isDraggingProgress = false;
                    if (mPlayService.isPlaying() || mPlayService.isPausing()) {
                        int progress = seekBar.getProgress();
                        mPlayService.seekTo(progress);
                        // TODO fragment 通信 歌词 问题
//                        if (mLrcViewSingle.hasLrc()) {
//                            mLrcViewSingle.updateTime(progress);
//                            mLrcViewFull.updateTime(progress);
//                        }
                    } else {
                        seekBar.setProgress(0);
                    }
                }
            }
        });
    }

    private void initPlayMode() {
        int mode = Preferences.getPlayMode();
        iconMode.setImageLevel(mode);
    }

    private void onChangeImpl(MusicInfoBean music) {
        if (music == null) {
            return;
        }

        tvTitle.setText(music.getTitle());
        tvArtists.setText(music.getArtist());
        seekBarMusic.setProgress((int) mPlayService.getCurrentPosition());
        seekBarMusic.setMax((int) music.getDuration());
        seekBarMusic.setSecondaryProgress((int) music.getDuration());
        mLastProgress = 0;
        tvCurrTime.setText(R.string.music_detail_start_time);
        tvTotalTime.setText(formatTime(music.getDuration()));
        setCoverAndBg(music);
//        setLrc(music); // TODO fragment 通信 歌词
        if (mPlayService.isPlaying() || mPlayService.isPreparing()) {
            iconPlay.setSelected(true);
//            mAlbumCoverView.start();// TODO fragment 通信 专辑图片旋转 开始
        } else {
            iconPlay.setSelected(false);
//            mAlbumCoverView.pause();// TODO fragment 通信 专辑图片旋转 停止
        }
    }

    private void setCoverAndBg(MusicInfoBean music) {
//        mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(music)); // TODO fragment 通信 切换图片
        if(asyncTask !=null && asyncTask.getStatus() == AsyncTask.Status.RUNNING){
            asyncTask.cancel(true);
        }
        newInstanceAsyncTask(music);

    }
    private void newInstanceAsyncTask(MusicInfoBean music){
        asyncTask = new AsyncTask<MusicInfoBean, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(MusicInfoBean... musics) {
                return CoverLoader.getInstance().loadBlur(musics[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (!isCancelled())
                    changBlurBg(iconBg, bitmap);
            }
        };
        asyncTask.execute(music);
    }

    private void changBlurBg(final ImageView icon, Bitmap bitmap){
        icon.setImageBitmap(bitmap);
        ObjectAnimator animatorOpen = ObjectAnimator.ofFloat(icon, "alpha", 0.5f, 1f);
        animatorOpen.setDuration(500L);
        animatorOpen.start();
    }

    private void play() {
        mPlayService.playPause();
    }

    private void next() {
        mPlayService.next();
    }

    private void prev() {
        mPlayService.prev();
    }

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                ToastUtil.show(R.string.mode_shuffle);
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                ToastUtil.show(R.string.mode_one);
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                ToastUtil.show(R.string.mode_loop);
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
    }

    private String formatTime(long time) {
        return SystemUtil.formatTime("mm:ss", time);
    }

    @Override
    public void loadData() {
        onChangeImpl(mPlayService.getPlayingMusic());
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void resume() {
        mPlayService = getPlayService();
        mPlayService.setOnPlayerEventListener(this);
    }

    @Override
    public void destroy() {

    }



    // 音乐播放服务回调接口
    @Override
    public void onChange(MusicInfoBean music) {

        onChangeImpl(music);
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onChange(music);
    }

    @Override
    public void onPlayerStart() {
        iconPlay.setSelected(true);
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onPlayerStart();
//        mAlbumCoverView.start();// TODO 专辑图片 开始旋转
    }

    @Override
    public void onPlayerPause() {
        iconPlay.setSelected(false);
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onPlayerPause();
//        mAlbumCoverView.pause();// TODO 专辑图片 暂停旋转
    }

    @Override
    public void onPublish(int duration, int progress) {
        if (!isDraggingProgress) {
            seekBarMusic.setMax(duration);
            seekBarMusic.setProgress(progress);
        }
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onPublish(duration, progress);

//        if (mLrcViewSingle.hasLrc()) { // TODO fragment 通信 歌词 问题
//            mLrcViewSingle.updateTime(progress);
//            mLrcViewFull.updateTime(progress);
//        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        seekBarMusic.setSecondaryProgress(seekBarMusic.getMax() * 100 / percent);
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onBufferingUpdate(percent);
    }

    @Override
    public void onTimer(long remain) {
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onTimer(remain);
    }

    @Override
    public void onMusicListUpdate() {
        if (mMusicPlayingAlbumFra != null)
            mMusicPlayingAlbumFra.onMusicListUpdate();
    }

    public static void startMusicPlayingAct(Context context){
        Intent intent = new Intent();
        intent.setClass(context, MusicPlayingAct.class);
        context.startActivity(intent);
    }
}
