package sharemythoughts.android.com.recycleviewwithvariantgrid.presenter;

import java.util.ArrayList;

import sharemythoughts.android.com.recycleviewwithvariantgrid.model.AdapterModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.ImageInfo;

public interface ImageLoadingContract {
    public interface View {
        public void onLoadImages(ArrayList<AdapterModel> images);

        public void onError(String ex);
    }

    public interface Presenter {
        public void loadImages();

        public void dispose();
    }
}
