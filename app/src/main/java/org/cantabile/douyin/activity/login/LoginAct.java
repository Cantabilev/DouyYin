package org.cantabile.douyin.activity.login;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import org.cantabile.douyin.R;

// TODO 登录activity 暂未实装 仅有背景视频，TextInputLayout
public class LoginAct extends AppCompatActivity {

    private VideoView videoview;
    private TextInputLayout account;
    private TextInputLayout password;

    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initVariables();
        initView();
        initEvent();
        loadData();
        doBusiness();
    }

    private void initVariables() {
        videoPath = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro).toString();
    }

    private void initView() {
        videoview = (VideoView) findViewById(R.id.videoview);
        account = (TextInputLayout) findViewById(R.id.account);
        password = (TextInputLayout) findViewById(R.id.password);
    }

    private void initEvent() {
        videoview.setVideoPath(videoPath);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    private void loadData() {

    }

    private void doBusiness() {

    }
}
