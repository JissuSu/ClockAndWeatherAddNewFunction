package mobi.infolife.cwwidget;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.TaskUtils;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences {
	// public static final long GROUP_FACEBOOK = -1;
	public static final long GROUP_ALLCONTACTS = 0;
	public static final long GROUP_STARRED = -2;

	public static final int VIRTUAL_GROUP_COUNT = 3;

	public static final int NAME_DISPLAY_NAME = 0;
	public static final int NAME_GIVEN_NAME = 1;
	public static final int NAME_FAMILY_NAME = 2;

	public static final String SPAN_X = "SpanX-%d";

	public static final String GROUP_ID = "GroupId-%d";
	public static final String DISPLAY_LABEL = "DisplayLabel-%d";
	public static final String SHOW_NAME = "ShowName-%d";
	public static final String NAME_KIND = "NameKind-%d";
	public static final String BACKGROUND_ALPHA = "BackgroundAlpha-%d";
	public static final String CURRENT_THREAD = "CurrnetThread-%d";
	public static final String CURRENT_ADDRESS = "CurrnetaAddress-%d";
	public static final String CURRENT_NAME = "CurrnetaName-%d";
	public static final String SHOWED_NUM = "showed_num";
	public static final String COLUMN_COUNT = "CoulumnCount-%d";
	public static final String TEXT_ALIGN = "TextAlign-%d";

	public static final String UPDATE_TIME = "UpdateTime-%d";
	public static final String CITY_SEARCH_KEY = "LastCity-%d";
	public static final String CITY_ID_SEARCH_KEY = "LastCityId-%d";

	public static final String CURRENT_CL = "CurrentLanguageAndCountry";

	public static final String SAVED_CITY = "saved_city-%d";
	public static final String WEATHER_DATA = "weather_data-%d";

	public static final String AUTO_ADDRESS = "auto_address-%d";
	public static final String AUTO_REFRESH = "auto_refresh-%d";
	public static final String USE_CELSIUS = "use_celsius";
	public static final String TWENTYFOUR_FORMAT = "24_format";

	public static final String REFRESH_INTERVAL = "refresh_interval";
	public static final String NEED_UPDATEVIEW = "need_update_view-%d";
	public static final String SCREENON_STAT = "screen_on";
	public static final String LAUNCHER_STAT = "launcher_on";
	public static final String DATA_TASK_STAT = "data_is_ruinng";
	public static final String WEEK_INFO_STAT = "week_info_stat";
	public static final String HAS_SET_TIME = "has_set_time";

	public static final String SKIP_STAT = "skip_update_view";

	public static final String LOCATED_CITY = "located_city-%d";
	public static final String LOCATED_STATE = "located_state-%d";
	public static final String LOCATED_COUNTRY = "located_country-%d";
	public static final String LOCATED_CITY_LAT = "LocatedCityLat-%d";
	public static final String LOCATED_CITY_LON = "LocatedCityLon-%d";

	public static final String GPS_CITY = "LocatedCity";
	public static final String GPS_STATE = "located_state";
	public static final String GPS_COUNTRY = "located_country";
	public static final String GPS_CITY_LAT = "Located_CityLat";
	public static final String GPS_CITY_LON = "Located_CityLon";

	public static final String SHOWN_ADDRESS = "ShownAddress-%d";

	public static final String SCREEN_HIEGHT = "ScreenHeight";

	public static final String SAVED_Id = "saved_id";

	public static final String ON_CLICK = "onClick-%d";
	public static final int CLICK_QCB = 0;
	public static final int CLICK_DIAL = 1;
	public static final int CLICK_SHWCONTACT = 2;
	public static final int CLICK_SMS = 3;

	public static final String BGIMAGE = "BGImage-%d";
	public static final int BG_BLACK = 0;
	public static final int BG_WHITE = 1;
	public static final int BG_TRANS = 2;

	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_RIGHT = 2;

	public static final String CLOCK_PACKAGE_NAME = "ClockPackage2";
	public static final String CLOCK_ACTIVITY_NAME = "ClockActivity2";
	public static final String CALENDAR_PACKAGE_NAME = "CalendarPackage2";
	public static final String CALENDAR_ACTIVITY_NAME = "CalendarActivity2";
	public static String get(String aPref, int aAppWidgetId) {
		String key = String.format(aPref, 0);
		return key;
	}

	// public static long getGroupId(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return Long.parseLong(prefs.getString(Preferences.get(
	// Preferences.GROUP_ID, aAppWidgetId), "0"));
	// }

	// public static String getDisplayLabel(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getString(Preferences.get(Preferences.DISPLAY_LABEL,
	// aAppWidgetId), "SMS");
	// }

	// public static int getBGImage(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return Integer.parseInt(prefs.getString(Preferences.get(
	// Preferences.BGIMAGE, aAppWidgetId), "0"));
	// }
	//
	// public static int getNameKind(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return Integer.parseInt(prefs.getString(Preferences.get(
	// Preferences.NAME_KIND, aAppWidgetId), "0"));
	// }

	// public static boolean getShowName(Context context, int aAppWidgetId) {
	// return true;
	// }
	// SharedPreferences prefs =
	// PreferenceManager.getDefaultSharedPreferences(context);
	// return prefs.getBoolean(Preferences.get(Preferences.SHOW_NAME,
	// aAppWidgetId), true);

	// public static int getColumnCount(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getInt(Preferences.get(Preferences.COLUMN_COUNT,
	// aAppWidgetId), 1);
	// }

	// public static int getBackgroundAlpha(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getInt(Preferences.get(Preferences.BACKGROUND_ALPHA,
	// aAppWidgetId), 255);
	// }
	//
	// public static int getSpanX(Context context, int aAppWidgetId,
	// int defaultValue) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getInt(Preferences.get(Preferences.SPAN_X, aAppWidgetId),
	// defaultValue);
	// }

	// public static int getOnClickAction(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return Integer
	// .parseInt(prefs.getString(Preferences.get(Preferences.ON_CLICK,
	// aAppWidgetId), String.valueOf(CLICK_QCB)));
	// }

	// public static void setSpanX(Context context, int aAppWidgetId, int value)
	// {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// Editor edit = prefs.edit();
	// edit.putInt(Preferences.get(SPAN_X, aAppWidgetId), value);
	// edit.commit();
	// }
	//
	// public static int getTextAlign(Context context, int aAppWidgetId) {
	// return ALIGN_RIGHT;
	// // SharedPreferences prefs =
	// // PreferenceManager.getDefaultSharedPreferences(context);
	// // return
	// //
	// Integer.parseInt(prefs.getString(Preferences.get(Preferences.TEXT_ALIGN,
	// // aAppWidgetId), String.valueOf(ALIGN_RIGHT)));
	// }

	public static int[] getAllWidgetIds(Context context) {
		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		List<int[]> result = new ArrayList<int[]>();

		result.add(awm.getAppWidgetIds(new ComponentName(context,
				FullWidget.class)));

		int i = 0;
		for (int[] arr : result)
			i += arr.length;

		int[] res = new int[i];
		i = 0;
		for (int[] arr : result) {
			for (int id : arr) {
				res[i++] = id;
			}
		}

		return res;
	}

	// public static void setCurrentThread(Context context, int aAppWidgetId,
	// String threadId) {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit().putString(
	// Preferences.get(Preferences.CURRENT_THREAD, aAppWidgetId),
	// threadId).commit();
	// }
	//
	// public static String getCurrentThread(Context context, int aAppWidgetId)
	// {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getString(Preferences.get(Preferences.CURRENT_THREAD,
	// aAppWidgetId), "ERROR");
	// }
	//
	// public static void setCurrentAddress(Context context, int aAppWidgetId,
	// String address) {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit().putString(
	// Preferences.get(Preferences.CURRENT_ADDRESS, aAppWidgetId),
	// address).commit();
	// }
	//
	// public static String getCurrentAddress(Context context, int aAppWidgetId)
	// {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getString(Preferences.get(Preferences.CURRENT_ADDRESS,
	// aAppWidgetId), "ERROR");
	// }
	//
	// public static void setCurrentName(Context context, int aAppWidgetId,
	// String name) {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit().putString(
	// Preferences.get(Preferences.CURRENT_NAME, aAppWidgetId), name)
	// .commit();
	// }
	//
	// public static String getCurrentName(Context context, int aAppWidgetId) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getString(Preferences.get(Preferences.CURRENT_NAME,
	// aAppWidgetId), "ERROR");
	// }
	//
	// public static void setShowedSmsNum(Context context, int num) {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit().putInt(SHOWED_NUM, num).commit();
	// }
	//
	// public static int getShowedSmsNum(Context context) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getInt(SHOWED_NUM, 20);
	// }

	public static void dropSettings(Context context, int[] appWidgetIds) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = prefs.edit();
		for (int appWId : appWidgetIds) {
			edit.remove(Preferences.get(Preferences.UPDATE_TIME, appWId));
			edit.remove(Preferences.get(Preferences.REFRESH_INTERVAL, appWId));
			edit.remove(Preferences.get(Preferences.USE_CELSIUS, appWId));
			edit.remove(Preferences.get(Preferences.CITY_SEARCH_KEY, appWId));
			edit.remove(Preferences.get(Preferences.CURRENT_CL, appWId));
			edit.remove(Preferences.get(Preferences.AUTO_ADDRESS, appWId));
		}
		edit.commit();
	}

	// public static void setCitySearchKeyById(Context context, String name, int
	// id) {
	// name = URLEncoder.encode(name.trim());
	//
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit()
	// .putString(Preferences.get(CITY_SEARCH_KEY, id), name).commit();
	//
	// }
	//
	// public static String getCitySearchKeyById(Context context, int id) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getString(Preferences.get(CITY_SEARCH_KEY, id),
	// Constants.NOTSET);
	// }

	public static void setCityIDById(Context context, String name, int id) {
		name = URLEncoder.encode(name.trim());

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putString(Preferences.get(CITY_ID_SEARCH_KEY, id), name)
				.commit();

	}

	public static String getCityIdById(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Preferences.get(CITY_ID_SEARCH_KEY, id),
				Constants.NOTSET);
	}

	public static void setCurrentCL(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(CURRENT_CL, name).commit();
	}

	public static String getCurrentCL(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(CURRENT_CL, Constants.NOTSET);
	}

	public static void setLocatedCity(Context context, String name, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putString(Preferences.get(LOCATED_CITY, id), name).commit();
	}

	public static String getLocatedCity(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Preferences.get(LOCATED_CITY, id),
				Constants.NOTSET);
	}

	public static void setGPSCity(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(GPS_CITY, name).commit();
	}

	public static String getGPSCity(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(GPS_CITY, Constants.NOTSET);
	}

	public static void setLocatedState(Context context, String name, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putString(Preferences.get(LOCATED_STATE, id), name).commit();
	}

	public static String getLocatedState(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Preferences.get(LOCATED_STATE, id),
				Constants.NOTSET);
	}

	public static void setGPSState(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(GPS_STATE, name).commit();
	}

	public static String getGPSState(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(GPS_STATE, Constants.NOTSET);
	}

	public static void setLocatedCountry(Context context, String name, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putString(Preferences.get(LOCATED_COUNTRY, id), name).commit();
	}

	public static String getLocatedCountry(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Preferences.get(LOCATED_COUNTRY, id),
				Constants.NOTSET);
	}

	public static void setGPSCountry(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(GPS_COUNTRY, name).commit();
	}

	public static String getGPSCountry(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(GPS_COUNTRY, Constants.NOTSET);
	}

	public static void setShownAddress(Context context, String name, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putString(Preferences.get(SHOWN_ADDRESS, id), name).commit();
	}

	public static String getShownAddress(Context context, int id) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Preferences.get(SHOWN_ADDRESS, id),
				Constants.NOTSET);

	}

	public static void setWeatherData(Context context, String data, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putString(Preferences.get(WEATHER_DATA, id), data).commit();
	}

	public static String getWeatherData(Context context, int id) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Preferences.get(WEATHER_DATA, id),
				Constants.NOTSET);

	}

	public static int getScreenHeight(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getInt(SCREEN_HIEGHT, 800);
	}

	public static void setScreenHeight(Context context, int height) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putInt(SCREEN_HIEGHT, height).commit();
	}

	public static void setLocatedCityLat(Context context, double dlat, int id) {
		float flat = (float) dlat;
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putFloat(Preferences.get(LOCATED_CITY_LAT, id), flat).commit();
	}

	public static float getLocatedCityLat(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getFloat(Preferences.get(LOCATED_CITY_LAT, id), 0);
	}

	public static void setGPSCityLat(Context context, double dlon) {
		float flon = (float) dlon;
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putFloat(GPS_CITY_LAT, flon).commit();
	}

	public static float getGPSCityLat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getFloat(GPS_CITY_LAT, 0);
	}

	public static void setLocatedCityLon(Context context, double dlon, int id) {
		float flon = (float) dlon;
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putFloat(Preferences.get(LOCATED_CITY_LON, id), flon).commit();
	}

	public static float getLocatedCityLon(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getFloat(Preferences.get(LOCATED_CITY_LON, id), 0);
	}

	public static void setGPSCityLon(Context context, double dlon) {
		float flon = (float) dlon;
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putFloat(GPS_CITY_LON, flon).commit();
	}

	public static float getGPSCityLon(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getFloat(GPS_CITY_LON, 0);
	}

	public static void setScreenOnStat(Context context, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(SCREENON_STAT, value).commit();
	}

	public static boolean getScreenOnStat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(SCREENON_STAT, true);
	}

	public static void setLauncherOnStat(Context context, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(LAUNCHER_STAT, value).commit();
	}

	public static boolean getLauncherOnStat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(LAUNCHER_STAT, true);
	}

	public static void setSkipUpdateViewStat(Context context, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(SKIP_STAT, value).commit();
	}

	public static boolean getSkipUpdateViewStat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(SKIP_STAT, false);
	}

	// public static void setSavedCity(Context context, String name, int id) {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit()
	// .putString(Preferences.get(SAVED_CITY, id), name).commit();
	// }
	//
	// public static String getSavedCity(Context context, int id) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getString(Preferences.get(SAVED_CITY, id), "ERROR");
	// }

	public static void setAutoAddressStat(Context context, Boolean value, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putBoolean(Preferences.get(AUTO_ADDRESS, id), value).commit();
	}

	public static Boolean getAutoAddressStat(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(Preferences.get(AUTO_ADDRESS, id), true);
	}

	public static void setNeedInternetUpdateStat(Context context,
												 Boolean value, int id) {
		CommonUtils.l("ID:" + id + " need update internet data: " + value);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putBoolean(Preferences.get(NEED_UPDATEVIEW, id), value)
				.commit();
	}

	public static Boolean getNeedInternetUpdateStat(Context context, int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(Preferences.get(NEED_UPDATEVIEW, id), false);
	}

	// public static void setAutoRefreshStat(Context context, Boolean value, int
	// id) {
	// SharedPreferences sharedPreferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// sharedPreferences.edit().putBoolean(Preferences.get(AUTO_REFRESH, id),
	// value).commit();
	// }
	//
	// public static Boolean getAutoRefreshStat(Context context, int id) {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// return prefs.getBoolean(Preferences.get(AUTO_REFRESH, id), true);
	// }

	public static void setCelsiusStat(Context context, Boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(USE_CELSIUS, value).commit();
	}

	public static Boolean getCelsiusStat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(USE_CELSIUS, false);
	}

	public static void setDataTaskRunning(Context context, Boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(DATA_TASK_STAT, value).commit();
	}

	public static Boolean getDataTaskRunning(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(DATA_TASK_STAT, false);
	}

	public static void set24FormatStat(Context context, Boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(TWENTYFOUR_FORMAT, value).commit();
	}

	public static Boolean get24FormatStat(Context context) {
		if (getSetTimeStat(context)) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			return prefs.getBoolean(TWENTYFOUR_FORMAT, false);
		} else {
			return TaskUtils.get24HourMode(context);
		}
	}

	public static void setSetTimeStat(Context context, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(HAS_SET_TIME, value).commit();
	}

	public static boolean getSetTimeStat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(HAS_SET_TIME, false);
	}

	public static void setWeekInfoStat(Context context, Boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(WEEK_INFO_STAT, value).commit();
	}

	public static Boolean getWeekInfoStat(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(WEEK_INFO_STAT, false);
	}

	public static int getUpdateInterval(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int position = sharedPreferences.getInt(REFRESH_INTERVAL, 3);
		return position;

	}

	public static void setUpdateInterval(Context context, int position) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putInt(REFRESH_INTERVAL, position).commit();
	}

	public static long getUpdateTime(Context context, int id) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getLong(Preferences.get(UPDATE_TIME, id), 0);
	}

	public static void setUpdateTime(Context context, long time, int id) {
		CommonUtils.l(" ID: " + id + "set update time= " + time);
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit()
				.putLong(Preferences.get(UPDATE_TIME, id), time).commit();
	}

	public static int getSavedId(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt(SAVED_Id, 0);
	}

	public static void setSavedId(Context context, int num) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putInt(SAVED_Id, num).commit();
	}

	public static void setClockPackageName(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(CLOCK_PACKAGE_NAME, name).commit();
	}

	public static String getClockPackageName(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(CLOCK_PACKAGE_NAME, Constants.NOTSET);
	}

	public static void setClockActivityName(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(CLOCK_ACTIVITY_NAME, name).commit();
	}

	public static String getClockActivityName(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(CLOCK_ACTIVITY_NAME, Constants.NOTSET);
	}

	public static void setCalendarPackageName(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(CALENDAR_PACKAGE_NAME, name).commit();
	}

	public static String getCalendarPackageName(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(CALENDAR_PACKAGE_NAME, Constants.NOTSET);
	}

	public static void setCalendarActivityName(Context context, String name) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(CALENDAR_ACTIVITY_NAME, name).commit();
	}

	public static String getCalendarActivityName(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(CALENDAR_ACTIVITY_NAME, Constants.NOTSET);
	}
}
