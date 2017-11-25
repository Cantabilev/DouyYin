package org.cantabile.douyin.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.home.MainActivity;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launch);
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
