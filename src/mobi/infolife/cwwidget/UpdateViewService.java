package mobi.infolife.cwwidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.analytics.tracking.android.Logger;

import mobi.infolife.utils.BatteryUtils;
import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.ViewUtils;
import mobi.infolife.widget.framework.MyAppWidgetManager;
import mobi.infolife.widget.framework.MyAppWidgetManager.CallBack;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateViewService extends Service {

	private static final String TAG = "UpdateViewService";
	private MyAppWidgetManager mMyAppWidgetManager = null;
	private CWWidgetFactory mCWWidgetFactory = null;
	private AppWidgetManager mAppWidgetManager = null ;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		CommonUtils.l("View Service onCreate");
		super.onCreate();
		mCWWidgetFactory = CWWidgetFactory.getInstance();
		mMyAppWidgetManager = MyAppWidgetManager
				.getInstance(this.getApplicationContext(),
						RemoteServiceCallBack.getInstance(this
								.getApplicationContext()));
		mAppWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());  

	}

	@Override
	public void onDestroy() {
		CommonUtils.l("View Service onDestroy");
		super.onDestroy();
		unregisterReceiver(BatteryUtils.batteryReceiver);
		unregisterReceiver(BatteryUtils.powerConnectedReceiver);
		unregisterReceiver(BatteryUtils.powerDisconnectedReceiver);
		unregisterReceiver(BatteryUtils.screenOrientationChangedReceiver);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// Log.i(TAG, "start service Time update");
		CommonUtils.l("View Service start");
		registerReceiver(BatteryUtils.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(BatteryUtils.powerConnectedReceiver,new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(BatteryUtils.powerDisconnectedReceiver,new IntentFilter(Intent.ACTION_POWER_DISCONNECTED ));
        registerReceiver(BatteryUtils.screenOrientationChangedReceiver, new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));
		boolean launcherIsResume = Preferences.getLauncherOnStat(this);

		SettingActivity.setScreenHeight(this);

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		List<CWWidget> cwWidgetList = mCWWidgetFactory.getCWWidgets(this,
				mMyAppWidgetManager);
 		
		if (launcherIsResume) {
			CommonUtils.l("Screen Is On, start update widget view" );
			try {
				for (CWWidget cwWidget : cwWidgetList) {
					cwWidget.updateView();
				}
			} catch (CWRemoteException e) {
				stopSelf();
				e.printStackTrace();
			}
		}
		Date now = new Date();
		int second = 61 - now.getSeconds();
		long updateMilis = second * 1000;
		restartServiceInTime(updateMilis, intent);
	}

	public void restartServiceInTime(long period, Intent intent) {
		Intent serviceIntent = intent;
		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				serviceIntent, 0);
		// // Schedule alarm, and force the device awake for this update
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ period, pendingIntent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
}
