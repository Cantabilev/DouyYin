package org.cantabile.douyin.interfaces;

import android.view.View;

/**
 * Created by Kbin on 2017/1/17.
 */

public interface IFraBase {
    /**
     * 绑定布局（onCreate方法中调用）
     */
    public int BindLayout();
    /**
     * 初始化变量（onCreate方法中调用）
     */
    public void initVariables();
    /**
     * 初始化视图（onCreate方法中调用）
     */
    public void initView(View view);

    /**
     * 初始化事件（onCreate方法中调用）
     */
    public void initEvent();

    /**
     * 加载数据（onCreate方法中调用）
     */
    public void loadData();

    /**
     * 业务处理（onCreate方法中调用）
     */
    public void doBusiness();

    /**
     * onCreate 方法
     */
    public void create();

    /**
     * 暂停Activity恢复后的相关操作（onResume方法中调用）
     */
    public void resume();

    /**
     * 暂停Activity的相关操作（onPause方法中调用）
     */
    public void pause();

    /**
     * 销毁Activity前的相关操作（onDestroy方法中调用）
     */
    public void destroy();
}
