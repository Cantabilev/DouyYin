package org.cantabile.douyin.interfaces;

import org.cantabile.douyin.entity.MusicInfoBean;

/**
 * 播放进度以及状态监听器
 * Created by simple on 2017/12/19.
 */
public interface OnPlayerEventListener {

    /**
     * 切换歌曲
     */
    void onChange(MusicInfoBean music);

    /**
     * 继续播放
     */
    void onPlayerStart();

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 更新进度
     */
    void onPublish(int duration, int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 更新定时停止播放时间
     */
    void onTimer(long remain);

    /**
     * 扫描音乐后 音乐列表更新
     */
    void onMusicListUpdate();
}
