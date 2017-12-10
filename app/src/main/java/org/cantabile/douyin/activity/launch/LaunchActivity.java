package org.cantabile.douyin.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.home.MainActivity;

public class LaunchActivity extends Activity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launch);
        handler.sendEmptyMessageDelayed(0, 2000);
    }
}
