package org.cantabile.douyin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.cantabile.douyin.constant.Action;
import org.cantabile.douyin.service.PlayService;

/**
 * 来电/耳机拔出时暂停播放
 * Created by simple on 2017/12/19.
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PlayService.startCommand(context, Action.ACTION_MEDIA_PLAY_PAUSE);
    }
}
