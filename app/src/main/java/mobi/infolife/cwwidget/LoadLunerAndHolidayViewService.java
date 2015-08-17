package mobi.infolife.cwwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;

import mobi.infolife.utils.CommonUtils;

/**
 * Created by a23qws on 2015/8/14.
 */
public class LoadLunerAndHolidayViewService extends Service {
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
        fullWidget.updateLunerAndHolidayView();
        long updateMilis = 12 * 60 * 60 * 1000;
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
