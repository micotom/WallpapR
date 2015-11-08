package com.dev.funglejunk.wallpapr.model.info;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CompressedInfo {

    @NonNull
    public final String userName;
    @NonNull
    public final String realName;
    @NonNull
    public final String title;
    @NonNull
    public final String description;
    @NonNull
    public final String date;

    private CompressedInfo(String userName,
                           @Nullable String realName,
                           @Nullable String title,
                           @Nullable String description,
                           @Nullable String date) {
        this.userName = userName != null ? userName : "";
        this.realName = realName != null ? realName : "";
        this.title = title != null ? title : "";
        this.description = description != null ? description : "";
        this.date = date != null ? date : "";
    }

    @Override
    public String toString() {
        return "CompressedInfo{" +
                "userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public static CompressedInfo fromRawInfo(RawInfo result) {
        RawInfo.Photo photo = result.photo;
        return new CompressedInfo(
                photo.owner != null ? photo.owner.username : "",
                photo.owner != null ? photo.owner.realname : "",
                photo.title != null ? photo.title.content : "",
                photo.description != null ? photo.description.content : "",
                photo.dates != null ? photo.dates.taken : ""
        );
    }

}
