package com.dev.funglejunk.wallpapr.receiver;

import android.content.Context;
import android.content.Intent;

import com.dev.funglejunk.wallpapr.flickr.FlickrLoaderService;

public class SingleUpdateReceiver extends AbstractReceiver {

    @Override
    void handleIntent(Context context) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(context, FlickrLoaderService.class);
        }
        context.startService(serviceIntent);
    }

}
