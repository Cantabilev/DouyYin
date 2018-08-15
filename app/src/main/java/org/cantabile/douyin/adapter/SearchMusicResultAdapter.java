package org.cantabile.douyin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.cantabile.douyin.R;
import org.cantabile.douyin.adapter.wrapper.CommRecyclerAdapter;
import org.cantabile.douyin.adapter.wrapper.ViewHolder;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.entity.SearchMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by You
 * @email 1269859055@qq.com
 * @data 2018/8/14
 **/
public class SearchMusicResultAdapter extends CommRecyclerAdapter<SearchMusic.Song> {

    private Context mContext;
    private List<SearchMusic.Song> list;

    public SearchMusicResultAdapter(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    public SearchMusicResultAdapter(Context mContext, List<SearchMusic.Song> list) {
        super(mContext, list);
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_music_single;
    }

    @Override
    public void convertView(ViewHolder holder, SearchMusic.Song song, int position) {
        holder.setText(R.id.tvSong, song.getSongName());
        holder.setText(R.id.tvSinger, song.getArtistName());
    }
}
