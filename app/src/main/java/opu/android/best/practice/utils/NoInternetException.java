package opu.android.best.practice.utils;

import java.io.IOException;

/**
 * Created by Md.Fazla Rabbi OPu on 8/8/2018.
 */

public class NoInternetException extends IOException {

    @Override
    public String getMessage() {
        return "Internet unavailable";
    }
}
