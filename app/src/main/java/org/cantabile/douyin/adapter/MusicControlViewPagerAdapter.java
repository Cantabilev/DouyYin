package org.cantabile.douyin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cantabile.douyin.R;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.util.CoverLoader;

import java.util.ArrayList;

/**
 * 底部音乐控制 viewpager 适配器
 * Created by simple on 2017/12/13.
 */
public class MusicControlViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<MusicInfoBean> mDatas;
    private OnViewClick onViewClick;

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
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_music_bottom_control, null);
        MusicInfoBean music = mDatas.get(position);
        ImageView iconAlbums = (ImageView) view.findViewById(R.id.iconAlbums);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvArtists = (TextView) view.findViewById(R.id.tvArtists);
        // FIXME 加载封面图片 有问题
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);
        iconAlbums.setImageBitmap(cover);
        tvTitle.setText(music.getTitle());
        tvArtists.setText(music.getArtist()+" - "+music.getAlbum());
        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onViewClick != null)
                    onViewClick.onClick(view, position);
            }
        });

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public void setOnViewClick(OnViewClick onViewClick) {
        this.onViewClick = onViewClick;
    }

    public interface OnViewClick{
        void onClick(View view, int position);
    }
}
