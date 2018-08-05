package opu.android.best.practice.presenter;

import java.util.ArrayList;

import opu.android.best.practice.model.AdapterModel;

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
