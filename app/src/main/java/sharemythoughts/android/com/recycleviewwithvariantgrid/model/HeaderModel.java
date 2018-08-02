package sharemythoughts.android.com.recycleviewwithvariantgrid.model;

public class HeaderModel implements AdapterModel {

    private ImageInfo data;

    public void setData(ImageInfo data) {
        this.data = data;
    }

    public ImageInfo getData() {
        return data;
    }

    @Override
    public int getType() {
        return AdapterModel.HEADER;
    }
}
