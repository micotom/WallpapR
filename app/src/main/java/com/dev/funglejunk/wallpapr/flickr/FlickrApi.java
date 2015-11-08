package com.dev.funglejunk.wallpapr.flickr;

public class FlickrApi {

    public final static String API_BASE = "https://api.flickr.com";
    public final static String FARM_BASE = "https://farm1.staticflickr.com";

    public final static String SEARCH_METHOD = "flickr.photos.search";
    public final static String INFO_METHOD = "flickr.photos.getInfo";

    private FlickrApi(){
        throw new UnsupportedOperationException();
    }

}
