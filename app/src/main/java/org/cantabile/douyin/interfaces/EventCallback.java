package org.cantabile.douyin.interfaces;

/**
 * 事件处理回调接口 service 使用
 * Created by simple on 2017/12/19.
 */
public interface EventCallback<T> {
    void onEvent(T t);
}
