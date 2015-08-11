package mobi.infolife.cwwidget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mobi.infolife.utils.BaseWeatherManager;
import mobi.infolife.utils.BestWeatherManager;
import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.FreeWeatherManager;
import mobi.infolife.utils.TaskUtils;
import mobi.infolife.utils.TimeTracker;
import mobi.infolife.utils.ViewUtils;
import mobi.infolife.widget.framework.MyAppWidgetManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
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
		
		context.startService(new Intent(context, UpdateDataService.class));
		context.startService(new Intent(context, UpdateViewService.class));
		context.startService(new Intent(context, ScreenStatService.class));
	}

	private MyAppWidgetManager mMyAppWidgetManager;
	private AppWidgetManager mAppWidgetManager;

	private Context mContext;

	public FullWidget(Context context, MyAppWidgetManager myAppWidgetManager) {
		mContext = context;
		mMyAppWidgetManager = myAppWidgetManager;
		mAppWidgetManager = AppWidgetManager.getInstance(context);
	}
	
	@Override
	public void updateView() throws CWRemoteException {
		CommonUtils.l("updateView");
		int[] fullWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, FullWidget.class));
//		int[] ezFullWidgetIds = CommonUtils.initIds(mMyAppWidgetManager,
//				new ComponentName(mContext, this.getClass()));

		RemoteViews fullView;

//		if (ezFullWidgetIds != null) {
//			if (ezFullWidgetIds.length > 0) {
//				CommonUtils.l(ezFullWidgetIds.length
//						+ " full widget need update");
//
//				for (int i = 0; i <= ezFullWidgetIds.length - 1; i++) {
//					fullView = ViewUtils.getFullRemoteviews(mContext,
//							ezFullWidgetIds[i]);
//
//					TaskUtils.widgetUpdate(mContext, fullView,
//							ezFullWidgetIds[i]);
//				}
//			}
//		}
		if (fullWidgetIds != null) {

			if (fullWidgetIds.length > 0) {
				CommonUtils.l(fullWidgetIds.length
						+ " weather widget need update");
				for (int i = 0; i <= fullWidgetIds.length - 1; i++) {
					fullView = ViewUtils.getFullRemoteviews(mContext, fullWidgetIds[i]);

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
		int[] ezFullWidgetIds = null;/*CommonUtils.initIds(mMyAppWidgetManager,
				new ComponentName(mContext, this.getClass()));*/

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
