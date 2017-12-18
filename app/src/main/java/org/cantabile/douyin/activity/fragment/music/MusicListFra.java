package org.cantabile.douyin.activity.fragment.music;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.activity.localmusic.LocalMusicAct;

/**
 * Created by simple on 2017/11/24.
 */

public class MusicListFra extends BaseFragment {

    private LinearLayout llLocalMusic, llRecentMusic, llDownloadManage, llMyRadio, llMyCollection;

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
