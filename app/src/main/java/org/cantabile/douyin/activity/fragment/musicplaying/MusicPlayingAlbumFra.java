package org.cantabile.douyin.activity.fragment.musicplaying;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import org.cantabile.douyin.R;
import org.cantabile.douyin.activity.BaseFragment;
import org.cantabile.douyin.adapter.MusicPlayingViewPagerAdapter;
import org.cantabile.douyin.application.AppCache;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.interfaces.OnPlayerEventListener;
import org.cantabile.douyin.service.PlayService;

import java.lang.reflect.Field;

/**
 * Created by simple on 2017/12/25.
 */

public class MusicPlayingAlbumFra extends BaseFragment implements OnPlayerEventListener {

    private static final int ROTATION_START = 0;
    private static final int ROTATION_END = -22;
    private static final int VIEWPAGER_SCROLL_TIME = 390;

    private PlayService mPlayService;

    private ObjectAnimator mNeedleAnim, mRotateAnim;
    private AnimatorSet mAnimatorSet;
    private ImageView iconNeedle;
    private ImageView iconFavorite, iconDownload, iconComment, iconMore;
    private ViewPager viewPagerMusic;
    private MusicPlayingViewPagerAdapter adapter;

    @Override
    public int BindLayout() {
        return R.layout.fra_musicplaying_album;
    }

    @Override
    public void initVariables() {
        adapter = new MusicPlayingViewPagerAdapter(getFragmentManager());
        mPlayService = AppCache.getPlayService();
        mAnimatorSet = new AnimatorSet();
    }

    @Override
    public void initView(View view) {
        viewPagerMusic = (ViewPager) view.findViewById(R.id.viewPagerMusic);
        iconNeedle = (ImageView) view.findViewById(R.id.iconNeedle);
        iconFavorite = (ImageView) view. findViewById(R.id.iconFavorite);
        iconDownload = (ImageView) view. findViewById(R.id.iconDownload);
        iconComment = (ImageView) view. findViewById(R.id.iconComment);
        iconMore = (ImageView) view. findViewById(R.id.iconMore);

        viewPagerMusic.setAdapter(adapter);
        viewPagerMusic.setCurrentItem(mPlayService.getPlayingPosition(), false);
        PlayPagerTransformer transformer = new PlayPagerTransformer();
        viewPagerMusic.setPageTransformer(true, transformer);
        viewPagerMusic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPlayService.play(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setViewPagerScrollTime(viewPagerMusic);

        mNeedleAnim = ObjectAnimator.ofFloat(iconNeedle, "rotation", ROTATION_END, ROTATION_START);
    }

    @Override
    public void initEvent() {
        iconFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("QQQ","iconFavorite");
            }
        });
        iconDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("QQQ","iconDownload");
            }
        });
        iconComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("QQQ","iconComment");
            }
        });
        iconMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("QQQ","iconMore");
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

    @Override
    public void onChange(MusicInfoBean music) {
        viewPagerMusic.setCurrentItem(mPlayService.getPlayingPosition(), true);
    }

    @Override
    public void onPlayerStart() {
        mRotateAnim = (ObjectAnimator) adapter.getCurrentFragment().getParentView().getTag(R.id.tag_animator);
        if (mRotateAnim != null && !mRotateAnim.isRunning() && mNeedleAnim != null) {
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.play(mNeedleAnim).with(mRotateAnim);
            mAnimatorSet.start();
        }else if(mRotateAnim != null && mRotateAnim.isPaused()){
            mRotateAnim.resume();
            mNeedleAnim.start();
        }

    }

    @Override
    public void onPlayerPause() {
        mRotateAnim = (ObjectAnimator) adapter.getCurrentFragment().getParentView().getTag(R.id.tag_animator);
        if (mRotateAnim != null && mRotateAnim.isRunning() && mNeedleAnim != null) {
            mRotateAnim.pause();
            mNeedleAnim.reverse();
        }
    }

    @Override
    public void onPublish(int duration, int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onTimer(long remain) {

    }

    @Override
    public void onMusicListUpdate() {

    }

    private void setViewPagerScrollTime(ViewPager viewPager){
        // 改变viewpager动画时间
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");// 拿到 ViewPager 的 mScroller 字段
            mField.setAccessible(true);// 取消 Java 语言访问检查
            MyScroller mScroller = new MyScroller(viewPager.getContext().getApplicationContext(), new LinearInterpolator());
            mField.set(viewPager, mScroller);// 对 mViewPager 的 mScroller 字段 重新赋值
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public class PlayPagerTransformer implements ViewPager.PageTransformer {
        boolean flag = true;
        @Override
        public void transformPage(View view, float position) {

            if (position == 0) {
                if (mPlayService.isPlaying()) {
                    flag = true;
                    mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                    if (mRotateAnim != null && !mRotateAnim.isRunning() && mNeedleAnim != null) {
                        mAnimatorSet = new AnimatorSet();
                        mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
                        mAnimatorSet.start();
                    }
                }

            } else if (position == -1 || position == -2 || position == 1) {

                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                if (mRotateAnim != null) {
                    mRotateAnim.setFloatValues(0);
                    mRotateAnim.end();
                    mRotateAnim = null;
                }
            } else {

                if (flag && mNeedleAnim != null) {
                    mNeedleAnim.reverse();
                    flag = false;
                }

                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                if (mRotateAnim != null) {
                    mRotateAnim.cancel();
                    float valueAvatar = (float) mRotateAnim.getAnimatedValue();
                    mRotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);
                }
            }
        }

    }

    public class MyScroller extends Scroller {
        private int animTime = VIEWPAGER_SCROLL_TIME;

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, animTime);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, animTime);
        }

        public void setDuration(int animTime) {
            this.animTime = animTime;
        }
    }
}
