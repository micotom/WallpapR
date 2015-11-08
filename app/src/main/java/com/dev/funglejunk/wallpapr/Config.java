package com.dev.funglejunk.wallpapr;

public final class Config {

    public static final int NR_OF_IMAGES = 10;
    public static final String KEYWORD = "abstract";

    public static final long REQUEST_INTERVAL = 60000; // ms

    public static final String PREFS_LAST_RESULT = "last_answer";
    public static final String PREFS_TIMESTAMP = "timestamp";

    private Config(){
        throw new UnsupportedOperationException();
    }

}
