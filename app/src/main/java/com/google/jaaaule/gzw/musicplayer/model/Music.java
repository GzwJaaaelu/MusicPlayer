package com.google.jaaaule.gzw.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/6/7.
 */

public class Music implements Parcelable {
    public long id;
    public String title;
    public String artist;
    public long duration;
    public long size;
    public String path;
    public String album;

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", album='" + album + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeLong(this.duration);
        dest.writeLong(this.size);
        dest.writeString(this.path);
        dest.writeString(this.album);
    }

    public Music() {
    }

    protected Music(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.artist = in.readString();
        this.duration = in.readLong();
        this.size = in.readLong();
        this.path = in.readString();
        this.album = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}
