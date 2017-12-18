package org.cantabile.douyin.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import org.cantabile.douyin.comm.MusicInfoBean;

import java.util.ArrayList;

/**
 * Created by simple on 2017/12/18.
 */

public class MusicCommUtil {

    public static ArrayList<MusicInfoBean> getMusics(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<MusicInfoBean> musics = new ArrayList<MusicInfoBean>();
        for (int i = 0; i < cursor.getCount(); i++) {
            MusicInfoBean music = new MusicInfoBean();//新建一个歌曲对象,将从cursor里读出的信息存放进去,直到取完cursor里面的内容为止.
            cursor.moveToNext();

            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id

            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小

            String pathUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));  //文件路径

            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)); //唱片

            long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)); //唱片ID

            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            if (isMusic != 0 && duration/(1000 * 60) >= 1) {     //只把1分钟以上的音乐添加到集合当中
                music.setMusicId(id);
                music.setTitle(title);
                music.setArtist(artist);
                music.setDuration(duration);
                music.setSize(size);
                music.setPathUrl(pathUrl);
                music.setAlbum(album);
                music.setAlbumId(album_id);
                musics.add(music);
            }
        }
        return musics;
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
