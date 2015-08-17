package mobi.infolife.limitNumAndAir.XKUtils;

/**
 * Created by longlong on 15-8-6.
 */
public class MyHttpException extends Exception {

    private int mErrorCode;

    public MyHttpException(int responseCode) {
        responseCode = mErrorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
