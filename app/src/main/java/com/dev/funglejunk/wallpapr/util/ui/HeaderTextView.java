package com.dev.funglejunk.wallpapr.util.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class HeaderTextView extends TextView {

    public HeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/AguafinaScript-Regular.ttf"));
    }

}
