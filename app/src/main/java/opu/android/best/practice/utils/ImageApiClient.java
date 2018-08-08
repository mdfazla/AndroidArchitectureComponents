package opu.android.best.practice.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
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
    private Context context;
    private static HandlerThread handlerThread;


    private ImageApiClient(Context context) {
        this.context = context;
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient().create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.IMG_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
        imageApiService = retrofit.create(ImageApi.class);
    }

    public static ImageApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ImageApiClient(context);
        }
        return instance;
    }


    public Observable<JsonResponse> getImageList() {

        return imageApiService.getImageList();

    }

    @TargetApi(18)
    public OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new InternetStatusInterceptor(context) {
                    @Override
                    public void internetUnavailable(final NoInternetException ex) {
                        if (handlerThread == null || !handlerThread.isAlive()) {
                            handlerThread = new HandlerThread("Exception");
                            handlerThread.start();
                        }
                        Looper looper = handlerThread.getLooper();
                        final Handler handler = new Handler(looper);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                                //thread.quitSafely();
                                handler.removeCallbacksAndMessages(null);
                            }
                        });

                    }
                })
                .build();
        return client;
    }


    public static void dispose() {
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread.interrupt();
            handlerThread = null;
        }
        instance = null;

    }
}
