package com.dev.funglejunk.wallpapr.util.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;

import com.squareup.picasso.Transformation;

public class ScaleDownTransformation implements Transformation {

    private final String cacheKey;
    private final float targetWidth;

    public ScaleDownTransformation(final Activity activity, float screenWidthPercent) {
        if (screenWidthPercent >= 1f || screenWidthPercent <= 0f) {
            screenWidthPercent = 1f;
        }
        final Display display = activity.getWindowManager().getDefaultDisplay();
        final Point screenSize = new Point();
        display.getSize(screenSize);
        this.targetWidth = screenWidthPercent * screenSize.x;
        cacheKey = "ScaleDownTransformation:" + screenWidthPercent;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final float scaleFactor = this.targetWidth / source.getWidth();
        if (scaleFactor >= 1f) {

            // don't scale up
            return source;
        }
        final int targetWidth = (int) this.targetWidth;
        final int targetHeight = (int) (source.getHeight() * scaleFactor);
        if (targetWidth <= 0 || targetHeight <= 0) {
            return source;
        }
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                source,
                targetWidth,
                targetHeight,
                true
        );
        source.recycle();
        return scaledBitmap;
    }

    @Override
    public String key() {
        return cacheKey;
    }

}
