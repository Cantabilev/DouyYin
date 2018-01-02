package org.cantabile.douyin.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.service.PlayService;
import org.cantabile.douyin.util.CoverLoader;
import org.cantabile.douyin.util.Preferences;
import org.cantabile.douyin.util.ScreenUtil;
import org.cantabile.douyin.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simple on 2017/12/19.
 */

public class AppCache {
    private Context mContext;
    private PlayService mPlayService;
    // 本地歌曲列表
    private final ArrayList<MusicInfoBean> mMusicList = new ArrayList<>();
    private final List<Activity> mActivityStack = new ArrayList<>();

    private AppCache() {
    }

    private static class SingletonHolder {
        private static AppCache mAppCache = new AppCache();
    }

    private static AppCache getInstance() {
        return SingletonHolder.mAppCache;
    }

    public static void init(Application application) {
        getInstance().onInit(application);
    }

    private void onInit(Application application) {
        mContext = application.getApplicationContext();
        ToastUtil.init(mContext);
        Preferences.init(mContext);
        ScreenUtil.init(mContext);
//        CrashHandler.getInstance().init();// Cantabile 尚未使用异常处理程序
        CoverLoader.getInstance().init(mContext);
        application.registerActivityLifecycleCallbacks(new ActivityLifecycle());
    }

    public static Context getContext() {
        return getInstance().mContext;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;
    }

    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }

    /**
     * 获取本地歌曲列表
     * @return 本地歌曲ArrayList
     */
    public static ArrayList<MusicInfoBean> getMusicList() {
        return getInstance().mMusicList;
    }

    public static void clearStack() {
        List<Activity> activityStack = getInstance().mActivityStack;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    private static class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
        private static final String TAG = ActivityLifecycle.class.getSimpleName();

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.i(TAG, "onCreate: " + activity.getClass().getSimpleName());
            getInstance().mActivityStack.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.i(TAG, "onDestroy: " + activity.getClass().getSimpleName());
            getInstance().mActivityStack.remove(activity);
        }
    }
}
