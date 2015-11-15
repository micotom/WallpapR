package com.dev.funglejunk.wallpapr;

public final class Config {

    public static final int NR_OF_IMAGES = 10;
    public static final String KEYWORD = "abstract";

    public static final long REQUEST_INTERVAL = 60000; // ms

    public static final String PREFS_LAST_RESULT = "last_answer";
    public static final String PREFS_TIMESTAMP = "timestamp";

    public static final String INTENT_NEW_REQUEST = "new_request";
    public static final String INTENT_FAVOURITES = "favourites";

    private Config(){
        throw new UnsupportedOperationException();
    }

}
