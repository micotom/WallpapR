package com.dev.funglejunk.wallpapr.util;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FixedSwipeLayout extends SwipeRefreshLayout {

    public FixedSwipeLayout(Context context) {
        super(context);
    }

    public FixedSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean handleTouch = true;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                handleTouch = false;
                break;
            default:
                if (handleTouch) {
                    return super.onTouchEvent(ev);
                }
                handleTouch = onInterceptTouchEvent(ev);
                switch (action) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handleTouch = true;
                        break;
                }
                break;
        }
        return true;
    }

}
