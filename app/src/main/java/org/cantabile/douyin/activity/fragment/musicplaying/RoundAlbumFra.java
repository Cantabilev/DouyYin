package org.cantabile.douyin.activity.fragment.musicplaying;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.cantabile.douyin.R;
import org.cantabile.douyin.entity.MusicInfoBean;
import org.cantabile.douyin.util.MusicCommUtil;

import java.lang.ref.WeakReference;

/**
 * Created by simple on 2017/12/26.
 */

public class RoundAlbumFra extends Fragment {
    private static final String KEY_ALBUM = "KEY_ALBUM";
    private static final long DURATION_TIME = 20000L;// 设置 转速    一个周期[0.0f, 360.0f] 多长时间 单位 ms

    private WeakReference<ObjectAnimator> animatorWeakReference;
    private SimpleDraweeView sdv;
    private MusicInfoBean mMusic;
    private ObjectAnimator animator;

    public static RoundAlbumFra newInstance(MusicInfoBean music) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_ALBUM, music);
        RoundAlbumFra fragment = new RoundAlbumFra();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_musicplaying_round_album, container, false);
        ((ViewGroup) rootView).setAnimationCacheEnabled(false);
        if (getArguments() != null) {
            mMusic = getArguments().getParcelable(KEY_ALBUM);
        }
        sdv = (SimpleDraweeView) rootView.findViewById(R.id.sdv);
        //初始化圆角圆形参数对象
        RoundingParams rp = new RoundingParams();
        //设置图像是否为圆形
        rp.setRoundAsCircle(true);
        //设置圆角半径
        //rp.setCornersRadius(20);
        //分别设置左上角、右上角、左下角、右下角的圆角半径
        //rp.setCornersRadii(20,25,30,35);
        //分别设置（前2个）左上角、(3、4)右上角、(5、6)左下角、(7、8)右下角的圆角半径
        //rp.setCornersRadii(new float[]{20,25,30,35,40,45,50,55});
        //设置边框颜色及其宽度
        rp.setBorder(Color.BLACK, 6);

        //获取GenericDraweeHierarchy对象
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources())
                //设置圆形圆角参数
                .setRoundingParams(rp)
                //设置圆角半径
                //.setRoundingParams(RoundingParams.fromCornersRadius(20))
                //分别设置左上角、右上角、左下角、右下角的圆角半径
                //.setRoundingParams(RoundingParams.fromCornersRadii(20,25,30,35))
                //分别设置（前2个）左上角、(3、4)右上角、(5、6)左下角、(7、8)右下角的圆角半径
                //.setRoundingParams(RoundingParams.fromCornersRadii(new float[]{20,25,30,35,40,45,50,55}))
                //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                //.setRoundingParams(RoundingParams.asCircle())
                //设置淡入淡出动画持续时间(单位：毫秒ms)
                .setFadeDuration(300)
                //构建
                .build();

        //设置Hierarchy
        sdv.setHierarchy(hierarchy);

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                //FLog.d("Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                sdv.setImageURI(Uri.parse("res:/" + R.drawable.default_artist));
            }
        };
        if (mMusic == null) {
            sdv.setImageURI(Uri.parse("res:/" + R.drawable.default_artist));
        } else {
            try {
//                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(albumPath)).build();
                // 本地音乐 直接 用albumId 向媒体库取 uri
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(MusicCommUtil.getMediaStoreAlbumCoverUri(mMusic.getAlbumId())).build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setOldController(sdv.getController())
                        .setImageRequest(request)
                        .setControllerListener(controllerListener)
                        .build();

                sdv.setController(controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        animatorWeakReference = new  WeakReference<ObjectAnimator>(new ObjectAnimator());
//        animator = animatorWeakReference.get();
        animatorWeakReference = new WeakReference(ObjectAnimator.ofFloat(getView(), "rotation", new float[]{0.0F, 360.0F}));
        animator = animatorWeakReference.get();
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.setDuration(DURATION_TIME);// 设置 转速    一个周期[0.0f, 360.0f] 多长时间
        animator.setInterpolator(new LinearInterpolator());

        if (getView() != null)
            getView().setTag(R.id.tag_animator, this.animator);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume

        } else {
            //相当于Fragment的onPause

        }
    }

    public View getParentView(){
        return getView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("roundfragment"," id = " + hashCode());
        if (animator != null) {
            animator = null;
            Log.e("roundfragment"," id = " + hashCode() + "  , animator destroy");
        }
    }
}
