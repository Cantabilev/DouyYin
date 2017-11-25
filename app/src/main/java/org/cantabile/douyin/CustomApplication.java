package org.cantabile.douyin;

import android.app.Application;
import android.content.Context;

/**
 * Created by simple on 2017/11/24.
 */

public class CustomApplication extends Application {

    private static CustomApplication instance=null;
    public static CustomApplication getInstance() {
        return instance;
    }
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = CustomApplication.this;
    }

    public Context getContext(){
        return context;
    }

}
