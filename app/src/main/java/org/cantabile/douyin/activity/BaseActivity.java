package org.cantabile.douyin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.cantabile.douyin.CustomApplication;
import org.cantabile.douyin.interfaces.IActBase;


public abstract class BaseActivity extends Activity implements IActBase {

        /** 整个应用的Application **/
        private CustomApplication app = null;
        /** 日志输出标志 **/
        protected final String TAG = this.getClass().getSimpleName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, TAG + "-->onCreate()");

            // 获取整个应用的Application
            app = CustomApplication.getInstance();
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

        /**
         * 获取整个应用的Application
         */
        public CustomApplication getApp() {
            return this.app;
        }
    }
