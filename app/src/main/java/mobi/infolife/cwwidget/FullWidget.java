package mobi.infolife.cwwidget;

import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.TaskUtils;
import mobi.infolife.utils.ViewUtils;
import mobi.infolife.widget.framework.MyAppWidgetManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FullWidget extends AppWidgetProvider implements CWWidget {
	private final static String TAG = "FullWidget";
    
	public FullWidget() {
		super();
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Preferences.dropSettings(context, appWidgetIds);

	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		String action = intent.getAction();
		if (action != null) {
			if (action.equals(MyAppWidgetManager.REBIND_ACTION)
					|| action.equals(Constants.ACTION_READY)
					|| action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
				context.startService(new Intent(context,
						UpdateDataService.class)
						.setAction(MyAppWidgetManager.REBIND_ACTION));
				context.startService(new Intent(context,
						UpdateViewService.class)
						.setAction(MyAppWidgetManager.REBIND_ACTION));
				context.startService(new Intent(context,
						ScreenStatService.class)
						.setAction(MyAppWidgetManager.REBIND_ACTION));
			}else if(action.equals(Constants.ACTION_CLICK_DATE)){
				CommonUtils.getClockClassNameNew(context, intent);
				String packageName = Preferences.getClockPackageName(context);
				if(!packageName.equals(Constants.NOTSET)){
					CommonUtils.startApplicationWithPackageName(context, packageName);
				}else{
					CommonUtils.startApplicationWithPackageName(context, "com.android.settings");
				}
			}if(action.equals(Constants.ACTION_CLICK_CALENDAR)){
				CommonUtils.getCalendarClassNameNew(context, intent);
				String packageName = Preferences.getCalendarPackageName(context);
				if(!packageName.equals(Constants.NOTSET)){
					CommonUtils.startApplicationWithPackageName(context, packageName);
				}else{
					CommonUtils.startApplicationWithPackageName(context, "com.android.settings");
				}
			} 
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		CommonUtils.l("onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews views;
		mAppWidgetManager = AppWidgetManager.getInstance(context);

		for (int appWidgetId : appWidgetIds) {
			CommonUtils.l("appWidgetId:" + appWidgetId);
			views = ViewUtils.getFullRemoteviews(context, appWidgetId);
			mAppWidgetManager.updateAppWidget(appWidgetId, views);
		}
		if (CommonUtils.getBooleanConfig(context, "ifContainBattery")) {
			context.startService(new Intent(context,LoadBatteryViewService.class));
		}
		if (CommonUtils.getBooleanConfig(context, "ifContainAlarm")) {
			context.startService(new Intent(context, LoadAlarmViewService.class));
		}
		if (CommonUtils.getBooleanConfig(context, "ifContainXK")) {
			context.startService(new Intent(context, LoadXKViewService.class));
		}
		if (CommonUtils.getBooleanConfig(context, "ifContainEvents")) {
			context.startService(new Intent(context, LoadEventsViewService.class));
		}
		if (CommonUtils.getBooleanConfig(context, "ifContainLunerAndHoliday")) {
			context.startService(new Intent(context, LoadLunerAndHolidayViewService.class));
		}
		context.startService(new Intent(context, UpdateDataService.class));
		context.startService(new Intent(context, UpdateViewService.class));
		context.startService(new Intent(context, ScreenStatService.class));
	}

	private AppWidgetManager mAppWidgetManager;

	private Context mContext;

	public FullWidget(Context context) {
		mContext = context;
		mAppWidgetManager = AppWidgetManager.getInstance(context);
	}
	
	@Override
	public void updateView() throws CWRemoteException {
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		RemoteViews fullView;
		if (fullWidgetIds != null) {
			if (fullWidgetIds.length > 0) {
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.getFullRemoteviews(mContext, fullWidgetIds[i]);
					mAppWidgetManager.updateAppWidget(fullWidgetIds[i],fullView);
				}
			}
		}
	}
	public void updateBatteryView(){
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		RemoteViews fullView;
		if (fullWidgetIds != null) {
			if (fullWidgetIds.length > 0) {
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.loadBatteryView(mContext);
					mAppWidgetManager.updateAppWidget(fullWidgetIds[i],
							fullView);
				}
			}
		}
	}

	public void updateAlarmView(){
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		RemoteViews fullView;
		if (fullWidgetIds != null) {
			if (fullWidgetIds.length > 0) {
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.loadAlarmView(mContext);
					mAppWidgetManager.updateAppWidget(fullWidgetIds[i],
							fullView);
				}
			}
		}
	}

	public void updateXKView(){
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		RemoteViews fullView;
		if (fullWidgetIds != null) {
			if (fullWidgetIds.length > 0) {
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.loadXKView(mContext);
					mAppWidgetManager.updateAppWidget(fullWidgetIds[i],
							fullView);
				}
			}
		}
	}

	public void updateEventsView(){
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		RemoteViews fullView;
		if (fullWidgetIds != null) {
			if (fullWidgetIds.length > 0) {
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.loadEventsView(mContext);
					mAppWidgetManager.updateAppWidget(fullWidgetIds[i],
							fullView);
				}
			}
		}
	}

	public void updateLunerAndHolidayView(){
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		RemoteViews fullView;
		if (fullWidgetIds != null) {
			if (fullWidgetIds.length > 0) {
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.loadLunerAndHoliday(mContext, fullWidgetIds[i]);
					mAppWidgetManager.updateAppWidget(fullWidgetIds[i],
							fullView);
				}
			}
		}
	}

	@Override
	public void loadData() throws CWRemoteException {

		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
		int[] ezFullWidgetIds = null;

		int[] combineIds = CommonUtils.combineArray(fullWidgetIds,
				ezFullWidgetIds);

		if (combineIds != null) {
			if (combineIds.length > 0) {
				CommonUtils.l(combineIds.length + " full widget need update");
				for (int i = 0; i <= combineIds.length - 1; i++) {
					int id = combineIds[i];

					TaskUtils.updateWeatherInternetDataInThread(mContext, id);

				}
			}
		}
	}

	@Override
	public void loadPreferences() {
		// CommonUtils.l("loadPreferences");
	}

}
