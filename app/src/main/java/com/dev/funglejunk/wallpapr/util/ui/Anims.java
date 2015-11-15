package com.dev.funglejunk.wallpapr.util.ui;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class Anims {

    private final static long DURATION_MS = 1000;

    private static AnimationSet rightToLeftSet;
    private static AnimationSet leftToRightSet;

    static {
        /*
        RIGHT TO LEFT
         */
        Animation rotate = new RotateAnimation(
                0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());

        Animation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.75f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f
        );
        translate.setInterpolator(new BounceInterpolator());

        rightToLeftSet = new AnimationSet(true);
        rightToLeftSet.setDuration(DURATION_MS);
        rightToLeftSet.setFillAfter(true);
        rightToLeftSet.setFillEnabled(true);
        rightToLeftSet.addAnimation(rotate);
        rightToLeftSet.addAnimation(translate);

        /*
        LEFT TO RIGHT
         */
        rotate = new RotateAnimation(
                0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());

        translate = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -0.75f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f
        );
        translate.setInterpolator(new BounceInterpolator());

        leftToRightSet = new AnimationSet(true);
        leftToRightSet.setDuration(DURATION_MS);
        leftToRightSet.setFillAfter(true);
        leftToRightSet.setFillEnabled(true);
        leftToRightSet.addAnimation(rotate);
        leftToRightSet.addAnimation(translate);
    }

    private Anims(){}

    public static AnimationSet getRightToLeft() {
        return rightToLeftSet;
    }

    public static AnimationSet getLeftToRight() {
        return leftToRightSet;
    }

}
