package opu.android.best.practice;

import retrofit2.http.GET;
import rx.Observable;
import opu.android.best.practice.model.JsonResponse;

/**
 * Created by Md.Fazla Rabbi OPu on 8/1/2018.
 */

public interface ImageApi {
    @GET("BeautifulBangladesh/master/beautiful_bangladesh.json")
    Observable<JsonResponse> getImageList();
}
