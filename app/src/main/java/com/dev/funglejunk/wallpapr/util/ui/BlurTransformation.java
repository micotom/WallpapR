package com.dev.funglejunk.wallpapr.util.ui;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

public class BlurTransformation implements Transformation {

    private final String cacheKey;
    private final RenderScript renderScript;
    private final ScriptIntrinsicBlur blurScript;

    public BlurTransformation(final RenderScript renderScript, final float blurRadius) {
        this.renderScript = renderScript;
        blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blurScript.setRadius(blurRadius);
        cacheKey = "BlurTransformation:" + blurRadius;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Allocation input = Allocation.createFromBitmap(renderScript, source);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        blurScript.setInput(input);
        blurScript.forEach(output);
        output.copyTo(source);
        return source;
    }

    @Override
    public String key() {
        return cacheKey;
    }

}
