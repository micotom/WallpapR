package com.dev.funglejunk.wallpapr.storage;

import com.dev.funglejunk.wallpapr.model.search.CompressedSearch;
import com.dev.funglejunk.wallpapr.util.StringUtils;

public class Entry {

    public final long timestamp;
    public final CompressedSearch result;

    public Entry(long timestamp, CompressedSearch result) {
        this.timestamp = timestamp;
        this.result = result;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "timestamp=" + StringUtils.getLocalDateFromMilliseconds(timestamp) +
                ", result=" + result +
                '}';
    }
}
