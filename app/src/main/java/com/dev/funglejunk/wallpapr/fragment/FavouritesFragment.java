package com.dev.funglejunk.wallpapr.fragment;

import android.animation.LayoutTransition;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.funglejunk.wallpapr.R;
import com.dev.funglejunk.wallpapr.storage.FavEntry;
import com.dev.funglejunk.wallpapr.storage.FavStore;
import com.dev.funglejunk.wallpapr.util.adapter.FavouritesPagerAdapter;
import com.dev.funglejunk.wallpapr.util.memory.FavouritesMemory;
import com.dev.funglejunk.wallpapr.util.ui.Anims;
import com.dev.funglejunk.wallpapr.util.ui.VerticalParallaxViewPager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;
import timber.log.Timber;

public class FavouritesFragment extends Fragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.root)
    ViewGroup layoutRoot;

    @Bind(R.id.wallpaper_button)
    FloatingActionButton wallpaperButton;

    @Bind(R.id.pager)
    VerticalParallaxViewPager pager;

    FavouritesPagerAdapter favouritesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favourites_fragment, container, false);
        ButterKnife.bind(this, layout);
        LayoutTransition transition = layoutRoot.getLayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        wallpaperButton.startAnimation(Anims.getRightToLeft());

        if (pager.getAdapter() == null) {
            favouritesAdapter = new FavouritesPagerAdapter(getActivity());
            pager.setAdapter(favouritesAdapter);
        }
        pager.setOnPageChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        FavStore.INSTANCE.getAll()
                .delay(1, TimeUnit.SECONDS)
                .subscribe(new Action1<FavEntry>() {
                    @Override
                    public void call(final FavEntry entry) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                favouritesAdapter.add(entry);
                            }
                        });
                    }
                });
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.wallpaper_button})
    public void onWallpaperClicked(View view) {
        Timber.d("wallpaper button clicked");
        final WallpaperManager wpm = WallpaperManager.getInstance(getContext());
        FavouritesMemory.INSTANCE.request(favouritesAdapter.getEntryAt(pager.getCurrentItem()).url)
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        try {
                            wpm.setBitmap(bitmap);
                        } catch (IOException e) {
                            Timber.w(e, "cannot set bitmap as wallpaper");
                        }
                    }
                });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        favouritesAdapter.setImageForPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

}
