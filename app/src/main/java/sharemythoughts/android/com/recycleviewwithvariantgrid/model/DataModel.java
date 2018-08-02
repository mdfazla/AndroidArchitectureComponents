package sharemythoughts.android.com.recycleviewwithvariantgrid.model;

public class DataModel extends ImageInfo implements AdapterModel {

    @Override
    public int getType() {
        return AdapterModel.ITEM;
    }
}
