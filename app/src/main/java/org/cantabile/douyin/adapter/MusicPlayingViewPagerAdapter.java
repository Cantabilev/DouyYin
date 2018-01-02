package org.cantabile.douyin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import org.cantabile.douyin.activity.fragment.musicplaying.RoundAlbumFra;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.service.PlayService;

/**
 * Created by simple on 2017/12/27.
 */

public class MusicPlayingViewPagerAdapter extends FragmentStatePagerAdapter {

    private RoundAlbumFra mCurrentFragment;

    public MusicPlayingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // return RoundFragment.newInstance(MusicPlayer.getQueue()[position - 1]);
        return RoundAlbumFra.newInstance(AppCache.getMusicList().get(position));
    }

    @Override
    public int getCount() {
        return AppCache.getMusicList().size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (RoundAlbumFra) object;
        super.setPrimaryItem(container, position, object);
    }


    public RoundAlbumFra getCurrentFragment() {
        return mCurrentFragment;
    }
}
