package opu.android.best.practice.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import opu.android.best.practice.ImageApi;
import opu.android.best.practice.model.JsonResponse;

/**
 * Created by Md.Fazla Rabbi OPu on 8/1/2018.
 */

public class ImageApiClient {


    private static ImageApiClient instance;
    private ImageApi imageApiService;


    private ImageApiClient() {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient().create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.IMG_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        imageApiService = retrofit.create(ImageApi.class);
    }

    public static ImageApiClient getInstance() {
        if (instance == null) {
            instance = new ImageApiClient();
        }
        return instance;
    }


    public Observable<JsonResponse> getImageList() {

        return imageApiService.getImageList();

    }

    public static void dispose() {
        instance = null;
    }
}
