package org.cantabile.douyin.net.http;

/**
 * @author Created by You
 * @email 1269859055@qq.com
 * @data 2018/8/15
 **/
public interface HttpCallback<T> {
    public  void onSuccess(T t);

    public  void onFail(Exception e);

    public void onFinish();
}
