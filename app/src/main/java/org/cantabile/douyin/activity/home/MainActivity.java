package org.cantabile.douyin.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cantabile.compoundcircle.CompoundCircleProgress;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.fragment.MusicPlayingFra;
import org.cantabile.douyin.activity.fragment.music.MusicCommunityFra;
import org.cantabile.douyin.activity.fragment.music.MusicListFra;
import org.cantabile.douyin.activity.fragment.music.MusicOnlineFra;
import org.cantabile.douyin.adapter.MusicControlViewPagerAdapter;
import org.cantabile.douyin.adapter.MusicTitleAdapter;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.interfaces.OnPlayerEventListener;
import org.cantabile.douyin.service.PlayService;
import org.cantabile.douyin.util.PermissionReq;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements OnPlayerEventListener, NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private TabLayout.Tab musicList;
    private TabLayout.Tab musicOnline;
    private TabLayout.Tab musicCommunity;
    private MusicTitleAdapter adapter;
    private int[] titleIcons = {
            R.drawable.selector_title_my,
            R.drawable.selector_title_online,
            R.drawable.selector_title_community
    };

    private PlayService mPlayService;
    private ViewPager musicControlViewpager;
    private MusicControlViewPagerAdapter musicControlAdapter;
    private CompoundCircleProgress controlUI;
    private MusicPlayingFra mMusicPlayingFra;
    private boolean isPlayMusicDetailFraShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

//        setSystemBarTransparent();// TODO  沉浸式 状态栏问题
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initValues();
        initView();
        initEvent();
        loadData();
    }

    private void initValues() {
        adapter = new MusicTitleAdapter(getSupportFragmentManager());

        ArrayList<MusicInfoBean> list = AppCache.getMusicList();
        musicControlAdapter = new MusicControlViewPagerAdapter(this, list);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.musicTitleTab);
        musicList = mTabLayout.getTabAt(0);
        musicOnline = mTabLayout.getTabAt(1);
        musicCommunity = mTabLayout.getTabAt(2);

        musicControlViewpager = (ViewPager) findViewById(R.id.musicControlViewpager);
        controlUI = (CompoundCircleProgress) findViewById(R.id.controlUI);
        musicControlViewpager.setAdapter(musicControlAdapter);
    }

    private void initEvent() {
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        controlUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayService != null){
                    mPlayService.playPause();
                }
            }
        });
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

        musicControlAdapter.setOnViewClick(new MusicControlViewPagerAdapter.OnViewClick() {
            @Override
            public void onClick(View view, int position) {
                showPlayingFragment();
            }
        });
    }

    private void loadData() {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MusicListFra());
        fragments.add(new MusicOnlineFra());
        fragments.add(new MusicCommunityFra());
        adapter.setFragments(fragments);

    }

    private void setupTabIcons() {
        musicList.setCustomView(getTabView(2));
        musicOnline.setCustomView(getTabView(1));
        musicCommunity.setCustomView(getTabView(0));
    }


    public View getTabView(int position) {
        ImageView img_title = new ImageView(this);
        img_title.setImageResource(titleIcons[position]);
        return img_title;
    }

    private void showPlayingFragment() {
        if (isPlayMusicDetailFraShow) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mMusicPlayingFra == null) {
            mMusicPlayingFra = new MusicPlayingFra();
            ft.replace(android.R.id.content, mMusicPlayingFra);
        } else {
            ft.show(mMusicPlayingFra);
        }
        ft.commitAllowingStateLoss();
        isPlayMusicDetailFraShow = true;
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mMusicPlayingFra);
        ft.commitAllowingStateLoss();
        isPlayMusicDetailFraShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mMusicPlayingFra != null && isPlayMusicDetailFraShow) {
            hidePlayingFragment();
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onChange(MusicInfoBean music) {
        controlUI.setMaxValue(music.getDuration());
        musicControlViewpager.setCurrentItem(mPlayService.getPlayingPosition());

        if (mMusicPlayingFra != null && mMusicPlayingFra.isAdded()) {
            mMusicPlayingFra.onChange(music);
        }
    }

    @Override
    public void onPlayerStart() {
        controlUI.setChecked(true);
        if (mMusicPlayingFra != null && mMusicPlayingFra.isAdded()) {
            mMusicPlayingFra.onPlayerStart();
        }
    }

    @Override
    public void onPlayerPause() {
        controlUI.setChecked(false);
        if (mMusicPlayingFra != null && mMusicPlayingFra.isAdded()) {
            mMusicPlayingFra.onPlayerPause();
        }
    }

    @Override
    public void onPublish(int duration, int progress) {
        controlUI.setChecked(true);
        controlUI.setMaxValue(duration);
        controlUI.setValue(progress);

        if (mMusicPlayingFra != null && mMusicPlayingFra.isAdded()) {
            mMusicPlayingFra.onPublish(duration, progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mMusicPlayingFra != null && mMusicPlayingFra.isAdded()) {
            mMusicPlayingFra.onBufferingUpdate(percent);
        }
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
}
