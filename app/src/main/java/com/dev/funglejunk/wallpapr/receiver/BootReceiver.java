package com.dev.funglejunk.wallpapr.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.dev.funglejunk.wallpapr.Config;
import com.dev.funglejunk.wallpapr.flickr.FlickrLoaderService;

public class BootReceiver extends AbstractReceiver {

    @Override
    void handleIntent(Context context) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(context, FlickrLoaderService.class);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pending = PendingIntent.getService(context, 0, serviceIntent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                Config.REQUEST_INTERVAL, pending);
    }

}
