package opu.android.best.practice.presenter;

import java.util.ArrayList;
import java.util.List;

import opu.android.best.practice.model.TitleModel;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import opu.android.best.practice.model.AdapterModel;
import opu.android.best.practice.model.DataModel;
import opu.android.best.practice.model.HeaderModel;
import opu.android.best.practice.model.ImageInfo;
import opu.android.best.practice.model.JsonResponse;
import opu.android.best.practice.utils.ImageApiClient;

public class Presenter implements ImageLoadingContract.Presenter {
    private ImageLoadingContract.View viewListener;
    private Subscription subscription;

    public Presenter(ImageLoadingContract.View listener) {
        this.viewListener = listener;
    }

    private void initImageLoader() {
        subscription = ImageApiClient.getInstance()
                .getImageList()
                .concatMap(new Func1<JsonResponse, Observable<List<ImageInfo>>>() {
                    @Override
                    public Observable<List<ImageInfo>> call(JsonResponse jsonResponse) {
                        Observable<List<ImageInfo>> list = Observable.from(jsonResponse.getImageList()).toList();
                        return list;
                    }
                })
                .map(new Func1<List<ImageInfo>, List<AdapterModel>>() {
                    @Override
                    public List<AdapterModel> call(List<ImageInfo> imageInfos) {
                        List<AdapterModel> adapterModels = new ArrayList<>();
                        for (int i = 0; i < imageInfos.size(); i++) {
                            ImageInfo imageInfo = imageInfos.get(i);
                            if (imageInfo.getImgUrl().endsWith(".gif")) {
                                HeaderModel headerModel = new HeaderModel();
                                headerModel.setData(imageInfo);
                                adapterModels.add(0, headerModel);
                                adapterModels.add(1, new TitleModel());
                            } else {
                                DataModel dataModel = new DataModel();
                                dataModel.setId(imageInfo.getId());
                                dataModel.setImgName(imageInfo.getImgName());
                                dataModel.setImgUrl(imageInfo.getImgUrl());
                                adapterModels.add(dataModel);

                            }
                        }
                        return adapterModels;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AdapterModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AdapterModel> adapterModels) {
                        viewListener.onLoadImages((ArrayList<AdapterModel>) adapterModels);
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
