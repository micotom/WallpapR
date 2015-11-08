package com.dev.funglejunk.wallpapr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dev.funglejunk.wallpapr.MainActivity;
import com.dev.funglejunk.wallpapr.R;
import com.dev.funglejunk.wallpapr.util.ui.BlurTransformation;
import com.dev.funglejunk.wallpapr.util.ui.ScaleDownTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GalleryAdapter extends PagerAdapter implements BitmapMemory.Listener {

    private LayoutInflater inflater;
    private MainActivity activity;

    private BlurTransformation blurTransformation;
    private ScaleDownTransformation scaleDownTransformation;

    public GalleryAdapter(Context context) {
        activity = (MainActivity)context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BitmapMemory.INSTANCE.addListener(this);
        final RenderScript renderScript = RenderScript.create(activity);
        blurTransformation = new BlurTransformation(renderScript, 8f);
        scaleDownTransformation = new ScaleDownTransformation(activity, 0.25f);
    }

    @Override
    public int getCount() {
        return BitmapMemory.INSTANCE.getContent().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View itemView = inflater.inflate(R.layout.gallery_item, container, false);

        final Bitmap bitmap = BitmapMemory.INSTANCE.getContent().get(position);
        ImageView mainView = (ImageView) itemView.findViewById(R.id.imageView);
        mainView.setImageBitmap(bitmap);
        container.addView(itemView);

        Observable.create(new Observable.OnSubscribe<RequestCreator>() {
            @Override
            public void call(Subscriber<? super RequestCreator> subscriber) {
                subscriber.onNext(Picasso.with(activity)
                        .load(getImageUri(activity, bitmap))
                        .transform(scaleDownTransformation)
                        .transform(blurTransformation)
                );
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<RequestCreator>() {
            @Override
            public void call(RequestCreator requestCreator) {
                ImageView blurView = (ImageView) itemView.findViewById(R.id.blur);
                requestCreator.into(blurView);
            }
        });


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public void onBitmapAdded() {
        notifyDataSetChanged();
    }

    @Override
    public void onStoreCleared() {
        notifyDataSetChanged();
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "Temp Blur", null);
        return Uri.parse(path);
    }

}
