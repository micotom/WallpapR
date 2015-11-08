package com.dev.funglejunk.wallpapr.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.funglejunk.wallpapr.R;

public class GalleryAdapter extends PagerAdapter implements BitmapStore.Listener {

    LayoutInflater inflater;

    public GalleryAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BitmapStore.INSTANCE.addListener(this);
    }

    @Override
    public int getCount() {
        return BitmapStore.INSTANCE.getContent().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.gallery_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(BitmapStore.INSTANCE.getContent().get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public void onBitmapAdded() {
        notifyDataSetChanged();
    }

    @Override
    public void onStoreCleared() {
        notifyDataSetChanged();
    }

}
