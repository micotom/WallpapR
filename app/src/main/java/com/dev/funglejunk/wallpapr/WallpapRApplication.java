package com.dev.funglejunk.wallpapr;

import android.app.Application;
import android.util.Log;

import com.dev.funglejunk.wallpapr.storage.FavStore;
import com.dev.funglejunk.wallpapr.storage.Store;

import timber.log.Timber;

public class WallpapRApplication extends Application {

    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();

        PACKAGE_NAME = getApplicationContext().getPackageName();

        initStore();
        initTimber();
    }

    private void initStore() {
        Store.INSTANCE.init(this);
        FavStore.INSTANCE.init(this);
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            if (t != null) {
                Log.w(tag, t);
            }
            else {
                Log.w(tag, message);
            }
        }
    }

}
