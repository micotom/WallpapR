package com.dev.funglejunk.wallpapr.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.funglejunk.wallpapr.Config;
import com.dev.funglejunk.wallpapr.MainActivity;
import com.dev.funglejunk.wallpapr.R;
import com.dev.funglejunk.wallpapr.flickr.FlickrLoaderService;
import com.dev.funglejunk.wallpapr.model.info.RawInfo;
import com.dev.funglejunk.wallpapr.rx.RxServiceCallback;
import com.dev.funglejunk.wallpapr.util.BitmapMemory;
import com.dev.funglejunk.wallpapr.util.GalleryAdapter;
import com.dev.funglejunk.wallpapr.util.GalleryPagerListener;
import com.dev.funglejunk.wallpapr.util.InfoMemory;
import com.dev.funglejunk.wallpapr.util.ui.VerticalParallaxViewPager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Bind(R.id.info_button)
    FloatingActionButton infoButton;

    private Intent serviceIntent;
    private Subscription serviceCallbackSubscription;

    private MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

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
        infoButton.show();
        swipeLayout.setOnRefreshListener(this);
        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity());
        pager.setAdapter(galleryAdapter);
        pager.setOnPageChangeListener(new GalleryPagerListener(activity, swipeLayout, infoButton));
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

    @SuppressWarnings("unused")
    @OnClick({R.id.info_button})
    public void onDetailButtonClick(View view){
        if (BitmapMemory.INSTANCE.pointer != -1) {
            activity.setFragment(MainActivity.FRAGMENT_DETAIL, true);
        }
    }

    private void fetchUpdate() {
        BitmapMemory.INSTANCE.reset();
        InfoMemory.INSTANCE.reset();
        subscribeToBus();
        triggerServiceIntent();
    }

    private void triggerServiceIntent() {
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
                .subscribe(new Action1<List<RawInfo>>() {
                    @Override
                    public void call(List<RawInfo> infos) {
                        if (infos != null) {
                            Observable.from(infos)
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .map(new Func1<RawInfo, String>() {
                                    @Override
                                    public String call(RawInfo info) {
                                        InfoMemory.INSTANCE.add(info);
                                        return info.toFarmUrl();
                                    }
                                })
                                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                                    @Override
                                    public Observable<? extends String> call(Throwable throwable) {
                                        Timber.w(throwable, "error retrieving farm url");
                                        return null;
                                    }
                                })
                                .filter(new Func1<String, Boolean>() {
                                    @Override
                                    public Boolean call(String s) {
                                        return s != null;
                                    }
                                })
                                .map(new Func1<String, Bitmap>() {
                                    @Override
                                    public Bitmap call(final String url) {
                                        return loadBitmapFromUrl(url);
                                    }
                                })
                                .subscribe(
                                        new Action1<Bitmap>() {
                                               @Override
                                               public void call(final Bitmap bitmap) {
                                                   addBitmapToStore(bitmap);
                                               }
                                            },
                                        new Action1<Throwable>() {
                                            @Override
                                            public void call(Throwable throwable) {
                                                Timber.w(throwable, "error receiving callback");
                                            }
                                        }
                                );
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

    @Nullable
    private Bitmap loadBitmapFromUrl(String url) {
        Timber.i("adding url item: %s", url);
        BitmapMemory.INSTANCE.addUrl(url);
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

    private void addBitmapToStore(final Bitmap bitmap) {
        if (bitmap != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BitmapMemory.INSTANCE.add(bitmap);
                    BitmapMemory.INSTANCE.pointer = 0;
                }
            });
        }
    }

    private void unsubscribeFromBus() {
        serviceCallbackSubscription.unsubscribe();
        serviceCallbackSubscription = null;
    }

}
