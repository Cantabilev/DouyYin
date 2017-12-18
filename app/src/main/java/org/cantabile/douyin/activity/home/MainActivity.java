package org.cantabile.douyin.activity.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.fragment.music.MusicCommunityFra;
import org.cantabile.douyin.activity.fragment.music.MusicListFra;
import org.cantabile.douyin.activity.fragment.music.MusicOnlineFra;
import org.cantabile.douyin.adapter.MusicControlViewPagerAdapter;
import org.cantabile.douyin.adapter.MusicTitleAdapter;
import org.cantabile.douyin.comm.MusicInfoBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    private ViewPager musicControlViewpager;
    private MusicControlViewPagerAdapter musicControlAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
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


        ArrayList<MusicInfoBean> list = new ArrayList<>();
        list.add(new MusicInfoBean());
        list.add(new MusicInfoBean());
        list.add(new MusicInfoBean());
        list.add(new MusicInfoBean());
        list.add(new MusicInfoBean());
        list.add(new MusicInfoBean());
        musicControlAdapter = new MusicControlViewPagerAdapter(this, list);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.musicTitleTab);
        musicList = mTabLayout.getTabAt(0);
        musicOnline = mTabLayout.getTabAt(1);
        musicCommunity = mTabLayout.getTabAt(2);

        musicControlViewpager = (ViewPager) findViewById(R.id.musicControlViewpager);
        musicControlViewpager.setAdapter(musicControlAdapter);
    }

    private void initEvent() {
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
}
