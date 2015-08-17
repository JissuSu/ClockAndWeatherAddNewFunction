/**
 * 
 */
package mobi.infolife.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import mobi.infolife.limitNumAndAir.XKUtils.DataStore;
import mobi.infolife.limitNumAndAir.XKUtils.PinyinFormat;
import mobi.infolife.limitNumAndAir.XKUtils.PinyinHelper;
import mobi.infolife.cwwidget.AndroidHttpClient;
import mobi.infolife.cwwidget.Preferences;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

public class AddressUtils {

	public static Location getLocation(Context c) {
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) c.getSystemService(serviceName);

		List<String> providers = locationManager.getAllProviders();
		Location location = null;
		for (int i = 0; i < providers.size() - 1; i++) {
			location = locationManager.getLastKnownLocation(providers.get(i));
			CommonUtils.l("Provider" + i + ":" + providers.get(i));
			if (location != null)
				return location;
		}

		return location;
	}

	public static boolean updateAddress(Context context, int appWidgetId) {
		Location location = getLocation(context);
		String cityName;

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			float savedLat = Preferences.getGPSCityLat(context);
			float savedLon = Preferences.getGPSCityLon(context);

			boolean latNotExceedOffside = Math.abs(savedLat - latitude) < 0.01 ? true
					: false;
			boolean lonNotExceedOffside = Math.abs(savedLon - longitude) < 0.01 ? true
					: false;

			CommonUtils.l("	cLA=" + latitude + " cLO=" + longitude);
			CommonUtils.l("	sLA=" + savedLat + " sLO=" + savedLon);

			if (latNotExceedOffside && lonNotExceedOffside) {
				cityName = Preferences.getGPSCity(context);
				CommonUtils.l("in the offside,saved city name=" + cityName);
			} else {
				cityName = getCityFromLatitude(latitude, longitude, context,
						appWidgetId);
			}

		} else {
			CommonUtils.l("locationManager is null");
			cityName = getCityFromIP(context, appWidgetId);
			if (cityName.equals(Constants.NOTSET)) {
				CommonUtils.l("can't get cityName");
				return false;
			}

		}

		if (cityName != null) {
			Preferences.setShownAddress(context, cityName, appWidgetId);
			// Preferences.setCitySearchKeyById(context, cityName, appWidgetId);
			CommonUtils.l("appWidgetId=" + appWidgetId + " get city name"
					+ cityName);
			DataStore.setData(
					context,
					DataStore.CITY_NAME,
					PinyinHelper
							.convertToPinyinString(cityName, "",
									PinyinFormat.WITHOUT_TONE).toLowerCase()
							.replace("'", "")); 

		}

		return true;
		// Preferences.setCurrentCity(context, "error");
	}

	static String getUserAgent() {

		StringBuilder sb = new StringBuilder();
		sb.append("Mozilla/5.0 (Linux; U; Android ");
		sb.append(Build.VERSION.RELEASE);
		sb.append("; ");
		sb.append(Locale.getDefault().toString());
		sb.append("; ");
		sb.append(Build.MODEL);
		sb.append(" Build/");
		sb.append(Build.VERSION.SDK);
		sb.append(")");
		sb.append(" AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		return sb.toString();

	}

	static String getCityFromLatitude(double lat, double lng, Context context,
			int appWidgetId) {
		String city = Constants.NOTSET;
		JSONObject data = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		AndroidHttpClient hc = AndroidHttpClient.newInstance(getUserAgent());

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ lat + "," + lng + "&sensor=false&language="
				+ Locale.getDefault().toString();
		// CommonUtils.l(url);
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse rp = hc.execute(get);
			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = rp.getEntity().getContent();
				in = new BufferedReader(new InputStreamReader(is, "utf-8"));
				String result = in.readLine();
				while (result != null) {
					sb.append(result);
					result = in.readLine();
				}
				in.close();
				if (sb.toString() != null) {
					// CommonUtils.l("cityData:" + sb.toString());
					data = new JSONObject(sb.toString());
				} else {
					CommonUtils.l("data string is null");

				}
			} else {
				CommonUtils.l("Google geo server is not 200");

			}
		} catch (Exception e) {
			e.printStackTrace();
			CommonUtils.l("Connect Google geo server Exception");

		} finally {

			if (hc != null) {
				hc.close();
				hc = null;
			}
		}

		if (data != null) {

			// CommonUtils.l("start prase address data:" + data);

			try {
				JSONArray jArr = new JSONArray();
				jArr = data.getJSONArray("results").getJSONObject(0)
						.getJSONArray("address_components");

				String route = "";
				String sublocality = "";
				String locality = "";
				String state = "";
				String country = "";

				for (int i = 0; i < jArr.length(); i++) {
					JSONObject opt = (JSONObject) jArr.opt(i);

					if (opt.getString("types").toString()
							.contains("\"country\"")) {
						country = opt.getString("long_name");
					}
					if (opt.getString("types").toString()
							.contains("\"administrative_area_level_1\"")) {
						state = opt.getString("long_name");
					}
					if (opt.getString("types").toString()
							.contains("\"locality\"")) {
						locality = opt.getString("long_name");
					}
					if (opt.getString("types").toString()
							.contains("\"sublocality\"")) {
						sublocality = opt.getString("long_name");
					}
					if (opt.getString("types").toString().contains("\"route\"")) {
						route = opt.getString("long_name");
					}
				}
				CommonUtils.l("route:" + route + "-sublocality:" + sublocality
						+ "-locality:" + locality + "-state" + state
						+ "-country" + country);

				String showName = locality;
				if (sublocality.length() > 1)
					showName = sublocality;

				Preferences.setGPSCity(context, locality);
				Preferences.setGPSState(context, state);
				Preferences.setGPSCountry(context, country);
				Preferences.setGPSCityLat(context, lat);
				Preferences.setGPSCityLon(context, lng);

				city = locality;
			} catch (JSONException e) {
				e.printStackTrace();
				city = Constants.NOTSET;
			} finally {
			}
		}
		return city;
	}

	static String getCityFromIP(Context context, int appWidgetId) {
		String city = Constants.NOTSET;
		String country;
		String state;
		double lat;
		double lng;

		JSONObject data = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		AndroidHttpClient hc = AndroidHttpClient
				.newInstance(Constants.USER_AGENT);
		String url = "http://api.ipinfodb.com/v3/ip-city/?key=57afa841d97146c059d6fb4b5ba39903ffaad522e91fa22cca8e98f903db25b4&format=json";
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse rp = hc.execute(get);
			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = rp.getEntity().getContent();
				in = new BufferedReader(new InputStreamReader(is, "utf-8"));
				String result = in.readLine();
				while (result != null) {
					sb.append(result);
					result = in.readLine();
				}
				in.close();
				if (sb.toString() != null) {
					// CommonUtils.l("cityData:" + sb.toString());
					data = new JSONObject(sb.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hc != null) {
				hc.close();
				hc = null;
			}
		}
		if (data != null) {
			try {
				city = data.getString("cityName");
				state = data.getString("regionName");
				country = data.getString("countryName");
				lat = data.getDouble("latitude");
				lng = data.getDouble("longitude");

				Preferences.setGPSCity(context, city);
				Preferences.setGPSState(context, state);
				Preferences.setGPSCountry(context, country);
				Preferences.setGPSCityLat(context, lat);
				Preferences.setGPSCityLon(context, lng);

			} catch (JSONException e) {
				city = Constants.NOTSET;
			}
		}
		return city;
	}

	public static void l(String logString) {
		if (Constants.TLOG)
			Log.i(Constants.TAG, logString);
	}

}
