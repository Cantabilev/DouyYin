package org.cantabile.douyin.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import org.cantabile.douyin.entity.MusicInfoBean;

import java.util.ArrayList;

/**
 * Created by simple on 2017/12/18.
 */

public class MusicCommUtil {
    private static final String SELECTION = MediaStore.Audio.AudioColumns.DURATION + " >= ?";


    public static ArrayList<MusicInfoBean> scanMusic(Context context) {
        ArrayList<MusicInfoBean> musics = new ArrayList<MusicInfoBean>();
        long filterTime = 60 * 1000;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,                            // 音乐id
                        MediaStore.Audio.AudioColumns.TITLE,        // 音乐标题
                        MediaStore.Audio.AudioColumns.ARTIST,       // 艺术家
                        MediaStore.Audio.AudioColumns.DURATION,     // 时长
                        MediaStore.Audio.AudioColumns.SIZE,         // 文件大小
                        MediaStore.Audio.AudioColumns.DATA,         // 文件路径
                        MediaStore.Audio.AudioColumns.ALBUM,        // 唱片
                        MediaStore.Audio.AudioColumns.ALBUM_ID,     // 唱片ID
                        MediaStore.Audio.AudioColumns.IS_MUSIC      // 是否为音乐
                },
                SELECTION,
                new String[]{
                        String.valueOf(filterTime)
                },
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return musics;
        }

        int i = 0;
        while (cursor.moveToNext()) {
            // 是否为音乐，魅族手机上始终为0
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
            if (!SystemUtil.isFlyme() && isMusic == 0) {
                continue;
            }

            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

            MusicInfoBean music = new MusicInfoBean();
            music.setMusicId(id);
            music.setType(MusicInfoBean.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setAlbumId(albumId);
            music.setDuration(duration);
            music.setPathUrl(path);
            music.setSize(fileSize);
            if (++i <= 20) {
                // 只加载前20首的缩略图
                CoverLoader.getInstance().loadThumbnail(music);
            }
            musics.add(music);
        }
        cursor.close();

        return musics;
    }

    public static Uri getMediaStoreAlbumCoverUri(long albumId) {
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, albumId);
    }

    /**
     * 将歌曲的时间转换为分秒的制度
     * @param time
     * @return
     */
    public static String formatTime(Long time){
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if(min.length() < 2)
            min = "0" + min;
        switch (sec.length()){
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0,2);
    }
}
