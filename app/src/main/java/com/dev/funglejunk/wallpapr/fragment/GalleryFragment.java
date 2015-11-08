package com.dev.funglejunk.wallpapr.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.funglejunk.wallpapr.Config;
import com.dev.funglejunk.wallpapr.MainActivity;
import com.dev.funglejunk.wallpapr.R;
import com.dev.funglejunk.wallpapr.flickr.FlickrLoaderService;
import com.dev.funglejunk.wallpapr.rx.RxServiceCallback;
import com.dev.funglejunk.wallpapr.util.BitmapStore;
import com.dev.funglejunk.wallpapr.util.GalleryAdapter;
import com.dev.funglejunk.wallpapr.util.VerticalParallaxViewPager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.pager)
    VerticalParallaxViewPager pager;

    private Intent serviceIntent;
    private Subscription serviceCallbackSubscription;

    private MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.gallery_fragment, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (MainActivity)getActivity();
        activity.getDetailsButton().show();
        swipeLayout.setOnRefreshListener(this);
        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity());
        pager.setAdapter(galleryAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                swipeLayout.setEnabled(position == 0);
                BitmapStore.INSTANCE.pointer = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    activity.getDetailsButton().hide();
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    activity.getDetailsButton().show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        if (serviceCallbackSubscription != null) {
            serviceCallbackSubscription.unsubscribe();
        }
        pager.setAdapter(null);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        fetchUpdate();
    }

    private void fetchUpdate() {
        activity.getDetailsButton().setOnClickListener(null);
        BitmapStore.INSTANCE.reset();
        subscribeToBus();
        if (serviceIntent == null) {
            serviceIntent = new Intent(getActivity(), FlickrLoaderService.class);
        }
        getActivity().startService(serviceIntent);
    }

    private void subscribeToBus() {
        serviceCallbackSubscription = RxServiceCallback.observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(Config.NR_OF_IMAGES)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> urls) {
                        if (urls != null) {
                            Observable.from(urls)
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<String, Bitmap>() {
                                    @Override
                                    public Bitmap call(String url) {
                                        Timber.i("adding url item: %s", url);
                                        Bitmap bmp = null;
                                        try {
                                            bmp = Picasso.with(getActivity())
                                                    .load(url)
                                                    .get();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        return bmp;
                                    }
                                })
                                .subscribe(new Action1<Bitmap>() {
                                    @Override
                                    public void call(final Bitmap bitmap) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                BitmapStore.INSTANCE.add(bitmap);
                                                BitmapStore.INSTANCE.pointer = 0;
                                            }
                                        });
                                    }
                                });
                            activity.getDetailsButton().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int bmpPos = BitmapStore.INSTANCE.pointer;
                                    if (bmpPos != -1) {
                                        activity.setFragment(MainActivity.FRAGMENT_DETAIL, true);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getActivity(),
                                    "No web connection :(",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                        swipeLayout.setRefreshing(false);
                        unsubscribeFromBus();
                    }
                });
    }

    private void unsubscribeFromBus() {
        serviceCallbackSubscription.unsubscribe();
        serviceCallbackSubscription = null;
    }

}
