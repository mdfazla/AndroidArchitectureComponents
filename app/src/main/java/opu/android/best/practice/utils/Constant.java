package opu.android.best.practice.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;

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

    public static boolean saveImage(String name, Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/imgSrc");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = name + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            return false;
        else {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

}
