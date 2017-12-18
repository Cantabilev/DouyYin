package org.cantabile.douyin.activity.localmusic;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseActivity;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicAlbumsFra;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicFolderFra;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicSingerFra;
import org.cantabile.douyin.activity.fragment.localmusic.LocalMusicSingleFra;
import org.cantabile.douyin.adapter.MusicTitleAdapter;

import java.util.ArrayList;

public class LocalMusicAct extends BaseActivity {

    private ViewPager localMusicViewPager;
    private MusicTitleAdapter adapter;
    private TabLayout mTabLayout;

    @Override
    public void initVariables() {
        adapter = new MusicTitleAdapter(getSupportFragmentManager());
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.localmusic_tab_single));
        titles.add(getString(R.string.localmusic_tab_singer));
        titles.add(getString(R.string.localmusic_tab_albums));
        titles.add(getString(R.string.localmusic_tab_folder));
        adapter.setTitles(titles);
    }

    @Override
    public void initView() {
        setContentView(R.layout.act_local_music);
        localMusicViewPager = (ViewPager) findViewById(R.id.localMusicViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.localMusicTab);
    }

    @Override
    public void initEvent() {
        localMusicViewPager.setOffscreenPageLimit(0);
        localMusicViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(localMusicViewPager);
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

    public static void startLocalMusicAct(Activity activity){
        Intent intent = new Intent(activity, LocalMusicAct.class);
        activity.startActivity(intent);
    }
}
