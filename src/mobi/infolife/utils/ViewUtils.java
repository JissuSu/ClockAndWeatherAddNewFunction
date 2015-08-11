/**
 * 
 */
package mobi.infolife.utils;


import java.io.File;
import java.util.Date;
import java.util.List;


import mobi.infolife.cwwidget.Preferences;
import mobi.infolife.cwwidget.R;
import mobi.infolife.cwwidget.WeatherDetailActivity;
import mobi.infolife.cwwidget.WeatherWidget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class ViewUtils {

	public static void updateWeekNumView(RemoteViews views, Context context) {
		if (Preferences.getWeekInfoStat(context)) {
			views.setTextViewText(R.id.weeknum, " " + CommonUtils.getWeekNum());
			views.setViewVisibility(R.id.weeknum, View.VISIBLE);
		} else {
			views.setViewVisibility(R.id.weeknum, View.GONE);
		}

	}

	public static RemoteViews getTimeRemoteViews(Context context) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.clock);
		loadTimeInfoToView(context, views);
		loadTimeClickInfoToView(context, views);

		return views;
	}

	public static RemoteViews getFullRemoteviews(Context context,
			int appWidgetId) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.full);
		BatteryUtils.ifContainBattery = CommonUtils.getBooleanConfig(context, "ifContainBattery");
		if(BatteryUtils.ifContainBattery){loadBatteryView(views);}
		loadTimeInfoToView(context, views);
		loadSimpleAddressInfoToView(context, views, appWidgetId);
		loadSimpleWeatherInfoToView(context, views, appWidgetId);
		loadSimpleWeatherClickInfoToView(context, views, appWidgetId);
		loadAlarmView(views, context);
		loadEventsView(views, context);
		return views;
	}

	public static RemoteViews getWeatherRemoteviews(Context context,
			int appWidgetId) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.weather);
		loadDetailOtherInfoToView(context, views, appWidgetId);
		loadDetailWeatherInfoToView(context, views, appWidgetId);
		loadDetailWeatherClickInfoToView(context, views, appWidgetId);
		return views;
	}

	public static void loadTimeInfoToView(Context context, RemoteViews views) {
		Date date = new Date();

		int hour = date.getHours();
		if (Preferences.get24FormatStat(context)) {
			// timeFormate = new SimpleDateFormat("kk:mm");
			views.setViewVisibility(R.id.msign, View.INVISIBLE);
		} else {
			views.setViewVisibility(R.id.msign, View.VISIBLE);

			views.setTextViewText(R.id.msign, "AM");

			if (hour > 12) {
				hour = hour - 12;
				views.setTextViewText(R.id.msign, "PM");
			}
			// timeFormate = new SimpleDateFormat("hh:mm");
			// String msign = new SimpleDateFormat("a").format(date);
			// views.setTextViewText(R.id.msign, msign);
		}
		int minute = date.getMinutes();

		int screenHeight = Preferences.getScreenHeight(context);
		if (screenHeight > 900) {
			views.setImageViewResource(R.id.clock1,
					Constants.numberDrawableH[(int) hour / 10]);
			views.setImageViewResource(R.id.clock2,
					Constants.numberDrawableH[hour % 10]);
			views.setImageViewResource(R.id.clock3,
					Constants.numberDrawableH[(int) minute / 10]);
			views.setImageViewResource(R.id.clock4,
					Constants.numberDrawableH[minute % 10]);
			views.setImageViewResource(R.id.dotdot, R.drawable.dotdot_h);
		} else if (screenHeight < 500) {

			views.setImageViewResource(R.id.clock1,
					Constants.numberDrawableM[(int) hour / 10]);
			views.setImageViewResource(R.id.clock2,
					Constants.numberDrawableM[hour % 10]);
			views.setImageViewResource(R.id.clock3,
					Constants.numberDrawableM[(int) minute / 10]);
			views.setImageViewResource(R.id.clock4,
					Constants.numberDrawableM[minute % 10]);
			views.setImageViewResource(R.id.dotdot, R.drawable.dotdot_m);
		} else {
			views.setImageViewResource(R.id.clock1,
					Constants.numberDrawable[(int) hour / 10]);
			views.setImageViewResource(R.id.clock2,
					Constants.numberDrawable[hour % 10]);
			views.setImageViewResource(R.id.clock3,
					Constants.numberDrawable[(int) minute / 10]);
			views.setImageViewResource(R.id.clock4,
					Constants.numberDrawable[minute % 10]);
			views.setImageViewResource(R.id.dotdot, R.drawable.dotdot);
		}

		updateWeekNumView(views, context);
		// String timeString = timeFormate.format(date);
		String dateString = String.format(
				context.getString(R.string.date_formater), date, date);
		String weekdayString = String.format("%ta", date);

		views.setTextViewText(R.id.weekday, weekdayString);
		// views.setTextViewText(R.id.clock, timeString);
		views.setTextViewText(R.id.date, dateString);

	}

	public static void loadTimeClickInfoToView(Context context,
			RemoteViews views) {
		Intent dateIntent = new Intent().setAction(Constants.ACTION_CLICK_DATE);
		
		PendingIntent datePendingIntent = PendingIntent.getBroadcast(context,
				0, dateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.datelinear, datePendingIntent);

	}

	public static void loadDetailOtherInfoToView(Context context,
			RemoteViews views, int appWidgetId) {
		String addressString = Preferences
				.getShownAddress(context, appWidgetId);

		String updateTime;

		if (TaskUtils.get24HourMode(context)) {
			updateTime = CommonUtils.format24Date(Preferences.getUpdateTime(
					context, appWidgetId));
		} else {
			updateTime = CommonUtils.format12Date(Preferences.getUpdateTime(
					context, appWidgetId));
		}
		views.setTextViewText(R.id.address, addressString);
		views.setTextViewText(R.id.updatetime, "Updated:\r\n" + updateTime);
		views.setViewVisibility(R.id.refresh, View.VISIBLE);
		if (CommonUtils.getSDKVersionNumber() > Constants.SDK_FROYO)
			views.setViewVisibility(R.id.loadingbar, View.GONE);

	}

	public static void loadDetailWeatherClickInfoToView(Context context,
			RemoteViews views, int appWidgetId) {

		Intent settingIntent = new Intent(context, WeatherDetailActivity.class);
		settingIntent
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		settingIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
				Constants.WEATHER);
		PendingIntent settingPendingIntent = PendingIntent.getActivity(context,
				appWidgetId, settingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.weatherlinear, settingPendingIntent);

		Intent refreshIntent = new Intent(context, WeatherWidget.class);
		refreshIntent.setAction("update weather");
		refreshIntent
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
				context, appWidgetId + 100, refreshIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.refreshlayout, refreshPendingIntent);

	}

	public static void loadDetailWeatherInfoToView(Context context,
			RemoteViews views, int appWidgetId) {

		String filePath = TaskUtils
				.getWeatherDataFileName(context, appWidgetId);
		File weatherData = new File(filePath);
		if (!weatherData.exists()) {
			Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
			return;
		}

		BaseWeatherManager f = new BaseWeatherManager();
		String weatherString[] = f.parseWeatherData(context, appWidgetId);

		if (weatherString == null) {
			Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
			return;
		}

		views.setTextViewText(R.id.todaycondition,
				weatherString[Constants.TODAY_CONDITION_INDEX]);

		views.setTextViewText(R.id.todaytemp,
				weatherString[Constants.TODAY_TEMP_INDEX]);
		views.setTextViewText(R.id.todaylow, "L:"
				+ weatherString[Constants.TODAY_LOW_INDEX]);
		views.setTextViewText(R.id.todayhigh, "H:"
				+ weatherString[Constants.TODAY_HIGH_INDEX]);
		views.setTextViewText(R.id.firstdaytemp,
				weatherString[Constants.FISRTDAY_LOW_INDEX] + "/"
						+ weatherString[Constants.FISRTDAY_HIGH_INDEX]);
		views.setTextViewText(R.id.seconddaytemp,
				weatherString[Constants.SECONDDAY_LOW_INDEX] + "/"
						+ weatherString[Constants.SECONDDAY_HIGH_INDEX]);
		views.setTextViewText(R.id.thirddaytemp,
				weatherString[Constants.THIRDDAY_LOW_INDEX] + "/"
						+ weatherString[Constants.THIRDDAY_HIGH_INDEX]);

		views.setTextViewText(R.id.firstday,
				weatherString[Constants.FISRTDAY_NAME_INDEX]);
		views.setTextViewText(R.id.secondday,
				weatherString[Constants.SECONDDAY_NAME_INDEX]);
		views.setTextViewText(R.id.thirdday,
				weatherString[Constants.THIRDDAY_NAME_INDEX]);

		views.setImageViewResource(R.id.todayimage, ViewUtils
				.getWeatherImageId(weatherString[Constants.TODAY_ICON_INDEX],
						false));
		views.setImageViewResource(R.id.firstdayimage, ViewUtils
				.getWeatherImageId(
						weatherString[Constants.FIRSTDAY_ICON_INDEX], true));
		views.setImageViewResource(R.id.seconddayimage, ViewUtils
				.getWeatherImageId(
						weatherString[Constants.SECONDDAY_ICON_INDEX], true));
		views.setImageViewResource(R.id.thirddayimage, ViewUtils
				.getWeatherImageId(
						weatherString[Constants.THIRDDAY_ICON_INDEX], true));

		views.setTextViewText(R.id.todaytemp,
				weatherString[Constants.TODAY_TEMP_INDEX]);
		views.setTextViewText(R.id.todaylow, "L:"
				+ weatherString[Constants.TODAY_LOW_INDEX]);
		views.setTextViewText(R.id.todayhigh, "H:"
				+ weatherString[Constants.TODAY_HIGH_INDEX]);
		views.setTextViewText(R.id.firstdaytemp,
				weatherString[Constants.FISRTDAY_LOW_INDEX] + "/"
						+ weatherString[Constants.FISRTDAY_HIGH_INDEX]);
		views.setTextViewText(R.id.seconddaytemp,
				weatherString[Constants.SECONDDAY_LOW_INDEX] + "/"
						+ weatherString[Constants.SECONDDAY_HIGH_INDEX]);
		views.setTextViewText(R.id.thirddaytemp,
				weatherString[Constants.THIRDDAY_LOW_INDEX] + "/"
						+ weatherString[Constants.THIRDDAY_HIGH_INDEX]);

	}

	public static void loadSimpleWeatherClickInfoToView(Context context,
			RemoteViews views, int appWidgetId) {

		Intent intent = new Intent(context, WeatherDetailActivity.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// intent.putExtra("TYPE", "FULL");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
				Constants.FULL);
		// Utils.l("appWidgetId passed =" + appWidgetId);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		views.setOnClickPendingIntent(R.id.weatherlinear, pendingIntent);

		Intent dateIntent = new Intent().setAction(Constants.ACTION_CLICK_DATE);
		dateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		dateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
				Constants.FULL);
		
		PendingIntent datePendingIntent = PendingIntent.getBroadcast(context,
				appWidgetId, dateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.datelinear, datePendingIntent);

	}

	public static void loadSimpleWeatherInfoToView(Context context,
			RemoteViews views, int appWidgetId) {

		String filePath = TaskUtils
				.getWeatherDataFileName(context, appWidgetId);
		File weatherData = new File(filePath);
		if (!weatherData.exists()) {
			Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
			return;
		}

		BaseWeatherManager f = new BaseWeatherManager();
		String weatherString[] = f.parseWeatherData(context, appWidgetId);

		if (weatherString == null) {
			Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
			return;
		}

		views.setTextViewText(R.id.condition,
				weatherString[Constants.TODAY_CONDITION_INDEX]);
		views.setTextViewText(R.id.temp,
				weatherString[Constants.TODAY_TEMP_INDEX]);
		views.setTextViewText(R.id.low, "L:"
				+ weatherString[Constants.TODAY_LOW_INDEX]);
		views.setTextViewText(R.id.high, "H:"
				+ weatherString[Constants.TODAY_HIGH_INDEX]);

		views.setImageViewResource(R.id.todayimage, ViewUtils
				.getWeatherImageId(weatherString[Constants.TODAY_ICON_INDEX],
						false));
	}

	public static void loadSimpleAddressInfoToView(Context context,
			RemoteViews views, int appWidgetId) {
		String addressString = Preferences
				.getShownAddress(context, appWidgetId);

		views.setTextViewText(R.id.address, addressString);

	}

	public static int getWeatherImageId(String iconUrl, boolean alwaysLight) {
		// CommonUtils.l("icon=" + iconUrl);
		Date date = new Date();
		// CommonUtils.l("ICONURL" + iconUrl);
		boolean ISLIGHT = true;
		int hour = date.getHours();
		if (hour > 18 || hour < 6)
			ISLIGHT = false;
		if (alwaysLight)
			ISLIGHT = true;
		if (iconUrl != null) {
			if (iconUrl.contains("sunny") || iconUrl.contains("mostly_sunny")
					|| iconUrl.contains("clear")
					|| TextUtils.equals(iconUrl.trim(), "01")
					|| TextUtils.equals(iconUrl.trim(), "33")
					|| TextUtils.equals(iconUrl.trim(), "30")
					|| TextUtils.equals(iconUrl.trim(), "02")
					|| TextUtils.equals(iconUrl.trim(), "34")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_CLEAR_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_CLEAR_DARK];
			} else if (iconUrl.contains("rain") || iconUrl.contains("shower")
					|| iconUrl.contains("lightrain")
					|| TextUtils.equals(iconUrl.trim(), "12")
					|| TextUtils.equals(iconUrl.trim(), "13")
					|| TextUtils.equals(iconUrl.trim(), "14")
					|| TextUtils.equals(iconUrl.trim(), "39")
					|| TextUtils.equals(iconUrl.trim(), "40")
					|| TextUtils.equals(iconUrl.trim(), "26")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_SMALL_RAIN_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_SMALL_RAIN_DARK];
			} else if (iconUrl.contains("heavyrain")
					|| TextUtils.equals(iconUrl.trim(), "18")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_BIG_RAIN_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_BIG_RAIN_DARK];
			} else if (iconUrl.contains("fog") || iconUrl.contains("smoke")
					|| iconUrl.contains("mist")
					|| TextUtils.equals(iconUrl.trim(), "05")
					|| TextUtils.equals(iconUrl.trim(), "37")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_FOG_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_FOG_DARK];
			} else if (iconUrl.contains("thunderstorm")
					|| iconUrl.contains("storm")
					|| TextUtils.equals(iconUrl.trim(), "15")
					|| TextUtils.equals(iconUrl.trim(), "16")
					|| TextUtils.equals(iconUrl.trim(), "17")
					|| TextUtils.equals(iconUrl.trim(), "41")
					|| TextUtils.equals(iconUrl.trim(), "42")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_THUNDER_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_THUNDER_DARK];
			} else if (iconUrl.contains("haze")
					|| TextUtils.equals(iconUrl.trim(), "11")) {
				return Constants.weatherDrawable[Constants.INDEX_HAZE];
			} else if (iconUrl.contains("hail")

			|| TextUtils.equals(iconUrl.trim(), "25")) {
				return Constants.weatherDrawable[Constants.INDEX_HAIL];
			} else if (iconUrl.contains("flurries")
					|| TextUtils.equals(iconUrl.trim(), "19")
					|| TextUtils.equals(iconUrl.trim(), "20")
					|| TextUtils.equals(iconUrl.trim(), "43")) {
				return Constants.weatherDrawable[Constants.INDEX_SMALL_SNOW];
			} else if (iconUrl.contains("chance_of_snow")
					|| iconUrl.contains("snow")
					|| TextUtils.equals(iconUrl.trim(), "23")
					|| TextUtils.equals(iconUrl.trim(), "21")
					|| TextUtils.equals(iconUrl.trim(), "22")
					|| TextUtils.equals(iconUrl.trim(), "44")) {
				return Constants.weatherDrawable[Constants.INDEX_SNOW];

			} else if (iconUrl.contains("sleet")
					|| TextUtils.equals(iconUrl.trim(), "29")) {
				return Constants.weatherDrawable[Constants.INDEX_SNOW_RAIN];

			} else if (iconUrl.contains("dust")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_DUST_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_DUST_DARK];

			} else if (iconUrl.contains("icy")
					|| TextUtils.equals(iconUrl.trim(), "24")
					|| TextUtils.equals(iconUrl.trim(), "31")) {
				return Constants.weatherDrawable[Constants.INDEX_ICY];

			} else if (iconUrl.contains("wind")
					|| TextUtils.equals(iconUrl.trim(), "32")) {
				return Constants.weatherDrawable[Constants.INDEX_WIND];

			} else if (iconUrl.contains("cloud")
					|| iconUrl.contains("overcast")
					|| TextUtils.equals(iconUrl.trim(), "03")
					|| TextUtils.equals(iconUrl.trim(), "04")
					|| TextUtils.equals(iconUrl.trim(), "06")
					|| TextUtils.equals(iconUrl.trim(), "07")
					|| TextUtils.equals(iconUrl.trim(), "08")
					|| TextUtils.equals(iconUrl.trim(), "35")
					|| TextUtils.equals(iconUrl.trim(), "36")
					|| TextUtils.equals(iconUrl.trim(), "38")) {
				if (ISLIGHT)
					return Constants.weatherDrawable[Constants.INDEX_PART_CLOUD_LIGHT];
				else
					return Constants.weatherDrawable[Constants.INDEX_PART_CLOUD_DARK];
			}
		}

		return Constants.weatherDrawable[Constants.INDEX_UNKNOW];

	}

	public static void widgetUpdate(Context context, RemoteViews views,
			int appWidgetId) {
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(appWidgetId, views);
//		MyAppWidgetManager.updateAppWidget(context, views,
//				new int[] { appWidgetId });
	}

	public static void widgetUpdate(Context context, RemoteViews views,
			int[] appWidgetId) {
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(appWidgetId, views);
//		MyAppWidgetManager.updateAppWidget(context, views, appWidgetId);
	}
	
	public static void loadBatteryView(RemoteViews views){
        views.setImageViewResource(R.id.batteryView, BatteryUtils.getBatteryImageId());
        views.setTextViewText(R.id.batteryText, BatteryUtils.getCurrentBatteryLevel()+"%");
    }
	
	public static void loadAlarmView(RemoteViews views,Context c){
		AlarmUtils alarmUtils = new AlarmUtils(c);
		views.setTextViewText(R.id.alarmText, alarmUtils.getNextAlarm());
		Intent intenta;
        if(isIntentAvailable(c,AlarmClock.ACTION_SHOW_ALARMS)){
            intenta = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        }
        else {
            intenta = new Intent(AlarmClock.ACTION_SET_ALARM);
        }
        PendingIntent pendingIntenta = PendingIntent.getActivity(c, 0, intenta, 0);
        views.setOnClickPendingIntent(R.id.alarmText, pendingIntenta);
	}
	
	public static void loadEventsView(RemoteViews views,Context c){
		EventUtils eventUtils = new EventUtils(c);
		views.setTextViewText(R.id.eventText, eventUtils.getNextEvent());
		long id = eventUtils.getEventId();
		Uri uri = ContentUris.withAppendedId( Uri.parse("content://com.android.calendar/events"), id);
        Intent intente = new Intent(Intent.ACTION_VIEW).setData(uri);
        PendingIntent pendingIntente = PendingIntent.getActivity(c, 0, intente, 0);
        views.setOnClickPendingIntent(R.id.eventText,pendingIntente);
	}
	
	public static boolean isIntentAvailable(Context context, String action) {
	        final PackageManager packageManager = context.getPackageManager();
	        final Intent intent = new Intent(action);
	        List<ResolveInfo> list =
	                packageManager.queryIntentActivities(intent,
	                        PackageManager.MATCH_DEFAULT_ONLY);
	        return list.size() > 0;
	    }

}
