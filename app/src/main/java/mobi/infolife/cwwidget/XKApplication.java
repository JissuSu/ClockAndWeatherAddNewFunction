package mobi.infolife.cwwidget;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import net.danlew.android.joda.JodaTimeAndroid;


/**
 * Created by longlong on 15-8-3.
 * ORM框架初始化
 */
public class XKApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ActiveAndroid
        ActiveAndroid.initialize(this);
        //初始化joda time
        JodaTimeAndroid.init(this);
    }
}