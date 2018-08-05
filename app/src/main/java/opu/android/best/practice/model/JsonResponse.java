package opu.android.best.practice.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Md.Fazla Rabbi OPu on 8/1/2018.
 */

public class JsonResponse {
    @SerializedName("images")
    private ArrayList<ImageInfo> imageList;

    public void setImageList(ArrayList<ImageInfo> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<ImageInfo> getImageList() {
        return imageList;
    }
}
