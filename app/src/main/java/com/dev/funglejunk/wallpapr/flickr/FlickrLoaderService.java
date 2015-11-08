package com.dev.funglejunk.wallpapr.flickr;

import android.app.IntentService;
import android.content.Intent;

import com.dev.funglejunk.wallpapr.Config;
import com.dev.funglejunk.wallpapr.model.info.RawInfo;
import com.dev.funglejunk.wallpapr.model.search.CompressedSearch;
import com.dev.funglejunk.wallpapr.model.search.RawSearch;
import com.dev.funglejunk.wallpapr.rx.RxServiceCallback;
import com.dev.funglejunk.wallpapr.util.Network;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class FlickrLoaderService extends IntentService {

    private static final ApiSearchService apiSearchService;
    private static final ApiInfoService apiInfoService;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FlickrApi.API_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiSearchService = retrofit.create(ApiSearchService.class);
        apiInfoService = retrofit.create(ApiInfoService.class);
    }

    public FlickrLoaderService() {
        super(FlickrLoaderService.class.getSimpleName());
    }

    private static Observable<RawSearch> createSearchObservable(String keyword, int count) {
        return apiSearchService.get(
                FlickrApi.SEARCH_METHOD,
                Credentials.KEY,
                keyword,
                count,
                "1,2,3,4,5,6,7",
                1,
                "photos",
                "json",
                1
        );
    }

    private static Observable<RawInfo> createInfoObservable(String photoId, String secret) {
        return apiInfoService.get(
                FlickrApi.INFO_METHOD,
                Credentials.KEY,
                photoId,
                secret,
                "json",
                1
        );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("api request");

        if (!Network.isConnected(this)) {
            Timber.w("no network - skip query");
            RxServiceCallback.report(null);
            return;
        }

        createSearchObservable(Config.KEYWORD, Config.NR_OF_IMAGES)
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RawSearch>>() {
                    @Override
                    public Observable<? extends RawSearch> call(Throwable throwable) {
                        Timber.w(throwable, "search observable null callback");
                        return null;
                    }
                })
                .concatMap(new Func1<RawSearch, Observable<RawSearch.Photos.Photo>>() {
                    @Override
                    public Observable<RawSearch.Photos.Photo> call(RawSearch rawSearch) {
                        return Observable.from(rawSearch.photos.photo);
                    }
                })
                .map(new Func1<RawSearch.Photos.Photo, CompressedSearch>() {
                    @Override
                    public CompressedSearch call(RawSearch.Photos.Photo photo) {
                        return CompressedSearch.fromRawSearch(photo);
                    }
                })
                .map(new Func1<CompressedSearch, Observable<RawInfo>>() {
                    @Override
                    public Observable<RawInfo> call(CompressedSearch compressedSearch) {
                        return createInfoObservable(compressedSearch.imageId, compressedSearch.secret);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Observable<RawInfo>>() {
                    @Override
                    public void call(Observable<RawInfo> rawInfo) {
                        if (rawInfo != null) {
                            rawInfo.subscribe(
                                new Action1<RawInfo>() {
                                    @Override
                                    public void call(RawInfo rawInfo) {
                                        Timber.i("trigger callback");
                                        RxServiceCallback.report(rawInfo);
                                    }
                                },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Timber.w("error task", throwable);
                                        RxServiceCallback.report(null);
                                    }
                           });
                        }
                        else {
                            Timber.w("got nullified raw info");
                            RxServiceCallback.report(null);
                        }
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, "error loading image");
                    }
                });

    }

}
