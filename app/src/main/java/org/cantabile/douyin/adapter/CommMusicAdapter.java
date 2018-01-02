package org.cantabile.douyin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cantabile.douyin.R;
import org.cantabile.douyin.entity.MusicInfoBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simple on 2017/12/18.
 */

public class CommMusicAdapter extends RecyclerView.Adapter<CommMusicAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MusicInfoBean> list;
    private ViewHolder.OnMusicItemClickListener onMusicItemClickListener;

    public CommMusicAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public CommMusicAdapter(Context mContext, ArrayList<MusicInfoBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setOnMusicItemClickListener(ViewHolder.OnMusicItemClickListener onMusicItemClickListener) {
        this.onMusicItemClickListener = onMusicItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_music_single, parent, false), onMusicItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MusicInfoBean music = list.get(position);
        holder.tvSong.setText(music.getTitle());
        holder.tvSinger.setText(music.getArtist()+" - "+music.getAlbum());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void insert(MusicInfoBean music, int position) {
        if (list == null)
            list = new ArrayList<>();
        list.add(position, music);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (list == null)
            return;
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(MusicInfoBean node) {
        if (list == null)
            return;
        notifyItemRemoved(list.indexOf(node));
        list.remove(node);
    }

    public void setList(ArrayList<MusicInfoBean> list) {
        this.list = list;
    }

    static

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSong)
        TextView tvSong;
        @BindView(R.id.iconSongMV)
        ImageView iconSongMV;
        @BindView(R.id.iconDownloadAlready)
        ImageView iconDownloadAlready;
        @BindView(R.id.iconSQ)
        ImageView iconSQ;
        @BindView(R.id.tvSinger)
        TextView tvSinger;
        @BindView(R.id.iconOption)
        ImageView iconOption;

        public ViewHolder(View itemView, final OnMusicItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(view, getLayoutPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onItemLongClick(view, getLayoutPosition());
                        return true;
                    }
                    return false;
                }
            });
        }

        public interface OnMusicItemClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }
    }
}
