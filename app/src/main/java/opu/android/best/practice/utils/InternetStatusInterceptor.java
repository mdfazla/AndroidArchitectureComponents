package opu.android.best.practice.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Md.Fazla Rabbi OPu on 8/8/2018.
 */

public abstract class InternetStatusInterceptor implements Interceptor {
    private Context mContext;

    public InternetStatusInterceptor(Context context) {
        this.mContext = context;
    }

    public abstract void internetUnavailable(NoInternetException ex);

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.isOnline(mContext)) {
            //throw new NoInternetException();
            internetUnavailable(new NoInternetException());
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

}
