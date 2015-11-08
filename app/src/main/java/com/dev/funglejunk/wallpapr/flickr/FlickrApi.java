package com.dev.funglejunk.wallpapr.flickr;

public class FlickrApi {

    public final static String KEY = "044eb30c71ad508a78a9d2a022443fd4";
    public final static String KEY_SECRET = "c872c94729a2e03d";

    public final static String API_BASE = "https://api.flickr.com";
    public final static String FARM_BASE = "https://farm1.staticflickr.com";

    public final static String SEARCH_METHOD = "flickr.photos.search";
    public final static String INFO_METHOD = "flickr.photos.getInfo";

    private FlickrApi(){
        throw new UnsupportedOperationException();
    }

}
