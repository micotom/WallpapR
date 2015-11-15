package com.dev.funglejunk.wallpapr.util.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.dev.funglejunk.wallpapr.model.info.RawInfo;
import com.dev.funglejunk.wallpapr.util.memory.GalleryMemory;

import rx.functions.Action1;

public class GalleryPagerAdapter extends ImageAdapter<RawInfo> {

    public GalleryPagerAdapter(Context context) {
        super(context);
    }

    @Override
    public void setImageForPage(final int position) {
        final ImageView view = views.get(position);
        view.setVisibility(View.INVISIBLE);
        GalleryMemory.INSTANCE.request(items.get(position).toFarmUrl())
            .subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    view.setImageBitmap(bitmap);
                    view.setVisibility(View.VISIBLE);
                }
            });
    }

}
