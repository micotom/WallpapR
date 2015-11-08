package com.dev.funglejunk.wallpapr.util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public enum BitmapStore {

    INSTANCE;

    private List<Bitmap> bitmaps;
    private List<Listener> listeners;

    public int pointer = -1;

    BitmapStore() {
        bitmaps = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public List<Bitmap> getContent() {
        return bitmaps;
    }

    public void add(Bitmap bitmap){
        bitmaps.add(bitmap);
        for (Listener listener : listeners) {
            listener.onBitmapAdded();
        }
    }

    public void reset() {
        bitmaps.clear();
        pointer = -1;
        for (Listener listener : listeners) {
            listener.onStoreCleared();
        }
    }

    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        Timber.d("listener count: %d", listeners.size());
    }

    public void clearListeners() {
        listeners.clear();
    }

    public interface Listener {
        void onBitmapAdded();
        void onStoreCleared();
    }

}
