package opu.android.best.practice.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import opu.android.best.practice.R;
import opu.android.best.practice.model.ImageInfo;
import opu.android.best.practice.utils.Constant;

/**
 * Created by Md.Fazla Rabbi OPu on 8/5/2018.
 */

public class FullImageFragment extends DialogFragment implements View.OnClickListener {
    private ImageInfo imageInfo;
    private int realWidth, realHeight;
    private ImageView imageView;
    private int translateYto = 0;
    private FrameLayout settings_holder;


    public static DialogFragment showDialog(ImageInfo imageInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FullImageFragment.class.getName(), imageInfo);
        DialogFragment fragment = new FullImageFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.full_image_fragment, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        int[] resolution = Constant.calculateDeviceResolution(getActivity());
        realWidth = resolution[1];
        realHeight = resolution[0];
        getBundleData();

        initView(parentView);
        return parentView;
    }

    private void initView(View root) {
        ImageView cross_dialog = (ImageView) root.findViewById(R.id.cross_dialog);
        cross_dialog.setOnClickListener(this);
        imageView = root.findViewById(R.id.image);
        settings_holder = root.findViewById(R.id.settings_holder);
        // Constant.setImage(getContext(), imageInfo.getImgUrl(), imageView);
        setImageViewInProperPosition();
        loadProperImage(imageView, imageInfo.getImgUrl());
        ImageView save_img = (ImageView) root.findViewById(R.id.save);
        save_img.setOnClickListener(this);


        float translateTo = realHeight / 2 - realWidth / 2;

        //float translateTo = 50;
        // transitionAnim(imageView, translateYto);

        backPressListener();

    }

    private void setImageViewInProperPosition() {
        settings_holder.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                        translateYto=settings_holder.getHeight();
                        params.bottomMargin = translateYto;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            settings_holder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            settings_holder.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    private void backPressListener() {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // dismissDialogWithAnim();
                    dismiss();
                    return true;
                } else
                    return false;
            }
        });
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

    private void dismissDialogWithAnim() {
        transitionAnim(imageView, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 400);
    }

    private void transitionAnim(View view, float transitionTo) {
        view.animate().translationY(view.getTop());
    }

    private void getBundleData() {
        if (getArguments() != null) {
            imageInfo = (ImageInfo) getArguments().getSerializable(FullImageFragment.class.getName());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cross_dialog:
                //dismissDialogWithAnim();
                dismiss();

                break;

            case R.id.save:
                showSaveConfirmDialog();
                break;
        }

    }

    private void showSaveConfirmDialog() {

    }
}

