package com.dev.funglejunk.wallpapr;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;

import com.dev.funglejunk.wallpapr.fragment.DetailFragment;
import com.dev.funglejunk.wallpapr.fragment.GalleryFragment;
import com.dev.funglejunk.wallpapr.util.BitmapStore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import timber.log.Timber;

public class MainActivity extends FragmentActivity {

    @IntDef({FRAGMENT_NULL, FRAGMENT_GALLERY, FRAGMENT_DETAIL, FRAGMENT_SETTINGS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentId{}
    public final static int FRAGMENT_NULL = -1;
    public final static int FRAGMENT_GALLERY = 0;
    public final static int FRAGMENT_DETAIL = 1;
    public final static int FRAGMENT_SETTINGS = 2;

    @State
    @FragmentId int currentFragmentId = FRAGMENT_NULL;

    @Bind(R.id.back_arrow)
    View backArrow;

    @Bind(R.id.star)
    View star;

    @Bind(R.id.settings)
    View settings;

    @Bind(R.id.details_button)
    FloatingActionButton detailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentFragmentId == FRAGMENT_NULL) {
            currentFragmentId = FRAGMENT_GALLERY;
        }
        Timber.d("current fragment id: %d", currentFragmentId);
        BitmapStore.INSTANCE.clearListeners();
        setFragment(currentFragmentId, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    public FloatingActionButton getDetailsButton() {
        return detailsButton;
    }

    public void setFragment(@FragmentId final int id, boolean forth) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getFragment(id, getTag(id), manager);
        setViewAnimations(id);
        setViewListeners(id);
        manager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setCustomAnimations(forth ? R.anim.slide_in_left : R.anim.slide_out_left,
                    forth ? R.anim.slide_out_left : R.anim.slide_in_left)
            .addToBackStack(getTag(id))
            .commit();
    }

    private void setViewAnimations(@FragmentId final int id) {
        switch (id) {
            case FRAGMENT_GALLERY:
                AnimationSet animSet = new AnimationSet(true);
                RotateAnimation rAnim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
                        0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
                rAnim.setDuration(1000);
                AlphaAnimation aAnim = new AlphaAnimation(1.0f, 0.0f);
                aAnim.setDuration(1500);
                animSet.addAnimation(rAnim);
                animSet.addAnimation(aAnim);
                animSet.setFillAfter(true);
                backArrow.startAnimation(animSet);
                break;
            case FRAGMENT_DETAIL:
                backArrow.setRotation(-45);
                animSet = new AnimationSet(true);
                aAnim = new AlphaAnimation(0.0f, 1.0f);
                aAnim.setDuration(1500);
                animSet.addAnimation(aAnim);
                animSet.setFillAfter(true);
                backArrow.startAnimation(animSet);
                break;
            case FRAGMENT_SETTINGS:
            default:
                throw new UnsupportedOperationException("Fragment not implemented: " + id);
        }
    }

    private void setViewListeners(@FragmentId final int id) {
        switch (id) {
            case FRAGMENT_GALLERY:
                backArrow.setOnClickListener(null);
                break;
            case FRAGMENT_DETAIL:
                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFragment(FRAGMENT_GALLERY, false);
                    }
                });
                break;
            case FRAGMENT_SETTINGS:
            default:
                throw new UnsupportedOperationException("Fragment not implemented: " + id);
        }
    }

    private String getTag(@FragmentId int fragmentId) {
        final String tag;
        switch (fragmentId) {
            case FRAGMENT_GALLERY:
                tag = GalleryFragment.class.getName();
                break;
            case FRAGMENT_DETAIL:
                tag = DetailFragment.class.getName();
                break;
            case FRAGMENT_SETTINGS:
            default:
                throw new UnsupportedOperationException("Fragment not implemented: " + fragmentId);
        }
        return tag;
    }

    @NonNull
    private Fragment getFragment(@FragmentId int fragmentId, String tag, FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            switch (fragmentId) {
                case FRAGMENT_GALLERY:
                    fragment = new GalleryFragment();
                    break;
                case FRAGMENT_DETAIL:
                    fragment = new DetailFragment();
                    break;
                case FRAGMENT_SETTINGS:
                default:
                    throw new UnsupportedOperationException("Fragment not implemented: " + fragmentId);
            }
        }
        return fragment;
    }

}
