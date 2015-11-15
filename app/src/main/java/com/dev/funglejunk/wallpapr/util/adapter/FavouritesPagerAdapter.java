package com.dev.funglejunk.wallpapr.util.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.dev.funglejunk.wallpapr.storage.FavEntry;
import com.dev.funglejunk.wallpapr.util.memory.GalleryMemory;

import rx.functions.Action1;

public class FavouritesPagerAdapter extends ImageAdapter<FavEntry> {

    public FavouritesPagerAdapter(Context context) {
        super(context);
    }

    @Override
    public void setImageForPage(int position) {
        final ImageView view = views.get(position);
        view.setVisibility(View.INVISIBLE);
        GalleryMemory.INSTANCE.request(items.get(position).url)
            .subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    view.setImageBitmap(bitmap);
                    view.setVisibility(View.VISIBLE);
                }
            });
    }

}
