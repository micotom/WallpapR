package com.dev.funglejunk.wallpapr.flickr;

import com.dev.funglejunk.wallpapr.model.search.RawSearch;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ApiSearchService {

    @GET("/services/rest/")
    Observable<RawSearch> get(@Query("method") String method,
                                 @Query("api_key") String apiKey,
                                 @Query("tags") String tags,
                                 @Query("per_page") int count,
                                 @Query("license") String license,
                                 @Query("content_type") int contentType,
                                @Query("media") String media,
                                @Query("format") String format,
                                 @Query("nojsoncallback") int noJsonCallback);

}
