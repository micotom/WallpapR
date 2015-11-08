package com.dev.funglejunk.wallpapr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dev.funglejunk.wallpapr.MainActivity;
import com.dev.funglejunk.wallpapr.R;
import com.dev.funglejunk.wallpapr.util.BitmapStore;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    @Bind(R.id.image)
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getDetailsButton().hide();
        imageView.setImageBitmap(BitmapStore.INSTANCE.getContent()
                .get(BitmapStore.INSTANCE.pointer));
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

}
