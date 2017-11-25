package org.cantabile.douyin.interfaces;

/**
 * Created by DELL on 2016/12/21.
 */

public interface IActBase {
    /**
     * 初始化变量（onCreate方法中调用）
     */
    public void initVariables();

    /**
     * 初始化视图（onCreate方法中调用）
     */
    public void initView();

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
     * 暂停Activity恢复后的相关操作（onResume方法中调用）
     */
    public void resume();

    /**
     * 销毁Activity前的相关操作（onDestroy方法中调用）
     */
    public void destroy();
}
