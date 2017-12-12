package org.cantabile.douyin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.cantabile.douyin.R;
import org.cantabile.douyin.comm.MusicInfoBean;

import java.util.ArrayList;

/**
 * Created by simple on 2017/12/13.
 */

public class MusicControlViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<MusicInfoBean> mDatas;

    public MusicControlViewPagerAdapter(Context mContext, ArrayList<MusicInfoBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_music_bottom_control, null);
        // TODO 加载Page Item  动态加载


        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
