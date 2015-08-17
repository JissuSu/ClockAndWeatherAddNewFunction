package mobi.infolife.cwwidget;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mobi.infolife.limitNumAndAir.XKMain.XKDownLoadUtil;
import mobi.infolife.limitNumAndAir.XKUtils.DataStore;
import mobi.infolife.batteryAlarmEvent.BatteryUtils;
import mobi.infolife.utils.CommonUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class UpdateViewService extends Service {

	private static final String TAG = "UpdateViewService";
	private CWWidgetFactory mCWWidgetFactory = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		CommonUtils.l("View Service onCreate");
		super.onCreate();
		mCWWidgetFactory = CWWidgetFactory.getInstance();

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
		registerReceiver(BatteryUtils.batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		registerReceiver(BatteryUtils.powerConnectedReceiver, new IntentFilter(
				Intent.ACTION_POWER_CONNECTED));
		registerReceiver(BatteryUtils.powerDisconnectedReceiver,
				new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
		registerReceiver(BatteryUtils.screenOrientationChangedReceiver,
				new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));
		boolean launcherIsResume = Preferences.getLauncherOnStat(this);

		SettingActivity.setScreenHeight(this);

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		List<CWWidget> cwWidgetList = mCWWidgetFactory.getCWWidgets(this);

		if (launcherIsResume) {
			CommonUtils.l("Screen Is On, start update widget view");
			try {
				for (CWWidget cwWidget : cwWidgetList) {
					cwWidget.updateView();
				}
			} catch (CWRemoteException e) {
//				stopSelf();
				e.printStackTrace();
			}
		}
		Calendar c = Calendar.getInstance();
		int hh = c.get(Calendar.HOUR_OF_DAY);
		int mm = c.get(Calendar.MINUTE);
		if (hh==0 && mm==0) {
			startService(new Intent(this, LoadLunerAndHolidayViewService.class));
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
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ period, pendingIntent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
}
