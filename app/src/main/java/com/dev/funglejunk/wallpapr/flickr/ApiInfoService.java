package com.dev.funglejunk.wallpapr.flickr;

import com.dev.funglejunk.wallpapr.model.info.RawInfo;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ApiInfoService {

    @GET("/services/rest/")
    Observable<RawInfo> get(@Query("method") String method,
                              @Query("api_key") String apiKey,
                              @Query("photo_id") String id,
                              @Query("secret") String secret,
                              @Query("format") String format,
                              @Query("nojsoncallback") int noJsonCallback);

}
