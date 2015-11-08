package com.dev.funglejunk.wallpapr.model.info;

import com.dev.funglejunk.wallpapr.flickr.FlickrApi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RawInfo {

    @SerializedName("photo")
    @Expose
    public Photo photo;
    @SerializedName("stat")
    @Expose
    public String stat;

    public String toFarmUrl() {
        return FlickrApi.FARM_BASE + "/" + photo.server + "/" + photo.id + "_"
                + photo.secret + "_b.jpg";
    }

    public static class Comments {

        @SerializedName("_content")
        @Expose
        public Integer content;

    }

    public static class Dates {

        @SerializedName("posted")
        @Expose
        public String posted;
        @SerializedName("taken")
        @Expose
        public String taken;
        @SerializedName("takengranularity")
        @Expose
        public Integer takengranularity;
        @SerializedName("takenunknown")
        @Expose
        public Integer takenunknown;
        @SerializedName("lastupdate")
        @Expose
        public String lastupdate;

    }

    public static class Description {

        @SerializedName("_content")
        @Expose
        public String content;

    }

    public static class Editability {

        @SerializedName("cancomment")
        @Expose
        public Integer cancomment;
        @SerializedName("canaddmeta")
        @Expose
        public Integer canaddmeta;

    }

    public static class Notes {

        @SerializedName("note")
        @Expose
        public List<Object> note = new ArrayList<>();

    }

    public static class Owner {

        @SerializedName("nsid")
        @Expose
        public String nsid;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("realname")
        @Expose
        public String realname;
        @SerializedName("location")
        @Expose
        public String location;
        @SerializedName("iconserver")
        @Expose
        public Integer iconserver;
        @SerializedName("iconfarm")
        @Expose
        public Integer iconfarm;
        @SerializedName("path_alias")
        @Expose
        public String pathAlias;

    }

    public static class People {

        @SerializedName("haspeople")
        @Expose
        public Integer haspeople;

    }

    public static class Photo {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("secret")
        @Expose
        public String secret;
        @SerializedName("server")
        @Expose
        public String server;
        @SerializedName("farm")
        @Expose
        public Integer farm;
        @SerializedName("dateuploaded")
        @Expose
        public String dateuploaded;
        @SerializedName("isfavorite")
        @Expose
        public Integer isfavorite;
        @SerializedName("license")
        @Expose
        public Integer license;
        @SerializedName("safety_level")
        @Expose
        public Integer safetyLevel;
        @SerializedName("rotation")
        @Expose
        public Integer rotation;
        @SerializedName("originalsecret")
        @Expose
        public String originalsecret;
        @SerializedName("originalformat")
        @Expose
        public String originalformat;
        @SerializedName("owner")
        @Expose
        public Owner owner;
        @SerializedName("title")
        @Expose
        public Title title;
        @SerializedName("description")
        @Expose
        public Description description;
        @SerializedName("visibility")
        @Expose
        public Visibility visibility;
        @SerializedName("dates")
        @Expose
        public Dates dates;
        @SerializedName("views")
        @Expose
        public Integer views;
        @SerializedName("editability")
        @Expose
        public Editability editability;
        @SerializedName("publiceditability")
        @Expose
        public Publiceditability publiceditability;
        @SerializedName("usage")
        @Expose
        public Usage usage;
        @SerializedName("comments")
        @Expose
        public Comments comments;
        @SerializedName("notes")
        @Expose
        public Notes notes;
        @SerializedName("people")
        @Expose
        public People people;
        @SerializedName("tags")
        @Expose
        public Tags tags;
        @SerializedName("urls")
        @Expose
        public Urls urls;
        @SerializedName("media")
        @Expose
        public String media;

    }

    public static class Publiceditability {

        @SerializedName("cancomment")
        @Expose
        public Integer cancomment;
        @SerializedName("canaddmeta")
        @Expose
        public Integer canaddmeta;

    }

    public static class Tag {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("author")
        @Expose
        public String author;
        @SerializedName("authorname")
        @Expose
        public String authorname;
        @SerializedName("raw")
        @Expose
        public String raw;
        @SerializedName("_content")
        @Expose
        public String Content;
        @SerializedName("machine_tag")
        @Expose
        public String machineTag;

    }

    public static class Tags {

        @SerializedName("tag")
        @Expose
        public List<Tag> tag = new ArrayList<>();

    }

    public static class Title {

        @SerializedName("_content")
        @Expose
        public String content;

    }

    public static class Url {

        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("_content")
        @Expose
        public String content;

    }

    public static class Urls {

        @SerializedName("url")
        @Expose
        public List<Url> url = new ArrayList<Url>();

    }

    public static class Usage {

        @SerializedName("candownload")
        @Expose
        public Integer candownload;
        @SerializedName("canblog")
        @Expose
        public Integer canblog;
        @SerializedName("canprint")
        @Expose
        public Integer canprint;
        @SerializedName("canshare")
        @Expose
        public Integer canshare;

    }

    public static class Visibility {

        @SerializedName("ispublic")
        @Expose
        public Integer ispublic;
        @SerializedName("isfriend")
        @Expose
        public Integer isfriend;
        @SerializedName("isfamily")
        @Expose
        public Integer isfamily;

    }

}
