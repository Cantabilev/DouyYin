package org.cantabile.douyin.activity.fragment.music;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.comm.Indicator;
import org.cantabile.douyin.adapter.MusicTitleAdapter;
import org.cantabile.douyin.activity.fragment.musiconline.*;

import java.util.ArrayList;

/**
 * Created by simple on 2017/11/24.
 */

public class MusicOnlineFra extends BaseFragment {

    private static int TAB_MARGIN_DIP = 28;
    private ViewPager viewPager;
    private MusicTitleAdapter adapter;
    private TabLayout mTabLayout;
    private TabLayout.Tab music;
    private TabLayout.Tab video;
    private TabLayout.Tab radio;


    @Override
    public int BindLayout() {
        Log.d("QQQ","MusicOnlineFra");
        return R.layout.fra_music_online;
    }

    @Override
    public void initVariables() {
        adapter = new MusicTitleAdapter(getActivity().getSupportFragmentManager());
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.musiconline_music));
        titles.add(getString(R.string.musiconline_video));
        titles.add(getString(R.string.musiconline_radio));
        adapter.setTitles(titles);
    }

    @Override
    public void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPagerInner);
        mTabLayout = (TabLayout) view.findViewById(R.id.musicOnlineTab);

        music = mTabLayout.getTabAt(0);
        video = mTabLayout.getTabAt(1);
        radio = mTabLayout.getTabAt(2);
    }

    @Override
    public void initEvent() {
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void loadData() {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MusicOnlineMusicFra());
        fragments.add(new MusicOnlineVideoFra());
        fragments.add(new MusicOnlineRadioFra());
        adapter.setFragments(fragments);
        Indicator.setIndicator(getContext(), mTabLayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void create() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
