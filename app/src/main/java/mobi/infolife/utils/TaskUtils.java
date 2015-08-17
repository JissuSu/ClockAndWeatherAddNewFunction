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
		// get weather failure, need update again
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
					weather[Constants.TODAY_TEMP_INDEX] = current + "°";
				} else {
					current = gwh.getCurrentFTemp();
					weather[Constants.TODAY_TEMP_INDEX] = current + "°";
				}

				int todayH = Integer.parseInt(gwh.getHighList(0, useCelsius));
				int todayL = Integer.parseInt(gwh.getLowList(0, useCelsius));
				if (current > todayH) {
					todayH = current;
				}
				if (current < todayL) {
					todayL = current;
				}

				weather[Constants.TODAY_LOW_INDEX] = todayL + "°";
				weather[Constants.TODAY_HIGH_INDEX] = todayH + "°";

				weather[Constants.FISRTDAY_LOW_INDEX] = gwh.getLowList(1,
						useCelsius) + "°";
				weather[Constants.SECONDDAY_LOW_INDEX] = gwh.getLowList(2,
						useCelsius) + "°";
				weather[Constants.THIRDDAY_LOW_INDEX] = gwh.getLowList(3,
						useCelsius) + "°";

				weather[Constants.FISRTDAY_HIGH_INDEX] = gwh.getHighList(1,
						useCelsius) + "°";
				weather[Constants.SECONDDAY_HIGH_INDEX] = gwh.getHighList(2,
						useCelsius) + "°";
				weather[Constants.THIRDDAY_HIGH_INDEX] = gwh.getHighList(3,
						useCelsius) + "°";

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

	public static void widgetUpdate(Context context, RemoteViews views,
			int appWidgetId) {
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(appWidgetId, views);
	}

	public static void widgetUpdate(Context context, RemoteViews views,
			int[] appWidgetId) {
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(appWidgetId, views);
	}

}
