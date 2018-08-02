package sharemythoughts.android.com.recycleviewwithvariantgrid.presenter;

import android.util.Log;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.AdapterModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.DataModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.HeaderModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.ImageInfo;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.JsonResponse;
import sharemythoughts.android.com.recycleviewwithvariantgrid.utils.ImageApiClient;

public class Presenter implements ImageLoadingContract.Presenter {
    private ImageLoadingContract.View viewListener;
    private Subscription subscription;

    public Presenter(ImageLoadingContract.View listener) {
        this.viewListener = listener;
    }

    private void initImageLoader() {
        subscription = ImageApiClient.getInstance()
                .getImageList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        viewListener.onError(e.getMessage());

                    }

                    @Override
                    public void onNext(JsonResponse response) {
                        Log.d("onNext ","");
                        if (response != null && !response.getImageList().isEmpty()) {

                            ArrayList<AdapterModel> adapterModels = new ArrayList<>();
                            for (int i = 0; i < response.getImageList().size(); i++) {
                                ImageInfo imageInfo = response.getImageList().get(i);
                                if (imageInfo.getImgUrl().endsWith(".gif")) {
                                    HeaderModel headerModel = new HeaderModel();
                                    headerModel.setData(imageInfo);
                                    adapterModels.add(0,headerModel);
                                } else {
                                    DataModel dataModel = new DataModel();
                                    dataModel.setId(imageInfo.getId());
                                    dataModel.setImgName(imageInfo.getImgName());
                                    dataModel.setImgUrl(imageInfo.getImgUrl());
                                    adapterModels.add(dataModel);

                                }
                            }
                            viewListener.onLoadImages(adapterModels);
                        }
                    }
                });
    }

    @Override
    public void loadImages() {
        initImageLoader();
    }

    @Override
    public void dispose() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        ImageApiClient.dispose();

    }
}
