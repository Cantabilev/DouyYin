package org.cantabile.douyin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.cantabile.douyin.activity.launch.LaunchActivity;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.application.CustomApplication;
import org.cantabile.douyin.interfaces.IActBase;
import org.cantabile.douyin.service.PlayService;
import org.cantabile.douyin.util.PermissionReq;


public abstract class BaseActivity extends AppCompatActivity implements IActBase {

    protected Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 整个应用的Application
     **/
    private CustomApplication app = null;
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + "-->onCreate()");

        // 获取整个应用的Application
        app = CustomApplication.getInstance();
//        setSystemBarTransparent();// TODO 解决 Android 4.4 沉浸式状态栏为问题， 后期为ToolBar添加相应高度
        initVariables();
        initView();
        initEvent();
        loadData();
        doBusiness();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG + "-->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, TAG + "-->onResume()");
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG + "-->onPause()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, TAG + "-->onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, TAG + "-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG + "-->onDestroy()");
        destroy();
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }

    protected boolean checkServiceAlive() {
        if (AppCache.getPlayService() == null) {
            startActivity(new Intent(this, LaunchActivity.class));
            AppCache.clearStack();
            return false;
        }
        return true;
    }

    public PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 获取整个应用的Application
     */
    public CustomApplication getApp() {
        return this.app;
    }
}

