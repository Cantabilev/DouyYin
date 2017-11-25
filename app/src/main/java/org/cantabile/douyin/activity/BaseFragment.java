package org.cantabile.douyin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cantabile.douyin.CustomApplication;
import org.cantabile.douyin.interfaces.IFraBase;

/**
 * Created by Kbin on 2017/1/17.
 */
public abstract class BaseFragment extends Fragment implements IFraBase {
    /** 整个应用的Application **/
    private CustomApplication app = null;
    /** 当前Fragment渲染的视图View **/
    private View view = null;
    /** 当前Fragment渲染的layoutId **/
    private int LayoutId=0;

    private boolean isRePaintView=false;
    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, TAG + "-->onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = CustomApplication.getInstance();
        create();
        Log.d(TAG, TAG + "-->onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, TAG + "-->onCreateView() + view -> "+view);
        // 渲染视图View(防止切换时重绘View)
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            Log.d(TAG, TAG+"-->parent --> "+parent);
            if (parent != null) {
                parent.removeView(view);
                //将视图从父视图中删除。
            }
        } else {
            initVariables();
            view = inflater.inflate(BindLayout(), container, false);
            initView(view);
            initEvent();
            loadData();
            doBusiness();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, TAG + "-->onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, TAG + "-->onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, TAG + "-->onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, TAG + "-->onResume()");
        resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, TAG + "-->onPause()");
        pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, TAG + "-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, TAG + "-->onDestroy()");
        destroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, TAG + "-->onDetach()");
        super.onDetach();
    }

    public void setRePaintView(boolean rePaintView) {
        isRePaintView = rePaintView;
    }

    /**
     * 获取整个应用的Application
     */
    public CustomApplication getApp() {
        return this.app;
    }
}
