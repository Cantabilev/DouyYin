package org.cantabile.douyin.activity.localmusic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.cantabile.compoundcircle.CompoundCircleProgress;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseActivity;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicAlbumsFra;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicFolderFra;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicSingerFra;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicSingleFra;
import org.cantabile.douyin.adapter.MusicControlViewPagerAdapter;
import org.cantabile.douyin.adapter.MusicTitleAdapter;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.interfaces.OnPlayerEventListener;
import org.cantabile.douyin.service.PlayService;

import java.util.ArrayList;

public class LocalMusicAct extends BaseActivity implements OnPlayerEventListener {

    private ViewPager localMusicViewPager;
    private MusicTitleAdapter adapter;
    private TabLayout mTabLayout;

    private PlayService mPlayService;
    private CompoundCircleProgress controlUI;
    private ViewPager musicControlViewpager;
    private MusicControlViewPagerAdapter musicControlAdapter;

    @Override
    public void initVariables() {
        adapter = new MusicTitleAdapter(getSupportFragmentManager());
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.localmusic_tab_single));
        titles.add(getString(R.string.localmusic_tab_singer));
        titles.add(getString(R.string.localmusic_tab_albums));
        titles.add(getString(R.string.localmusic_tab_folder));
        adapter.setTitles(titles);

        ArrayList<MusicInfoBean> list = AppCache.getMusicList();
        musicControlAdapter = new MusicControlViewPagerAdapter(this, list);
    }

    @Override
    public void initView() {
        setContentView(R.layout.act_local_music);
        localMusicViewPager = (ViewPager) findViewById(R.id.localMusicViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.localMusicTab);

        controlUI = (CompoundCircleProgress) findViewById(R.id.controlUI);
        musicControlViewpager = (ViewPager) findViewById(R.id.musicControlViewpager);
    }

    @Override
    public void initEvent() {
        localMusicViewPager.setOffscreenPageLimit(0);
        localMusicViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(localMusicViewPager);

        controlUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayService != null){
                    mPlayService.playPause();
                }
            }
        });
        musicControlViewpager.setAdapter(musicControlAdapter);
        musicControlViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mPlayService.getPlayingPosition() != position)
                    mPlayService.play(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData() {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new LocalMusicSingleFra());
        fragments.add(new LocalMusicSingerFra());
        fragments.add(new LocalMusicAlbumsFra());
        fragments.add(new LocalMusicFolderFra());
        adapter.setFragments(fragments);
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    public static void startLocalMusicAct(Activity activity) {
        Intent intent = new Intent(activity, LocalMusicAct.class);
        activity.startActivity(intent);
    }

    @Override
    public void onChange(MusicInfoBean music) {
        controlUI.setMaxValue(music.getDuration());
        musicControlViewpager.setCurrentItem(mPlayService.getPlayingPosition());
    }

    @Override
    public void onPlayerStart() {
        controlUI.setChecked(true);
    }

    @Override
    public void onPlayerPause() {
        controlUI.setChecked(false);
    }

    @Override
    public void onPublish(int duration, int progress) {
        controlUI.setChecked(true);
        controlUI.setMaxValue(duration);
        controlUI.setValue(progress);
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onTimer(long remain) {

    }

    @Override
    public void onMusicListUpdate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayService = AppCache.getPlayService();
        mPlayService.setOnPlayerEventListener(this);
        musicControlViewpager.setCurrentItem(mPlayService.getPlayingPosition());
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
