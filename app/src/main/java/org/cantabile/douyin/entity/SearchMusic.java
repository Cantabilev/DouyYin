package org.cantabile.douyin.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Created by You
 * @email 1269859055@qq.com
 * @data 2018/8/14
 **/
public class SearchMusic {
    @SerializedName("song")
    private List<Song> song;

    public List<Song> getSong() {
        return song;
    }

    public void setSong(List<Song> song) {
        this.song = song;
    }

    public static class Song {
        @SerializedName("songname")
        private String songName;
        @SerializedName("artistname")
        private String artistName;
        @SerializedName("songid")
        private String songId;

        public String getSongName() {
            return songName;
        }

        public void setSongName(String songName) {
            this.songName = songName;
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        public String getSongId() {
            return songId;
        }

        public void setSongId(String songId) {
            this.songId = songId;
        }
    }
}
