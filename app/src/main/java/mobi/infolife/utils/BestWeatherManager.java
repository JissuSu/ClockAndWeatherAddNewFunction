package mobi.infolife.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import mobi.infolife.cwwidget.Preferences;
import mobi.infolife.cwwidget.R;
import android.content.Context;
import android.text.TextUtils;

public class BestWeatherManager extends BaseWeatherManager {

	@Override
	public String generateUrlLink(Context context, int appWidgetId) {
		boolean GPSMODE = Preferences.getAutoAddressStat(context, appWidgetId);
		float lat, lon;
		String langId = context.getString(R.string.lang_id);
		String gpsUrlPrefix = "http://htc2.accu-weather.com/widget/htc2/weather-data.asp?langid="
				+ langId;
		String nameUrlPrefix = "http://htc2.accu-weather.com/widget/htc2/weather-data.asp?metric=0&langid="
				+ langId + "&location=";

		if (GPSMODE) {
			lat = Preferences.getGPSCityLat(context);
			lon = Preferences.getGPSCityLon(context);
			return gpsUrlPrefix + "&slat=" + lat + "&slon=" + lon;
		} else {
			lat = Preferences.getLocatedCityLat(context, appWidgetId);
			lon = Preferences.getLocatedCityLon(context, appWidgetId);
			return gpsUrlPrefix + "&slat=" + lat + "&slon=" + lon;

		}

	}

	public boolean loadWeatherDataFromXml(Context c, int appWidgetId) {
		// Utils.l(fileAddr);\
		String fileAddr = TaskUtils.getWeatherDataFileName(c, appWidgetId);
		File file = new File(fileAddr);
		CommonUtils.l("load weather data file path:" + fileAddr);
		String[] weather = new String[20];


		try {
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			boolean useCelsius = Preferences.getCelsiusStat(c);

			BestWeatherHandler gwh = new BestWeatherHandler(useCelsius);
			xr.setContentHandler(gwh);
			xr.parse(is);

			if (gwh.getXmlValidStat()) {
				Preferences.setNeedInternetUpdateStat(c, true, appWidgetId);
				return false;
			} else {
				weather[Constants.TODAY_CONDITION_INDEX] = gwh
						.getCurrentCondition();
				weather[Constants.TODAY_WIND_INDEX] = c
						.getString(R.string.view_wind)
						+ " "
						+ gwh.getCurrentWind();
				weather[Constants.TODAY_HUMIDITY_INDEX] = c
						.getString(R.string.view_humidity)
						+ " "
						+ gwh.getCurrentHum();

				String current;

				current = gwh.getCurrentTemp();
				weather[Constants.TODAY_TEMP_INDEX] = current + "°";

				String todayH = gwh.getHighList(0);
				String todayL = gwh.getLowList(0);

				weather[Constants.TODAY_LOW_INDEX] = todayL + "°";
				weather[Constants.TODAY_HIGH_INDEX] = todayH + "°";

				weather[Constants.FISRTDAY_LOW_INDEX] = gwh.getLowList(1) + "°";
				weather[Constants.SECONDDAY_LOW_INDEX] = gwh.getLowList(2)
						+ "°";
				weather[Constants.THIRDDAY_LOW_INDEX] = gwh.getLowList(3) + "°";

				weather[Constants.FISRTDAY_HIGH_INDEX] = gwh.getHighList(1)
						+ "°";
				weather[Constants.SECONDDAY_HIGH_INDEX] = gwh.getHighList(2)
						+ "°";
				weather[Constants.THIRDDAY_HIGH_INDEX] = gwh.getHighList(3)
						+ "°";

				weather[Constants.FISRTDAY_NAME_INDEX] = CommonUtils
						.getWeedDayByOffSet(1);
				weather[Constants.SECONDDAY_NAME_INDEX] = CommonUtils
						.getWeedDayByOffSet(2);
				weather[Constants.THIRDDAY_NAME_INDEX] = CommonUtils
						.getWeedDayByOffSet(3);

				weather[Constants.TODAY_ICON_INDEX] = gwh.getIconList(0);
				weather[Constants.FIRSTDAY_ICON_INDEX] = gwh.getIconList(2);
				weather[Constants.SECONDDAY_ICON_INDEX] = gwh.getIconList(3);
				weather[Constants.THIRDDAY_ICON_INDEX] = gwh.getIconList(4);
			}

			String data = CommonUtils.stringArrayToString(weather);
			Preferences.setWeatherData(c, data, appWidgetId);
			// CommonUtils.l(appWidgetId+" saved weather data is"+data);

			// Utils.l("weather String[]=" + weather.toString());
			return true;
		} catch (Exception e) {
			CommonUtils.l("load xml error");
			e.printStackTrace();
			return false;
		}
	}

}
