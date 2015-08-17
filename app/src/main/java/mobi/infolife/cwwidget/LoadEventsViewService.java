package mobi.infolife.cwwidget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by a23qws on 2015/8/14.
 */
public class LoadEventsViewService  extends Service {
    FullWidget fullWidget;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        fullWidget = new FullWidget(this);
        fullWidget.updateEventsView();
    }
}
