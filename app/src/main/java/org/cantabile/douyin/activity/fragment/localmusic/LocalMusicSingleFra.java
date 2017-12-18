package org.cantabile.douyin.activity.fragment.localmusic;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.adapter.CommMusicAdapter;
import org.cantabile.douyin.adapter.wrapper.HeaderAndFooterWrapper;
import org.cantabile.douyin.comm.MusicInfoBean;
import org.cantabile.douyin.util.MusicCommUtil;

import java.util.ArrayList;

/**
 * Created by simple on 2017/12/14.
 */

public class LocalMusicSingleFra extends BaseFragment {

    private View header, footer;
    private RecyclerView musicRecyclerView;
    private CommMusicAdapter adapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    public int BindLayout() {
        return R.layout.fra_localmusic_single;
    }

    @Override
    public void initVariables() {
        adapter = new CommMusicAdapter(getActivity());
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
    }

    @Override
    public void initView(View view) {
        musicRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMusic);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        header = LayoutInflater.from(getContext()).inflate(R.layout.item_header_play_all, musicRecyclerView, false);
        footer = LayoutInflater.from(getContext()).inflate(R.layout.view_footer_placeholder, musicRecyclerView, false);
    }

    @Override
    public void initEvent() {
        musicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHeaderAndFooterWrapper.addHeaderView(header);
        mHeaderAndFooterWrapper.addFootView(footer);
        musicRecyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    @Override
    public void loadData() {
        ArrayList<MusicInfoBean> musics = MusicCommUtil.getMusics(getActivity().getContentResolver());
        adapter.setList(musics);
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
