package com.dev.funglejunk.wallpapr;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.dev.funglejunk.wallpapr.fragment.DetailFragment;
import com.dev.funglejunk.wallpapr.fragment.FavouritesFragment;
import com.dev.funglejunk.wallpapr.fragment.GalleryFragment;
import com.dev.funglejunk.wallpapr.storage.FavEntry;
import com.dev.funglejunk.wallpapr.storage.FavStore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import rx.functions.Action1;
import timber.log.Timber;

public class MainActivity extends FragmentActivity {

    @IntDef({FRAGMENT_NULL, FRAGMENT_GALLERY, FRAGMENT_DETAIL, FRAGMENT_SETTINGS, FRAGMENT_FAVOURITES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentId{}
    public final static int FRAGMENT_NULL = -1;
    public final static int FRAGMENT_GALLERY = 0;
    public final static int FRAGMENT_DETAIL = 1;
    public final static int FRAGMENT_SETTINGS = 2;
    public final static int FRAGMENT_FAVOURITES = 3;

    @State
    @FragmentId int currentFragmentId = FRAGMENT_NULL;

    @Bind(R.id.star)
    ImageView star;

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

    @SuppressWarnings("unused")
    @OnClick({R.id.settings})
    public void onSettingsClicked() {
        setFragment(FRAGMENT_FAVOURITES, true);
    }

    public void setFragment(@FragmentId final int id, boolean forth) {
        FragmentManager manager = getSupportFragmentManager();
        final String tag = getTag(id);
        Fragment fragment = getFragment(id, tag, manager);
        manager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setCustomAnimations(forth ? R.anim.slide_in_left : R.anim.slide_out_left,
                    forth ? R.anim.slide_out_left : R.anim.slide_in_left)
            .addToBackStack(tag)
            .commit();
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
            case FRAGMENT_FAVOURITES:
                tag = FavouritesFragment.class.getName();
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
                case FRAGMENT_FAVOURITES:
                    fragment = new FavouritesFragment();
                    break;
                case FRAGMENT_SETTINGS:
                default:
                    throw new UnsupportedOperationException("Fragment not implemented: " + fragmentId);
            }
        }
        return fragment;
    }

    public void updateStar(final String currentUrl) {
        final FavEntry favEntry = new FavEntry(currentUrl);
        FavStore.INSTANCE.has(favEntry).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean has) {
                if (has) {
                    onFavouriteImagePaged();
                } else {
                    onNonFavouriteImagePaged(currentUrl);
                }
            }
        });
    }

    private void onNonFavouriteImagePaged(final String currentUrl) {
        star.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_star_0));
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FavEntry favEntry = new FavEntry(currentUrl);
                FavStore.INSTANCE.has(favEntry).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean has) {
                        if (has) {return;}
                        FavStore.INSTANCE.persist(favEntry).subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean success) {
                                if (success) {
                                    star.setImageDrawable(getResources()
                                            .getDrawable(R.drawable.ic_action_star_10));
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void onFavouriteImagePaged() {
        star.setOnClickListener(null);
        star.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_star_10));
    }

}
