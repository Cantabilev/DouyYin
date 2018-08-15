package org.cantabile.douyin.activity.music;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseActivity;
import org.cantabile.douyin.adapter.CommMusicAdapter;
import org.cantabile.douyin.adapter.SearchMusicResultAdapter;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.constant.LoadStateEnum;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.entity.SearchMusic;
import org.cantabile.douyin.net.http.HttpCallback;
import org.cantabile.douyin.net.http.HttpClient;
import org.cantabile.douyin.util.ViewUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NetSearchAct extends BaseActivity {

    private ImageView iconBack;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private SearchView mSearchView;
    private RecyclerView recyclerSearch;
    private LinearLayout llLoading;
    private LinearLayout llLoadFail;

    private ProgressDialog mProgressDialog;

    private List<SearchMusic.Song> searchMusics;
    private SearchMusicResultAdapter searchAdapter;

    @Override
    public void initVariables() {
        searchMusics = new ArrayList<>();
        searchAdapter = new SearchMusicResultAdapter(this, searchMusics);
    }

    @Override
    public void initView() {
        setContentView(R.layout.act_net_search);

        iconBack = (ImageView) findViewById(R.id.iconBack);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        recyclerSearch = (RecyclerView) findViewById(R.id.recyclerSearch);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        llLoadFail = (LinearLayout) findViewById(R.id.llLoadFail);

        mSearchView.setQueryHint("输入歌曲名查找");
        mSearchView.onActionViewExpanded();// 当展开无输入内容的时候，没有关闭的图标
        mSearchView.setIconified(false);//设置searchView处于展开状态

        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        //设置输入框内容文字和提示文字的颜色
        mSearchAutoComplete.setHintTextColor(getResources().getColor(R.color.search_view_hint_color));
        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
        mSearchAutoComplete.setTextSize(16);

        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void initEvent() {
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                Toast.makeText(NetSearchAct.this, query, Toast.LENGTH_SHORT).show();
                querySubmitMusic(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调
                queryLikeMusic(newText);
                return true;
            }
        });

        recyclerSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerSearch.setAdapter(searchAdapter);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mSearchAutoComplete.isShown()) {
//                    try {
//                        mSearchAutoComplete.setText("");//清除文本
//                        //利用反射调用收起SearchView的onCloseClicked()方法
//                        Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
//                        method.setAccessible(true);
//                        method.invoke(mSearchView);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    finish();
//                }
//            }
//        });
    }

    private void querySubmitMusic(String content){
        ViewUtils.changeViewState(recyclerSearch, llLoading, llLoadFail, LoadStateEnum.LOADING);
        searchMusic(content);
    }
    private void queryLikeMusic(String content){

    }

    private void searchMusic(String keyword){
        HttpClient.searchMusic(keyword, new HttpCallback<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if (response == null || response.getSong() == null) {
                    ViewUtils.changeViewState(recyclerSearch, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
                    return;
                }
                ViewUtils.changeViewState(recyclerSearch, llLoading, llLoadFail, LoadStateEnum.LOAD_SUCCESS);
                searchMusics.clear();
                searchMusics.addAll(response.getSong());
                searchAdapter.notifyDataSetChanged();
                recyclerSearch.requestFocus();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerSearch.scrollToPosition(0);
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                ViewUtils.changeViewState(recyclerSearch, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
            }

            @Override
            public void onFinish() {

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
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    public static void startNetSearchAct(Context context){
        Intent intent = new Intent(context, NetSearchAct.class);
        context.startActivity(intent);
    }
}
