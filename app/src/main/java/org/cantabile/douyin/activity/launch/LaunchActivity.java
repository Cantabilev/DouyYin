package org.cantabile.douyin.activity.launch;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.home.MainActivity;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.interfaces.EventCallback;
import org.cantabile.douyin.service.PlayService;
import org.cantabile.douyin.util.PermissionReq;

public class LaunchActivity extends Activity {

    private ServiceConnection mPlayServiceConnection;
    protected Handler handler = new Handler(Looper.getMainLooper());

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
////            intent.putExtras(getIntent());// TODO 通知栏时使用
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//            finish();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launch);

        checkService();
    }

    private void checkService() {
        if (AppCache.getPlayService() == null) {
            Intent intent = new Intent(this, PlayService.class);
            startService(intent);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            }, 1000);
        } else {
            startMusicActivity();
            finish();
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void startMusicActivity() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtras(getIntent());// TODO 通知栏 使用
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            AppCache.setPlayService(playService);
            PermissionReq.with(LaunchActivity.this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .result(new PermissionReq.Result() {
                        @Override
                        public void onGranted() {
                            scanMusic(playService);
                        }

                        @Override
                        public void onDenied() {
                            Toast.makeText(LaunchActivity.this, getResources().getString(R.string.no_permission_storage), Toast.LENGTH_SHORT).show();
                            finish();
                            playService.quit();
                        }
                    })
                    .request();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    private void scanMusic(final PlayService playService) {
        playService.updateMusicList(new EventCallback<Void>() {
            @Override
            public void onEvent(Void aVoid) {
                startMusicActivity();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
