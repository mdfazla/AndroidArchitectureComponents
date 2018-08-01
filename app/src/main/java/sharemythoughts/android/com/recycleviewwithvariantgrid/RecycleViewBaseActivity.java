package sharemythoughts.android.com.recycleviewwithvariantgrid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.ImageInfo;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.JsonResponse;
import sharemythoughts.android.com.recycleviewwithvariantgrid.utils.ImageApiClient;

public class RecycleViewBaseActivity extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_base);

        getImages();
    }


    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void getImages() {
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

                    }

                    @Override
                    public void onNext(JsonResponse response) {
                        if (response != null) {
                            for (int i = 0; i < response.getImageList().size(); i++) {
                                Log.d("ImgUrl ", response.getImageList().get(i).getImgUrl());
                            }
                        }
                    }
                });
    }
}
