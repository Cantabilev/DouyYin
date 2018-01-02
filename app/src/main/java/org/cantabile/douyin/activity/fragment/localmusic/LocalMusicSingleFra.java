package org.cantabile.douyin.activity.fragment.localmusic;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.adapter.CommMusicAdapter;
import org.cantabile.douyin.adapter.wrapper.HeaderAndFooterWrapper;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.util.MusicCommUtil;

import java.util.ArrayList;
import java.util.List;

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

        adapter.setOnMusicItemClickListener(new CommMusicAdapter.ViewHolder.OnMusicItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getPlayService().play(position - mHeaderAndFooterWrapper.getHeadersCount());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void loadData() {
        ArrayList<MusicInfoBean> musics = AppCache.getMusicList();
        if (musics != null && musics.size() > 0) {
            adapter.setList(musics);
            ((TextView)header.findViewById(R.id.tvCount)).setText(getString(R.string.parentheses_all_count, musics.size()));
        }else {
            //TODO 显示暂无本地歌曲 占位图
        }
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
