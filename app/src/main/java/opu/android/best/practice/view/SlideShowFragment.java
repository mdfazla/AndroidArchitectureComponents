package opu.android.best.practice.view;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import opu.android.best.practice.R;
import opu.android.best.practice.model.ImageInfo;
import opu.android.best.practice.utils.Constant;
import opu.android.best.practice.utils.EfficientTimer;
import opu.android.best.practice.utils.TimeChangeListener;

/**
 * Created by Md.Fazla Rabbi OPu on 8/6/2018.
 */

public class SlideShowFragment extends DialogFragment implements TimeChangeListener {
    private ArrayList<ImageInfo> imageInfos;
    private int realWidth, realHeight;
    private ImageView imageView;
    private int translateYto = 0;
    private int currentImagePosition = 0;
    private static AppCompatActivity activity;


    public static DialogFragment showDialog(ArrayList<ImageInfo> imageInfos, AppCompatActivity context) {
        activity = context;
        Bundle bundle = new Bundle();
        bundle.putSerializable(SlideShowFragment.class.getName(), imageInfos);
        DialogFragment fragment = new SlideShowFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.slide_show_view, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        int[] resolution = Constant.calculateDeviceResolution(activity);
        realWidth = resolution[1];
        realHeight = resolution[0];
        getBundleData();

        initView(parentView);
        return parentView;
    }

    private void initView(View root) {
        imageView = root.findViewById(R.id.slide_show_img_view);
        loadProperImage(imageView, imageInfos.get(currentImagePosition).getImgUrl());
        EfficientTimer.start(this);


    }


    private void loadProperImage(final ImageView imageView, String url) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                // Do something with bitmap here.
                // int imgWidth = (int) (bitmap.getHeight() * scalingFactor);
                currentImagePosition++;
                int bitmapHeight = bitmap.getHeight();
                int bitmapWidth = bitmap.getWidth();
                float scaleY = (realHeight - translateYto) / (float) bitmapHeight;
                float scaleX = realWidth / (float) bitmapWidth;
                int imgWidth = bitmapWidth;
                int imgHeight = bitmapHeight;
                if (scaleX < 1) {
                    imgWidth = (int) (bitmapWidth * scaleX);
                }

                if (scaleY < 1) {
                    imgHeight = (int) (bitmapHeight * scaleY);
                }
                imageView.getLayoutParams().width = imgWidth;
                imageView.getLayoutParams().height = imgHeight;
                imageView.setImageBitmap(bitmap);
            }
        };
        Glide.with(getActivity()).load(url).asBitmap().format(DecodeFormat.PREFER_RGB_565).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            imageInfos = (ArrayList<ImageInfo>) getArguments().getSerializable(SlideShowFragment.class.getName());
        }

    }


    @Override
    public void onTick() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentImagePosition >= imageInfos.size()) {
                    dismiss();
                } else {
                    loadProperImage(imageView, imageInfos.get(currentImagePosition).getImgUrl());
                }
            }
        });
    }

    @Override
    public long getInterval() {
        return 3000;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EfficientTimer.removeObserver(this);
        activity = null;
    }
}


