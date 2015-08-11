package mobi.infolife.cwwidget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mobi.infolife.utils.BaseWeatherManager;
import mobi.infolife.utils.BestWeatherManager;
import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.FreeWeatherManager;
import mobi.infolife.utils.TaskUtils;
import mobi.infolife.utils.ViewUtils;
import mobi.infolife.widget.framework.MyAppWidgetManager;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WeatherWidget extends AppWidgetProvider implements CWWidget {
	private static WeatherWidget sInstance;

	public WeatherWidget() {
		super();
	}

	// static synchronized WeatherWidget getInstance() {
	// if (sInstance == null) {
	// sInstance = new WeatherWidget();
	// }
	//
	// return sInstance;
	// }

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
		final String action = intent.getAction();
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
			}
			if (action.equals("update weather")) {
				// if (CommonUtils.isSDCardMounted()) {
				CommonUtils.l("get refresh action");
				int id = intent.getIntExtra(
						AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

				RemoteViews weatherView = updateLoadingViews(context, id);
				TaskUtils.widgetUpdate(context, weatherView, id);
				CommonUtils.playTone(context);
				TaskUtils.updateWeatherInternetDataNotInThread(context, id);
				weatherView = ViewUtils.getWeatherRemoteviews(context, id);
				// /manager.updateAppWidget(id, weatherView);
				TaskUtils.widgetUpdate(context, weatherView, id);

				// }
			}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		mAppWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews views;
		for (int appWidgetId : appWidgetIds) {
			views = ViewUtils.getWeatherRemoteviews(context, appWidgetId);
			mAppWidgetManager.updateAppWidget(appWidgetId, views);
			// TaskUtils.widgetUpdate(context, views, appWidgetId);
		}
		// manager.updateAppWidget(new ComponentName(context,
		// WeatherWidget.class), views);

		context.startService(new Intent(context, UpdateDataService.class));
		context.startService(new Intent(context, UpdateViewService.class));
		context.startService(new Intent(context, ScreenStatService.class));

	}

 



	public static RemoteViews updateLoadingViews(Context context,
			int appWidgetId) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.weather);

		views.setTextViewText(R.id.updatetime, "Loading\r\nPlease wait...");
		views.setViewVisibility(R.id.refresh, View.GONE);
		if (CommonUtils.getSDKVersionNumber() > Constants.SDK_FROYO)
			views.setViewVisibility(R.id.loadingbar, View.VISIBLE);
		return views;
	}

//	public static void addClickEvent(Context context, int appWidgetId,
//			RemoteViews views) {
//
//		Intent settingIntent = new Intent(context, WeatherDetailActivity.class);
//		settingIntent
//				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//		PendingIntent settingPendingIntent = PendingIntent.getActivity(context,
//				appWidgetId, settingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		views.setOnClickPendingIntent(R.id.weatherlinear, settingPendingIntent);
//
//		Intent refreshIntent = new Intent(context, WeatherWidget.class);
//		refreshIntent.setAction("update weather");
//		refreshIntent
//				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//
//		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
//				context, appWidgetId + 100, refreshIntent,
//				PendingIntent.FLAG_UPDATE_CURRENT);
//		views.setOnClickPendingIntent(R.id.refreshlayout, refreshPendingIntent);
//	}

	// private ComponentName[] mComponentNames = {
	// new ComponentName("mobi.infolife.cwwidget",
	// "mobi.infolife.cwwidget.WeatherWidget"),
	// };

	private MyAppWidgetManager mMyAppWidgetManager;
	private AppWidgetManager mAppWidgetManager;

	private Context mContext;

	public WeatherWidget(Context context, MyAppWidgetManager myAppWidgetManager) {
		mContext = context;
		mMyAppWidgetManager = myAppWidgetManager;
		mAppWidgetManager = AppWidgetManager.getInstance(context);
	}

	@Override
	public void updateView() throws CWRemoteException {
		int[] weatherWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext,
						WeatherWidget.class));
//		int[] ezWeatherWidgetIds = CommonUtils.initIds(mMyAppWidgetManager,
//				new ComponentName(mContext, this.getClass()));
		RemoteViews weatherView;

//		if (ezWeatherWidgetIds != null) {
//
//			if (ezWeatherWidgetIds.length > 0) {
//				CommonUtils.l(ezWeatherWidgetIds.length
//						+ " ez weather widget need update");
//				for (int i = 0; i <= ezWeatherWidgetIds.length - 1; i++) {
//					weatherView = ViewUtils.getWeatherRemoteviews(mContext,
//							ezWeatherWidgetIds[i]);
//					TaskUtils.widgetUpdate(mContext, weatherView,
//							ezWeatherWidgetIds[i]);
//
//				}
//			}
//		}
		if (weatherWidgetIds != null) {

			if (weatherWidgetIds.length > 0) {
				CommonUtils.l(weatherWidgetIds.length
						+ " weather widget need update");
				for (int i = 0; i <= weatherWidgetIds.length - 1; i++) {
					weatherView = ViewUtils.getWeatherRemoteviews(mContext,
							weatherWidgetIds[i]);

					mAppWidgetManager.updateAppWidget(weatherWidgetIds[i],
							weatherView);

				}
			}
		}
	}

	@Override
	public void loadData() throws CWRemoteException {
		int[] weatherWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext,
						WeatherWidget.class));
		int[] ezWeatherWidgetIds = null;/*CommonUtils.initIds(mMyAppWidgetManager,
				new ComponentName(mContext, this.getClass()));*/

		int[] newArray = CommonUtils.combineArray(weatherWidgetIds,
				ezWeatherWidgetIds);

		if (newArray != null) {
			if (newArray.length > 0) {
				CommonUtils.l(newArray.length + "  weather widget need update");

				for (int i = 0; i <= newArray.length - 1; i++) {
					// update data section
					int id = newArray[i];
					TaskUtils.updateWeatherInternetDataInThread(mContext, id);

				}
			}
		}

	}

	@Override
	public void loadPreferences() {
		// TODO Auto-generated method stub

	}

}
