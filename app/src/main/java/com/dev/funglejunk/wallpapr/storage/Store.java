package com.dev.funglejunk.wallpapr.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dev.funglejunk.wallpapr.Config;
import com.dev.funglejunk.wallpapr.R;
import com.dev.funglejunk.wallpapr.model.search.CompressedSearch;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public enum Store {

    INSTANCE;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Gson gson;

    public void init(@NonNull final Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
    }

    public boolean hasEntry() {
        return preferences.getLong(Config.PREFS_TIMESTAMP, -1) != -1;
    }

    public Observable<Entry> getLastEntry() {
        return Observable.create(new Observable.OnSubscribe<Entry>() {
            @Override
            public void call(Subscriber<? super Entry> subscriber) {
                subscriber.onNext(
                        new Entry(preferences.getLong(Config.PREFS_TIMESTAMP, -1),
                                gson.fromJson(
                                        preferences.getString(Config.PREFS_LAST_RESULT, null),
                                        CompressedSearch.class
                                )
                        )
                );
                subscriber.onCompleted();
            }
        });
    }

    public void persist(@NonNull final Entry entry) {
        Timber.i("persist: %s", entry);
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String json = gson.toJson(entry.result);
                editor.putString(Config.PREFS_LAST_RESULT, json);
                editor.putLong(Config.PREFS_TIMESTAMP, entry.timestamp);
                subscriber.onNext(editor.commit());
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean success) {
                if (success) {
                    Timber.d("saved last answer: %s", entry.result);
                } else {
                    Timber.e("could not save last answer");
                }
            }
        });
    }

    @Nullable
    public CompressedSearch getLastEntryResult() {
        String lastAnswerJson = preferences.getString(Config.PREFS_LAST_RESULT, null);
        return lastAnswerJson == null
                ? null
                : gson.fromJson(lastAnswerJson, CompressedSearch.class);
    }

}
