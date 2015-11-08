package com.dev.funglejunk.wallpapr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dev.funglejunk.wallpapr.WallpapRApplication;

import timber.log.Timber;

public abstract class AbstractReceiver extends BroadcastReceiver {

    public final static String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public final static String ACTION_REQ_UPDATE = WallpapRApplication.PACKAGE_NAME + ".REQ_UPDATE";

    Intent serviceIntent;

    @Override
    public final void onReceive(Context context, Intent intent) {
        Timber.d("intent received");
        handleIntent(context);
    }

    abstract void handleIntent(Context context);

}
