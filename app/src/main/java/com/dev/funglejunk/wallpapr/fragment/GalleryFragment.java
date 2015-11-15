package com.dev.funglejunk.wallpapr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.dev.funglejunk.wallpapr.model.info.RawInfo;
import com.dev.funglejunk.wallpapr.rx.RxServiceCallback;
import com.dev.funglejunk.wallpapr.util.adapter.GalleryPagerAdapter;
import com.dev.funglejunk.wallpapr.util.ui.Anims;
import com.dev.funglejunk.wallpapr.util.ui.VerticalParallaxViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewPager.OnPageChangeListener {

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.pager)
    VerticalParallaxViewPager pager;

    @Bind(R.id.info_button)
    FloatingActionButton infoButton;

    private Intent serviceIntent;
    private Subscription serviceCallbackSubscription;

    private MainActivity activity;

    private GalleryPagerAdapter pagerAdapter;

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

        infoButton.startAnimation(Anims.getLeftToRight());

        swipeLayout.setOnRefreshListener(this);

        pagerAdapter = new GalleryPagerAdapter(getActivity());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(this);
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
        Toast.makeText(getActivity(), "not implemented", Toast.LENGTH_SHORT).show();
    }

    private void fetchUpdate() {
        pagerAdapter.clear();
        subscribeToBus();
        triggerServiceIntent();
    }

    private void triggerServiceIntent() {
        if (serviceIntent == null) {
            serviceIntent = new Intent(getActivity(), FlickrLoaderService.class);
            serviceIntent.setAction(Config.INTENT_NEW_REQUEST);
        }
        Timber.i("request intent service");
        getActivity().startService(serviceIntent);
    }

    private void subscribeToBus() {
        serviceCallbackSubscription = RxServiceCallback.observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RawInfo, RawInfo>() {
                    @Override
                    public RawInfo call(RawInfo rawInfo) {
                        if (rawInfo == null) {
                            throw OnErrorThrowable.from(new Exception("null result"));
                        }
                        return rawInfo;
                    }
                })
                .map(new Func1<RawInfo, RawInfo>() {
                    @Override
                    public RawInfo call(RawInfo rawInfo) {
                        Timber.d("adding url: %s", rawInfo.toFarmUrl());
                        pagerAdapter.add(rawInfo);
                        return rawInfo;
                    }
                })
                .buffer(Config.NR_OF_IMAGES)
                .subscribe(
                        new Action1<List<RawInfo>>() {
                            @Override
                            public void call(List<RawInfo> rawInfos) {
                                Timber.d("request done");
                                swipeLayout.setRefreshing(false);
                                unsubscribeFromBus();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Timber.w(throwable, "error in fragment to service subscription");
                                swipeLayout.setRefreshing(false);
                                unsubscribeFromBus();
                                pagerAdapter.notifyDataSetChanged();
                            }
                        }
                );
    }

    private void unsubscribeFromBus() {
        serviceCallbackSubscription.unsubscribe();
        serviceCallbackSubscription = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(final int position) {
        swipeLayout.setEnabled(position == 0);
        if (!infoButton.isShown()) {
            infoButton.show();
        }
        pagerAdapter.setImageForPage(position);
        activity.updateStar(pagerAdapter.getEntryAt(pager.getCurrentItem()).toFarmUrl());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            infoButton.hide();
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            infoButton.show();
        }
    }
}
