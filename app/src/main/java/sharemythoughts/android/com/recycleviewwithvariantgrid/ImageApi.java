package sharemythoughts.android.com.recycleviewwithvariantgrid;

import retrofit2.http.GET;
import rx.Observable;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.JsonResponse;

/**
 * Created by Md.Fazla Rabbi OPu on 8/1/2018.
 */

public interface ImageApi {
    @GET("BeautifulBangladesh/master/beautiful_bangladesh.json")
    Observable<JsonResponse> getImageList();
}
