package org.cantabile.douyin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.cantabile.douyin.activity.BaseFragment;

import java.util.ArrayList;

/**
 * Created by simple on 2017/11/24.
 */

public class MusicTitleAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments;
    private ArrayList<String> titles;

    public  MusicTitleAdapter(FragmentManager fm){
        super(fm);
    }

    public  MusicTitleAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments){
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }
    @Override
    public int getCount(){
        return fragments==null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? super.getPageTitle(position) : titles.get(position);
    }

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }
}
