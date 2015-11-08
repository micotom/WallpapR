package com.dev.funglejunk.wallpapr.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RawSearch {

    @SerializedName("photos")
    @Expose
    public Photos photos;

    @Override
    public String toString() {
        return "SearchAnswer{" +
                "photos=" + photos +
                '}';
    }

    public static class Photos {

        @SerializedName("page")
        @Expose
        public int page;
        @SerializedName("pages")
        @Expose
        public int pages;
        @SerializedName("perpage")
        @Expose
        public int perpage;
        @SerializedName("total")
        @Expose
        public String total;
        @SerializedName("photo")
        @Expose
        public List<Photo> photo = new ArrayList<>();

        @Override
        public String toString() {
            return "Photos{" +
                    "page=" + page +
                    ", pages=" + pages +
                    ", perpage=" + perpage +
                    ", total='" + total + '\'' +
                    ", photo=" + photo +
                    '}';
        }

        public static class Photo {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("owner")
            @Expose
            public String owner;
            @SerializedName("secret")
            @Expose
            public String secret;
            @SerializedName("server")
            @Expose
            public String server;
            @SerializedName("farm")
            @Expose
            public int farm;
            @SerializedName("title")
            @Expose
            public String title;
            @SerializedName("ispublic")
            @Expose
            public int ispublic;
            @SerializedName("isfriend")
            @Expose
            public int isfriend;
            @SerializedName("isfamily")
            @Expose
            public int isfamily;

            @Override
            public String toString() {
                return "Photo{" +
                        "id='" + id + '\'' +
                        ", owner='" + owner + '\'' +
                        ", secret='" + secret + '\'' +
                        ", server='" + server + '\'' +
                        ", farm=" + farm +
                        ", title='" + title + '\'' +
                        ", ispublic=" + ispublic +
                        ", isfriend=" + isfriend +
                        ", isfamily=" + isfamily +
                        '}';
            }
        }

    }

}
