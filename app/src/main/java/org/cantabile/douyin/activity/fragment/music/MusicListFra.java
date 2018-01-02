package org.cantabile.douyin.activity.fragment.music;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.localmusic.LocalMusicAct;
import org.cantabile.douyin.application.AppCache;

/**
 * Created by simple on 2017/11/24.
 */

public class MusicListFra extends BaseFragment {

    private LinearLayout llLocalMusic, llRecentMusic, llDownloadManage, llMyRadio, llMyCollection;
    private TextView tvLocalMusicCount;

    @Override
    public int BindLayout() {
        Log.d("QQQ","MusicListFra");
        return R.layout.fra_music_list;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView(View view) {
        llLocalMusic = (LinearLayout) view.findViewById(R.id.llLocalMusic);
        llRecentMusic = (LinearLayout) view.findViewById(R.id.llRecentMusic);
        llDownloadManage = (LinearLayout) view.findViewById(R.id.llDownloadManage);
        llMyRadio = (LinearLayout) view.findViewById(R.id.llMyRadio);
        llMyCollection = (LinearLayout) view.findViewById(R.id.llMyCollection);

        tvLocalMusicCount = (TextView) view.findViewById(R.id.tvLocalMusicCount);
    }

    @Override
    public void initEvent() {
        llLocalMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalMusicAct.startLocalMusicAct(getActivity());
            }
        });
    }

    @Override
    public void loadData() {
        tvLocalMusicCount.setText(getString(R.string.parentheses_count, AppCache.getMusicList().size()));
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
