package sharemythoughts.android.com.recycleviewwithvariantgrid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Md.Fazla Rabbi OPu on 8/1/2018.
 */

public class JsonResponse {
    @SerializedName("images")
    @Expose
    private List<ImageInfo> imageList;

    public void setImageList(List<ImageInfo> imageList) {
        this.imageList = imageList;
    }

    public List<ImageInfo> getImageList() {
        return imageList;
    }
}
