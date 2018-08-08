package opu.android.best.practice.model;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Created by Md.Fazla Rabbi OPu on 8/8/2018.
 */

public class ImagesViewModel extends ViewModel {

    private ArrayList<AdapterModel> imageList;
    private int index;

    public void setImageList(ArrayList<AdapterModel> list) {
        this.imageList = list;
    }

    public ArrayList<AdapterModel> getImageList() {
        return imageList;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
