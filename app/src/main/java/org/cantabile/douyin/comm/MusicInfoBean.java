package org.cantabile.douyin.comm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simple on 2017/12/13.
 */

public class MusicInfoBean implements Parcelable {
    private String iconUrl;// 图片地址
    private String singName;// 歌曲名
    private String singer;// 歌手
    // TODO 歌词
    private boolean favorite;// 是否喜欢
    private boolean download;// 是否下载
    private int commentCount;// 评论数
    private String album;// 专辑
    private String source;// 来源
    private int time;// 歌曲时长
    private int currTime;// 当前播放位置

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSingName() {
        return singName;
    }

    public void setSingName(String singName) {
        this.singName = singName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCurrTime() {
        return currTime;
    }

    public void setCurrTime(int currTime) {
        this.currTime = currTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iconUrl);
        dest.writeString(this.singName);
        dest.writeString(this.singer);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.download ? (byte) 1 : (byte) 0);
        dest.writeInt(this.commentCount);
        dest.writeString(this.album);
        dest.writeString(this.source);
        dest.writeInt(this.time);
        dest.writeInt(this.currTime);
    }

    public MusicInfoBean() {
    }

    protected MusicInfoBean(Parcel in) {
        this.iconUrl = in.readString();
        this.singName = in.readString();
        this.singer = in.readString();
        this.favorite = in.readByte() != 0;
        this.download = in.readByte() != 0;
        this.commentCount = in.readInt();
        this.album = in.readString();
        this.source = in.readString();
        this.time = in.readInt();
        this.currTime = in.readInt();
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
