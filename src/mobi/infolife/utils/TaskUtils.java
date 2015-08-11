/**
 * 
 */
package mobi.infolife.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mobi.infolife.cwwidget.Preferences;
import mobi.infolife.cwwidget.R;
import mobi.infolife.cwwidget.UpdateViewService;
import mobi.infolife.cwwidget.R.drawable;
import mobi.infolife.cwwidget.R.id;
import mobi.infolife.cwwidget.R.string;
import mobi.infolife.widget.framework.MyAppWidgetManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class TaskUtils {

	public static boolean get24HourMode(final Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}

	public static String getWeatherDataFileName(Context context, int appWidgetId) {
		Locale locale = Locale.getDefault();

		String address = Preferences.getShownAddress(context, appWidgetId);
		String language = locale.getLanguage();
		String path = context.getDir("SWwidget", 0).getAbsolutePath() + "/"
				+ address + "_" + language + ".xml";

		// CommonUtils.l(path);

		return path;
		// return Constants.FOLDER_ADDR + "/" + address + "_" + language +
		// ".xml";
	}

	public static String getCityDataFileName(Context context) {

		String path = context.getDir("SWwidget", 0).getAbsolutePath()
				+ "/city.xml";
		return path;
	}

//	public static void updateTimeView(RemoteViews views, Context context) {
//		Date date = new Date();
// 
// 
//		int hour = date.getHours();
//		if (Preferences.get24FormatStat(context)) {
//			// timeFormate = new SimpleDateFormat("kk:mm");
//			views.setViewVisibility(R.id.msign, View.INVISIBLE);
//		} else {
//			views.setViewVisibility(R.id.msign, View.VISIBLE);
//
//			views.setTextViewText(R.id.msign, "AM");
//
//			if (hour > 12) {
//				hour = hour - 12;
//				views.setTextViewText(R.id.msign, "PM");
//			}
//			// timeFormate = new SimpleDateFormat("hh:mm");
//			// String msign = new SimpleDateFormat("a").format(date);
//			// views.setTextViewText(R.id.msign, msign);
//		}
//		int minute = date.getMinutes();
//
//		int screenHeight = Preferences.getScreenHeight(context);
//		if (screenHeight > 900) {
//			views.setImageViewResource(R.id.clock1,
//					Constants.numberDrawableH[(int) hour / 10]);
//			views.setImageViewResource(R.id.clock2,
//					Constants.numberDrawableH[hour % 10]);
//			views.setImageViewResource(R.id.clock3,
//					Constants.numberDrawableH[(int) minute / 10]);
//			views.setImageViewResource(R.id.clock4,
//					Constants.numberDrawableH[minute % 10]);
//			views.setImageViewResource(R.id.dotdot, R.drawable.dotdot_h);
//		} else if (screenHeight < 500) {
//
//			views.setImageViewResource(R.id.clock1,
//					Constants.numberDrawableM[(int) hour / 10]);
//			views.setImageViewResource(R.id.clock2,
//					Constants.numberDrawableM[hour % 10]);
//			views.setImageViewResource(R.id.clock3,
//					Constants.numberDrawableM[(int) minute / 10]);
//			views.setImageViewResource(R.id.clock4,
//					Constants.numberDrawableM[minute % 10]);
//			views.setImageViewResource(R.id.dotdot, R.drawable.dotdot_m);
//		} else {
//			views.setImageViewResource(R.id.clock1,
//					Constants.numberDrawable[(int) hour / 10]);
//			views.setImageViewResource(R.id.clock2,
//					Constants.numberDrawable[hour % 10]);
//			views.setImageViewResource(R.id.clock3,
//					Constants.numberDrawable[(int) minute / 10]);
//			views.setImageViewResource(R.id.clock4,
//					Constants.numberDrawable[minute % 10]);
//			views.setImageViewResource(R.id.dotdot, R.drawable.dotdot);
//		}
//
//		updateWeekNumView(views, context);
//		// String timeString = timeFormate.format(date);
//		String dateString = String.format(
//				context.getString(R.string.date_formater), date, date);
//		String weekdayString = String.format("%ta", date);
//
//		views.setTextViewText(R.id.weekday, weekdayString);
//		// views.setTextViewText(R.id.clock, timeString);
//		views.setTextViewText(R.id.date, dateString);
//
//	}
//
//	public static void updateFullWeatherView(RemoteViews views,
//			String[] weatherString) {
//
//		views.setTextViewText(R.id.condition,
//				weatherString[Constants.TODAY_CONDITION_INDEX]);
//		views.setTextViewText(R.id.temp,
//				weatherString[Constants.TODAY_TEMP_INDEX]);
//		views.setTextViewText(R.id.low, "L:"
//				+ weatherString[Constants.TODAY_LOW_INDEX]);
//		views.setTextViewText(R.id.high, "H:"
//				+ weatherString[Constants.TODAY_HIGH_INDEX]);
//
//		views.setImageViewResource(R.id.todayimage, TaskUtils
//				.getWeatherImageId(weatherString[Constants.TODAY_ICON_INDEX],
//						false));
//	}
//
//	public static void updateWeekNumView(RemoteViews views, Context context) {
//		if (Preferences.getWeekInfoStat(context)) {
//			views.setTextViewText(R.id.weeknum, " " + CommonUtils.getWeekNum());
//			views.setViewVisibility(R.id.weeknum, View.VISIBLE);
//		} else {
//			views.setViewVisibility(R.id.weeknum, View.GONE);
//		}
//
//	}

	public static void updateWeatherInternetDataInThread(final Context context,
			final int appWidgetId) {
		Thread loadListDataThread = new Thread(null, new Runnable() {
			public void run() {
				updateWeatherInternetDataNotInThread(context, appWidgetId);
			}
		}, "UpdateWeatherData");
		loadListDataThread.start();
	}

	public static void updateWeatherInternetDataNotInThread(
			final Context context, final int appWidgetId) {

		boolean needUpdateAgain = false;
		boolean AutoGetAddress = Preferences.getAutoAddressStat(context,
				appWidgetId);

		if (Preferences.getShownAddress(context, appWidgetId).equals(
				Constants.NOTSET)) {
			AutoGetAddress = true;
			Preferences.setAutoAddressStat(context, true, appWidgetId);
		}
		// get address failure, need update again
		if (AutoGetAddress) {
			if (!AddressUtils.updateAddress(context, appWidgetId))
				needUpdateAgain = true;
		}
		// get weather failure, need update again¡¢
		BestWeatherManager f = new BestWeatherManager();
		if (!f.downloadWeatherData(context, appWidgetId))

			needUpdateAgain = true;
		else
			f.loadWeatherDataFromXml(context, appWidgetId);

		// update ok , set update time
		if (!needUpdateAgain) {
			Preferences.setUpdateTime(context, System.currentTimeMillis(),
					appWidgetId);
			context.startService(new Intent(context, UpdateViewService.class));
		}

		Preferences.setNeedInternetUpdateStat(context, needUpdateAgain,
				appWidgetId);

	}

	// public static boolean dumpWeatherData(Context context, int appWidgetId) {
	//
	// String cityKey = Preferences.getCitySearchKeyById(context, appWidgetId);
	// String hl = Preferences.getCurrentCL(context);
	// String key = getKey();
	//
	// // String urlPrefix = "http://www.google.com/ig/api?weather=";
	// String fileAddr = getWeatherDataFileName(context, appWidgetId);
	// String urlPrefix =
	// "http://free.worldweatheronline.com/feed/weather.ashx?format=xml&num_of_days=4&key="
	// + key + "&q=";
	// String url = "";
	//
	// Location location = AddressUtils.getLocation(context);
	// if (Preferences.getAutoAddressStat(context, appWidgetId)
	// && (location != null)) {
	// url = urlPrefix + (long) (location.getLatitude()) + ","
	// + (long) (location.getLongitude());
	// }
	//
	// try {
	// if (!url.startsWith(urlPrefix))
	// url = urlPrefix + URLEncoder.encode(cityKey);
	//
	// CommonUtils.l("appWidgetId=" + appWidgetId + " get data from url="
	// + url);
	// TaskUtils.dumpHttpDataToFile(url, fileAddr, hl);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }

	public static boolean dumpCityData(Context context, String city) {

		// String urlPrefix = "http://www.google.com/ig/api?weather=";
		String fileAddr = getCityDataFileName(context);
		String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22"
				+ URLEncoder.encode(city.trim()) + "%22";
		try {

			CommonUtils.l("get city list for " + city);
			String data = TaskUtils.dumpHttpDataToString(url, null);
			if (data != Constants.NOTSET) {
				TaskUtils.writeInputStringToFile(data, fileAddr);
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static String getKey() {
		int n = new Date().getMinutes() % Constants.keypool.length;
		return Constants.keypool[n];

	}

	static String dumpHttpDataToString(String url, String language) {
		if (url == null) {
			return Constants.NOTSET;
		}
		//CommonUtils.l("Dump Data Of:" + url);
		String data = Constants.NOTSET;
		HttpGet httpRequest = new HttpGet(url);

		if (language != null)
			httpRequest.setHeader("Accept-Language", language + "q=0.5");

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 20000);
		try {
			HttpResponse httpResponse = new DefaultHttpClient(httpParams)
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				data = EntityUtils.toString(httpResponse.getEntity());
				CommonUtils.l("get data ok, prepare dump to local");

				return data;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpRequest.abort();
		}
		return data;

	}

	public static void writeInputStringToFile(String in, String localpath) {
		OutputStream out = null;
		CommonUtils.l("save file to " + localpath);

		try {
			out = new BufferedOutputStream(new FileOutputStream(localpath));
			out.write(in.getBytes("utf-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String[] loadGWeatherDataFromXml(Context c, String fileAddr,
			int appWidgetId) {
		// Utils.l(fileAddr);
		File file = new File(fileAddr);
		CommonUtils.l("load file from:" + fileAddr);
		String[] weather = new String[20];
		boolean useCelsius = Preferences.getCelsiusStat(c);
		// InputSource xml = new InputSource(file);
		// InputStream xml = new FileInputStream(file);
		try {
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			GoogleWeatherHandler gwh = new GoogleWeatherHandler();
			xr.setContentHandler(gwh);
			xr.parse(is);

			if (gwh.getXmlValidStat()) {
				Preferences.setNeedInternetUpdateStat(c, true, appWidgetId);
				return null;
			} else {
				weather[Constants.TODAY_CONDITION_INDEX] = gwh
						.getCurrentCondition();
				weather[Constants.TODAY_WIND_INDEX] = gwh.getCurrentWind();
				weather[Constants.TODAY_HUMIDITY_INDEX] = gwh.getCurrentHum();

				int current;
				if (Preferences.getCelsiusStat(c)) {
					current = gwh.getCurrentCTemp();
					weather[Constants.TODAY_TEMP_INDEX] = current + "¡ã";
				} else {
					current = gwh.getCurrentFTemp();
					weather[Constants.TODAY_TEMP_INDEX] = current + "¡ã";
				}

				int todayH = Integer.parseInt(gwh.getHighList(0, useCelsius));
				int todayL = Integer.parseInt(gwh.getLowList(0, useCelsius));
				if (current > todayH) {
					todayH = current;
				}
				if (current < todayL) {
					todayL = current;
				}

				weather[Constants.TODAY_LOW_INDEX] = todayL + "¡ã";
				weather[Constants.TODAY_HIGH_INDEX] = todayH + "¡ã";

				weather[Constants.FISRTDAY_LOW_INDEX] = gwh.getLowList(1,
						useCelsius) + "¡ã";
				weather[Constants.SECONDDAY_LOW_INDEX] = gwh.getLowList(2,
						useCelsius) + "¡ã";
				weather[Constants.THIRDDAY_LOW_INDEX] = gwh.getLowList(3,
						useCelsius) + "¡ã";

				weather[Constants.FISRTDAY_HIGH_INDEX] = gwh.getHighList(1,
						useCelsius) + "¡ã";
				weather[Constants.SECONDDAY_HIGH_INDEX] = gwh.getHighList(2,
						useCelsius) + "¡ã";
				weather[Constants.THIRDDAY_HIGH_INDEX] = gwh.getHighList(3,
						useCelsius) + "¡ã";

				weather[Constants.FISRTDAY_NAME_INDEX] = gwh.getdayList(1);
				weather[Constants.SECONDDAY_NAME_INDEX] = gwh.getdayList(2);
				weather[Constants.THIRDDAY_NAME_INDEX] = gwh.getdayList(3);

				weather[Constants.TODAY_ICON_INDEX] = gwh.getIconURL();
				weather[Constants.FIRSTDAY_ICON_INDEX] = gwh.getIconList(1);
				weather[Constants.SECONDDAY_ICON_INDEX] = gwh.getIconList(2);
				weather[Constants.THIRDDAY_ICON_INDEX] = gwh.getIconList(3);
			}
			// Utils.l("weather String[]=" + weather.toString());
			return weather;
		} catch (Exception e) {
			CommonUtils.l("load xml error");
			e.printStackTrace();
			return null;
		}
	}

	// public static String[] loadWeatherDataFromXml1(Context c, String
	// fileAddr,
	// int appWidgetId) {
	// TaskUtils.l(fileAddr);
	// File file = new File(fileAddr);
	// String[] weather = new String[4];
	// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	// try {
	// DocumentBuilder builder = factory.newDocumentBuilder();
	// Document doc = builder.parse(file);
	// NodeList nodelist1 = (NodeList) doc
	// .getElementsByTagName("current_conditions");
	// NodeList nodelist2 = nodelist1.item(0).getChildNodes();
	// weather[0] = nodelist2.item(0).getAttributes().item(0)
	// .getNodeValue();
	// // Utils.l("t=" + weather[0]);
	// if (Preferences.getCelsiusStat(c, appWidgetId)) {
	// weather[1] = nodelist2.item(2).getAttributes().item(0)
	// .getNodeValue()
	// + "¡æ";
	// l("appWidgetId:" + appWidgetId + "use ¡æ");
	// } else {
	// weather[1] = nodelist2.item(1).getAttributes().item(0)
	// .getNodeValue()
	// + "¨H";
	// l("appWidgetId:" + appWidgetId + "use ¨H");
	//
	// }
	// // Utils.l("t=" + weather[1]);
	//
	// weather[2] = nodelist2.item(3).getAttributes().item(0)
	// .getNodeValue();
	// // Utils.l("t=" + weather[2]);
	//
	// weather[3] = nodelist2.item(5).getAttributes().item(0)
	// .getNodeValue();
	// // Utils.l("t=" + weather[3]);
	//
	// return weather;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	// public static void l(String logString) {
	// if (Constants.TLOG)
	// Log.i(Constants.TAG, logString);
	// }

//	public static int getWeatherImageId(String iconUrl, boolean alwaysLight) {
//		// CommonUtils.l("icon=" + iconUrl);
//		Date date = new Date();
//		//CommonUtils.l("ICONURL" + iconUrl);
//		boolean ISLIGHT = true;
//		int hour = date.getHours();
//		if (hour > 18 || hour < 6)
//			ISLIGHT = false;
//		if (alwaysLight)
//			ISLIGHT = true;
//		if (iconUrl != null) {
//			if (iconUrl.contains("sunny") || iconUrl.contains("mostly_sunny")
//					|| iconUrl.contains("clear")
//					|| TextUtils.equals(iconUrl.trim(), "01")
//					|| TextUtils.equals(iconUrl.trim(), "33")
//					|| TextUtils.equals(iconUrl.trim(), "30")
//					|| TextUtils.equals(iconUrl.trim(), "02")
//					|| TextUtils.equals(iconUrl.trim(), "34")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_CLEAR_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_CLEAR_DARK];
//			} else if (iconUrl.contains("rain") || iconUrl.contains("shower")
//					|| iconUrl.contains("lightrain")
//					|| TextUtils.equals(iconUrl.trim(), "12")
//					|| TextUtils.equals(iconUrl.trim(), "13")
//					|| TextUtils.equals(iconUrl.trim(), "14")
//					|| TextUtils.equals(iconUrl.trim(), "39")
//					|| TextUtils.equals(iconUrl.trim(), "40")
//					|| TextUtils.equals(iconUrl.trim(), "26")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_SMALL_RAIN_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_SMALL_RAIN_DARK];
//			} else if (iconUrl.contains("heavyrain")
//					|| TextUtils.equals(iconUrl.trim(), "18")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_BIG_RAIN_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_BIG_RAIN_DARK];
//			} else if (iconUrl.contains("fog") || iconUrl.contains("smoke")
//					|| iconUrl.contains("mist")
//					|| TextUtils.equals(iconUrl.trim(), "05")
//					|| TextUtils.equals(iconUrl.trim(), "37")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_FOG_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_FOG_DARK];
//			} else if (iconUrl.contains("thunderstorm")
//					|| iconUrl.contains("storm")
//					|| TextUtils.equals(iconUrl.trim(), "15")
//					|| TextUtils.equals(iconUrl.trim(), "16")
//					|| TextUtils.equals(iconUrl.trim(), "17")
//					|| TextUtils.equals(iconUrl.trim(), "41")
//					|| TextUtils.equals(iconUrl.trim(), "42")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_THUNDER_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_THUNDER_DARK];
//			} else if (iconUrl.contains("haze")
//					|| TextUtils.equals(iconUrl.trim(), "11")) {
//				return Constants.weatherDrawable[Constants.INDEX_HAZE];
//			} else if (iconUrl.contains("hail")
//
//			|| TextUtils.equals(iconUrl.trim(), "25")) {
//				return Constants.weatherDrawable[Constants.INDEX_HAIL];
//			} else if (iconUrl.contains("flurries")
//					|| TextUtils.equals(iconUrl.trim(), "19")
//					|| TextUtils.equals(iconUrl.trim(), "20")
//					|| TextUtils.equals(iconUrl.trim(), "43")) {
//				return Constants.weatherDrawable[Constants.INDEX_SMALL_SNOW];
//			} else if (iconUrl.contains("chance_of_snow")
//					|| iconUrl.contains("snow")
//					|| TextUtils.equals(iconUrl.trim(), "23")
//					|| TextUtils.equals(iconUrl.trim(), "21")
//					|| TextUtils.equals(iconUrl.trim(), "22")
//					|| TextUtils.equals(iconUrl.trim(), "44")) {
//				return Constants.weatherDrawable[Constants.INDEX_SNOW];
//
//			} else if (iconUrl.contains("sleet")
//					|| TextUtils.equals(iconUrl.trim(), "29")) {
//				return Constants.weatherDrawable[Constants.INDEX_SNOW_RAIN];
//
//			} else if (iconUrl.contains("dust")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_DUST_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_DUST_DARK];
//
//			} else if (iconUrl.contains("icy")
//					|| TextUtils.equals(iconUrl.trim(), "24")
//					|| TextUtils.equals(iconUrl.trim(), "31")) {
//				return Constants.weatherDrawable[Constants.INDEX_ICY];
//
//			} else if (iconUrl.contains("wind")
//					|| TextUtils.equals(iconUrl.trim(), "32")) {
//				return Constants.weatherDrawable[Constants.INDEX_WIND];
//
//			} else if (iconUrl.contains("cloud")
//					|| iconUrl.contains("overcast")
//					|| TextUtils.equals(iconUrl.trim(), "03")
//					|| TextUtils.equals(iconUrl.trim(), "04")
//					|| TextUtils.equals(iconUrl.trim(), "06")
//					|| TextUtils.equals(iconUrl.trim(), "07")
//					|| TextUtils.equals(iconUrl.trim(), "08")
//					|| TextUtils.equals(iconUrl.trim(), "35")
//					|| TextUtils.equals(iconUrl.trim(), "36")
//					|| TextUtils.equals(iconUrl.trim(), "38")) {
//				if (ISLIGHT)
//					return Constants.weatherDrawable[Constants.INDEX_PART_CLOUD_LIGHT];
//				else
//					return Constants.weatherDrawable[Constants.INDEX_PART_CLOUD_DARK];
//			}
//		}
//
//		return Constants.weatherDrawable[Constants.INDEX_UNKNOW];
//
//	}

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

}
