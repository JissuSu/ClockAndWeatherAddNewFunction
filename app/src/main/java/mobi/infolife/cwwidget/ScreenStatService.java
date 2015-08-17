package mobi.infolife.cwwidget;

import java.util.Timer;
import java.util.TimerTask;

import mobi.infolife.utils.CommonUtils;
import mobi.intuitit.android.content.LauncherIntent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

public class ScreenStatService extends Service {

	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ScreenStatService.startService(context, intent.getAction());
		}

	};

	public void startReceive() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(LauncherIntent.Broadcast.BROADCAST_HOME_PAUSE);
		intentFilter.addAction(LauncherIntent.Broadcast.BROADCAST_HOME_RESUME);

		this.registerReceiver(this.broadcastReceiver, intentFilter);
	}

	public void stopReceive() {
		this.unregisterReceiver(this.broadcastReceiver);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.startReceive();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		if (intent != null && intent.getAction() != null) {
			//CommonUtils.l(intent.getAction());
			if (TextUtils.equals(intent.getAction(),
					LauncherIntent.Broadcast.BROADCAST_HOME_PAUSE)) {
				CommonUtils.l("On pause");
				Preferences.setLauncherOnStat(this, false);
			}
			if (TextUtils.equals(intent.getAction(),
					LauncherIntent.Broadcast.BROADCAST_HOME_RESUME)) {
				CommonUtils.l("On resume");
				Preferences.setLauncherOnStat(this, true);
				Preferences.setDataTaskRunning(this, false);
				this.startService(new Intent(this, UpdateViewService.class));
				this.startService(new Intent(this, LoadBatteryViewService.class));
			}
			if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				CommonUtils.l("Screen is off");
				Preferences.setScreenOnStat(this, false);

			} else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
				CommonUtils.l("Screen is on");
				Preferences.setScreenOnStat(this, true);
				this.startService(new Intent(this, UpdateViewService.class));
				this.startService(new Intent(this, LoadBatteryViewService.class));
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.stopReceive();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void startService(Context context) {
		Intent intent = new Intent(context, ScreenStatService.class);
		context.startService(intent);
	}

	public static void startService(Context context, String action) {
		Intent intent = new Intent(context, ScreenStatService.class);
		intent.setAction(action);
		context.startService(intent);
	}

	public static void stopService(Context context) {
		Intent intent = new Intent(context, ScreenStatService.class);
		context.stopService(intent);
	}
}
