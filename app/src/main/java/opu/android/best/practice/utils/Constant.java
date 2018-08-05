package opu.android.best.practice.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Md.Fazla Rabbi OPu on 8/1/2018.
 */

public class Constant {
    public static final String IMG_BASE_URL = "https://raw.githubusercontent.com/mdfazla/";


    public static int[] calculateDeviceResolution(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] res = new int[2];
        res[0] = displayMetrics.heightPixels;
        res[1] = displayMetrics.widthPixels;

        return res;
    }

    public static void setImage(Context context, String url, ImageView vIew) {

        Glide.with(context)
                .load(url)
                .placeholder(android.R.color.darker_gray)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(vIew);
    }

    public static void loadGif(Context context, String url, ImageView vIew) {


        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(android.R.color.darker_gray)
                .crossFade()
                .into(vIew);
    }

}
