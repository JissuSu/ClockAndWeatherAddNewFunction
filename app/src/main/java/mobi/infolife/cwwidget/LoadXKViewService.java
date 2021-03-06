package mobi.infolife.cwwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;

import mobi.infolife.limitNumAndAir.XKMain.XKDownLoadUtil;
import mobi.infolife.limitNumAndAir.XKUtils.DataStore;
import mobi.infolife.utils.CommonUtils;

/**
 * Created by a23qws on 2015/8/14.
 */
public class LoadXKViewService extends Service {
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
        if (!DataStore.getData(getApplicationContext(), DataStore.CITY_NAME)
                .equals("null")) {
            startService(new Intent(this, XKDownLoadUtil.class));
        }
        fullWidget = new FullWidget(this);
        fullWidget.updateXKView();
        long updateMilis = 30 * 60 * 1000;
        restartServiceInTime(updateMilis, intent);
    }

    public void restartServiceInTime(long period, Intent intent) {
        if(intent != null) {
            if (CommonUtils.isIntentAvailable(this, intent.getAction())) {
                Intent serviceIntent = intent;
                PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                        serviceIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + period, pendingIntent);
            }
        }
    }
}
