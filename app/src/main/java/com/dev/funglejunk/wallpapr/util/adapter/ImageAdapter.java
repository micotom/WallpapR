package com.dev.funglejunk.wallpapr.util.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dev.funglejunk.wallpapr.R;

import java.util.ArrayList;
import java.util.List;

public abstract class ImageAdapter<T> extends PagerAdapter {

    final LayoutInflater inflater;
    List<T> items;
    List<ImageView> views;

    public ImageAdapter(Context context) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = new ArrayList<>();
        views = new ArrayList<>();
    }

    @Override
    public final int getCount() {
        return items.size();
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        final View itemView = inflater.inflate(R.layout.gallery_item, container, false);
        final ImageView imgView = (ImageView) itemView.findViewById(R.id.imageView);
        views.add(position, imgView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public abstract void setImageForPage(final int position);

    public final void add(T item) {
        items.add(item);
        notifyDataSetChanged();
        if (items.size() == 1) {
            setImageForPage(0);
        }
    }

    public final T getEntryAt(int position) {
        return items.get(position);
    }

    public final void clear() {
        items.clear();
        notifyDataSetChanged();
    }

}
