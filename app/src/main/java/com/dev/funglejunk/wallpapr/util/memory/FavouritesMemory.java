package com.dev.funglejunk.wallpapr.util.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public enum FavouritesMemory {

    INSTANCE;

    private Context context;
    private LruCache<String, Bitmap> cache;

    public void init(Context context) {
        this.context = context;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Observable<Bitmap> request(@NonNull final String url) {
        final Bitmap bmp = cache.get(url);
        return bmp == null ? loadBitmapObservable(url) : getBitmapObservable(bmp);
    }

    @NonNull
    private Observable<Bitmap> getBitmapObservable(final Bitmap bmp) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(bmp);
                subscriber.onCompleted();
            }
        });
    }

    private Observable<Bitmap> loadBitmapObservable(final String url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Bitmap bmp = Picasso.with(context)
                            .load(url)
                            .get();
                    cache.put(url, bmp);
                    subscriber.onNext(bmp);
                }
                catch (IOException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

}
