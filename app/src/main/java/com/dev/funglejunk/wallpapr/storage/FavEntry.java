package com.dev.funglejunk.wallpapr.storage;

public class FavEntry {

    public final String url;

    public FavEntry(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FavEntry{" +
                "url='" + url + '\'' +
                '}';
    }
}
