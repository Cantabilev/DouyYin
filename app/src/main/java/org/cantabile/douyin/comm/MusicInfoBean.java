package org.cantabile.douyin.comm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simple on 2017/12/13.
 */

public class MusicInfoBean implements Parcelable {
    private long musicId;// 音乐ID
    private long albumId;// 唱片ID
    private String album;// 唱片
    private String title;// 音乐标题
    private String artist;// 艺术家
    // TODO 歌词
    private boolean favorite;// 是否喜欢
    private boolean download;// 是否下载
    private int commentCount;// 评论数
    private String source;// 来源
    private long duration;// 歌曲时长
    private int currTime;// 当前播放位置
    private long size;// 文件大小
    private String pathUrl;// 文件路径

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCurrTime() {
        return currTime;
    }

    public void setCurrTime(int currTime) {
        this.currTime = currTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.musicId);
        dest.writeLong(this.albumId);
        dest.writeString(this.album);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.download ? (byte) 1 : (byte) 0);
        dest.writeInt(this.commentCount);
        dest.writeString(this.source);
        dest.writeLong(this.duration);
        dest.writeInt(this.currTime);
        dest.writeLong(this.size);
        dest.writeString(this.pathUrl);
    }

    public MusicInfoBean() {
    }

    protected MusicInfoBean(Parcel in) {
        this.musicId = in.readLong();
        this.albumId = in.readLong();
        this.album = in.readString();
        this.title = in.readString();
        this.artist = in.readString();
        this.favorite = in.readByte() != 0;
        this.download = in.readByte() != 0;
        this.commentCount = in.readInt();
        this.source = in.readString();
        this.duration = in.readLong();
        this.currTime = in.readInt();
        this.size = in.readLong();
        this.pathUrl = in.readString();
    }

    public static final Parcelable.Creator<MusicInfoBean> CREATOR = new Parcelable.Creator<MusicInfoBean>() {
        @Override
        public MusicInfoBean createFromParcel(Parcel source) {
            return new MusicInfoBean(source);
        }

        @Override
        public MusicInfoBean[] newArray(int size) {
            return new MusicInfoBean[size];
        }
    };
}
