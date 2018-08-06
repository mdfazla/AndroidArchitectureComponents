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
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import opu.android.best.practice.R;
import opu.android.best.practice.model.ImageInfo;
import opu.android.best.practice.utils.Constant;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Md.Fazla Rabbi OPu on 8/5/2018.
 */

public class FullImageFragment extends DialogFragment implements View.OnClickListener {
    private ImageInfo imageInfo;
    private int realWidth, realHeight;
    private ImageView imageView;
    private int translateYto = 0;
    private FrameLayout settings_holder;
    private Bitmap bitmapToSaved;
    private ProgressBar progressbar;
    private Subscription observable;


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
        progressbar = root.findViewById(R.id.progressbar);
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
                        translateYto = settings_holder.getHeight();
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
                bitmapToSaved = bitmap;
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
                if (Constant.isStoragePermissionGranted(getActivity())) {
                    showSaveConfirmDialog();
                }
                break;
        }

    }

    private void showSaveConfirmDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle(getActivity().getResources().getString(R.string.download))
                .setMessage(getActivity().getResources().getString(R.string.download_msg))
                .setPositiveButton(getActivity().getResources().getString(R.string.download), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressbar.setVisibility(View.VISIBLE);
                        observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
                            @Override
                            public void call(Subscriber<? super Bitmap> subscriber) {
                                subscriber.onNext(bitmapToSaved);
                                subscriber.onCompleted();

                            }
                        })
                                .map(new Func1<Bitmap, String>() {
                                    @Override
                                    public String call(Bitmap bitmap) {
                                        boolean sucs = Constant.saveImage(imageInfo.getImgName(), bitmap);
                                        String msg = "";
                                        if (sucs) {
                                            msg = getActivity().getResources().getString(R.string.save_sucs);
                                        } else {
                                            msg = getActivity().getResources().getString(R.string.already_save);
                                        }
                                        return msg;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String msg) {
                                        progressbar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        ///Another way to subscribe///
                                /*.subscribe(new Observer<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(String msg) {
                                        progressbar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .show();

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (observable != null) {
                    observable.unsubscribe();
                    observable = null;
                }
            }
        });
    }
}

