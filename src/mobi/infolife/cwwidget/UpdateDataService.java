package mobi.infolife.cwwidget;

import java.util.List;

import mobi.infolife.utils.CommonUtils;
import mobi.infolife.widget.framework.MyAppWidgetManager;
import mobi.infolife.widget.framework.MyAppWidgetManager.CallBack;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateDataService extends Service {

	// private static final String TAG = "DigitalClock";
	private MyAppWidgetManager mMyAppWidgetManager = null;
	private CWWidgetFactory mCWWidgetFactory = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		CommonUtils.l("Data Service onCreate");
		super.onCreate();
		mCWWidgetFactory = CWWidgetFactory.getInstance();
		mMyAppWidgetManager = MyAppWidgetManager
				.getInstance(this.getApplicationContext(),
						RemoteServiceCallBack.getInstance(this
								.getApplicationContext()));
	}

	@Override
	public void onDestroy() {
		CommonUtils.l("Data Service onDestroy");
		super.onDestroy();

	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// Log.i(TAG, "start service Time update");
		CommonUtils.l("Data Service start");
		// get id from system provider

		boolean taskStat = Preferences.getDataTaskRunning(this);

		if (!taskStat) {
			CommonUtils.l("task is not running now, task start");
			Preferences.setDataTaskRunning(this, true);
			List<CWWidget> cwWidgetList = mCWWidgetFactory.getCWWidgets(this,
					mMyAppWidgetManager);
			try {
				for (CWWidget cwWidget : cwWidgetList) {
					cwWidget.loadData();
				}
			} catch (CWRemoteException e) {
				Preferences.setDataTaskRunning(this, false);
				stopSelf();
				e.printStackTrace();
			}
		}else{
			CommonUtils.l("task is running now, task skip");
		}
		long updateMilis = 15 * 60 * 1000;
		restartServiceInTime(updateMilis, intent);
	}

	public void restartServiceInTime(long period, Intent intent) {
		long now = System.currentTimeMillis();

		Intent serviceIntent = intent;
		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				serviceIntent, 0);
		// // Schedule alarm, and force the device awake for this update
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, now + period, pendingIntent);
	}

	// class mythread extends thread {
	// private context context;
	// private remoteviews views;
	// private appwidgetmanager widgetmanager;
	//
	// public mythread(context context, appwidgetmanager widgetmanager) {
	// this.context = context;
	// this.widgetmanager = widgetmanager;
	// }
	//
	// @override
	// public void run() {
	// while (true) {
	// views = fullwidget.updatefullviews(context);
	// widgetmanager.updateappwidget(new componentname(context,
	// fullwidget.class), views);
	// try {
	// sleep(1000);
	// } catch (interruptedexception e) {
	// e.printstacktrace();
	// }
	// }
	// }
	// }

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
}
