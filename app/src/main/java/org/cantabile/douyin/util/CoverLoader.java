package org.cantabile.douyin.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import org.cantabile.douyin.R;
import org.cantabile.douyin.entity.MusicInfoBean;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by simple on 2017/12/19.
 */

public class CoverLoader {
    public static final int THUMBNAIL_MAX_LENGTH = 500;
    private static final String KEY_NULL = "null";

    // 封面缓存
    private LruCache<String, Bitmap> mCoverCache;
    private Context mContext;

    private Bitmap play_page_default_bg;// blur
    private Bitmap play_page_default_cover;// round
    private Bitmap default_cover;// default

    private enum Type {
        THUMBNAIL(""),// 缩略图
        BLUR("#BLUR"),// 模糊
        ROUND("#ROUND");// 圆

        private String value;

        Type(String value) {
            this.value = value;
        }
    }

    public static CoverLoader getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static CoverLoader instance = new CoverLoader();
    }

    private CoverLoader() {
        // 获取当前进程的可用内存（单位KB）
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 缓存大小为当前进程可用内存的1/8
        int cacheSize = maxMemory / 8;
        mCoverCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return bitmap.getAllocationByteCount() / 1024;
                } else {
                    return bitmap.getByteCount() / 1024;
                }
            }
        };
        mCoverCache.remove("10");
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public Bitmap loadThumbnail(MusicInfoBean music) {
        return loadCover(music, Type.THUMBNAIL);
    }

    public Bitmap loadBlur(MusicInfoBean music) {
        return loadCover(music, Type.BLUR);
    }

    public Bitmap loadRound(MusicInfoBean music) {
        return loadCover(music, Type.ROUND);
    }

    private Bitmap loadCover(MusicInfoBean music, Type type) {
        Bitmap bitmap;
        String key = getKey(music, type);
        if (TextUtils.isEmpty(key)) {
            bitmap = mCoverCache.get(KEY_NULL.concat(type.value));
            if (bitmap != null) {
                return bitmap;
            }

            bitmap = getDefaultCover(type);
            mCoverCache.put(KEY_NULL.concat(type.value), bitmap);
            return bitmap;
        }

        bitmap = mCoverCache.get(key);
        if (bitmap != null) {
            return bitmap;
        }

        bitmap = loadCoverByType(music, type);
        if (bitmap != null) {
            mCoverCache.put(key, bitmap);
            return bitmap;
        }

        return loadCover(null, type);
    }

    private String getKey(MusicInfoBean music, Type type) {
        if (music == null) {
            return null;
        }

        if (music.getType() == MusicInfoBean.Type.LOCAL && music.getAlbumId() > 0) {
            return String.valueOf(music.getAlbumId()).concat(type.value);
        } else if (music.getType() == MusicInfoBean.Type.ONLINE && !TextUtils.isEmpty(music.getCoverPath())) {
            return music.getCoverPath().concat(type.value);
        } else {
            return null;
        }
    }

    private Bitmap getDefaultCover(Type type) {
        switch (type) {
            case BLUR:
                if (play_page_default_bg == null)
                    play_page_default_bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play_page_default_bg);
                return play_page_default_bg;
            case ROUND:
                if (play_page_default_cover == null) {
                    play_page_default_cover = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play_page_default_cover);
                    play_page_default_cover = ImageUtil.resizeImage(play_page_default_cover, ScreenUtil.getScreenWidth() / 2, ScreenUtil.getScreenWidth() / 2);
                }
                return play_page_default_cover;
            default:
                if (default_cover == null)
                    default_cover = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_cover);
                return default_cover;
        }
    }

    private Bitmap loadCoverByType(MusicInfoBean music, Type type) {
        Bitmap bitmap;
        if (music.getType() == MusicInfoBean.Type.LOCAL) {
            bitmap = loadCoverFromMediaStore(music.getAlbumId());
        } else {
            bitmap = loadCoverFromFile(music.getCoverPath());
        }

        switch (type) {
            case BLUR:
                return ImageUtil.blur(bitmap);
            case ROUND:
                bitmap = ImageUtil.resizeImage(bitmap, ScreenUtil.getScreenWidth() / 2, ScreenUtil.getScreenWidth() / 2);
                return ImageUtil.createCircleImage(bitmap);
            default:
                return bitmap;
        }
    }

    /**
     * 从媒体库加载封面<br>
     * 本地音乐
     */
    private Bitmap loadCoverFromMediaStore(long albumId) {
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = MusicCommUtil.getMediaStoreAlbumCoverUri(albumId);
        InputStream is;
        try {
            is = resolver.openInputStream(uri);
        } catch (FileNotFoundException ignored) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 从下载的图片加载封面<br>
     * 网络音乐
     */
    private Bitmap loadCoverFromFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }
}
