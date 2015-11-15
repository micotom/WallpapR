package com.dev.funglejunk.wallpapr.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.dev.funglejunk.wallpapr.R;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public enum FavStore {

    INSTANCE;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Gson gson;

    private long count;

    public void init(@NonNull final Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.preference_favs),
                Context.MODE_PRIVATE);
        editor = preferences.edit();
        count = preferences.getAll().size(); // TODO int max sets the limit
        gson = new Gson();
    }

    public Observable<Boolean> has(@NonNull final FavEntry entry) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean found = false;
                for (Object value : preferences.getAll().values()) {
                    FavEntry oldEntry = gson.fromJson((String) value, FavEntry.class);
                    if (oldEntry.url.equals(entry.url)) {
                        found = true;
                        break;
                    }
                }
                subscriber.onNext(found);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> persist(@NonNull final FavEntry entry) {
        Timber.i("persist (%d): %s", count, entry);
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String json = gson.toJson(entry);
                editor.putString("E" + count, json);
                ++count;
                subscriber.onNext(editor.commit());
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FavEntry> getAll() {
        return Observable.create(new Observable.OnSubscribe<FavEntry>() {
            @Override
            public void call(Subscriber<? super FavEntry> subscriber) {
            for (Object value : preferences.getAll().values()) {
                FavEntry entry = gson.fromJson((String) value, FavEntry.class);
                subscriber.onNext(entry);
            }
            subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

}
