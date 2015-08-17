package mobi.infolife.limitNumAndAir.XKMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mobi.infolife.limitNumAndAir.XKModel.AirData;
import mobi.infolife.limitNumAndAir.XKModel.LimitCityData;
import mobi.infolife.limitNumAndAir.XKModel.LimitTodayNum;
import mobi.infolife.limitNumAndAir.XKUtils.DataStore;
import mobi.infolife.limitNumAndAir.XKUtils.MyHttpException;
import mobi.infolife.limitNumAndAir.XKUtils.PinyinFormat;
import mobi.infolife.limitNumAndAir.XKUtils.PinyinHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * Created by longlong on 15-8-5.
 */
public class XKLoadUtil {

	private Context mContext;

	private static final String url = "http://aqicn.org/city/";
	private static final String url_x = "http://wz.qichecdn.com/ashx/LimitNumsConfig.ashx?&platform=1&version=3.3.1"; // 鑾峰彇闄愬彿鏁版嵁

	public XKLoadUtil(Context context) {
		this.mContext = context;
	}

	public void setAirQuility(String cityname) throws IOException {

		String mCityNamePinyin = PinyinHelper.convertToPinyinString(cityname,
				"", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
		// String finalUrl = url + mCityNamePinyin + "/cn";

		String finalUrl = url + cityname + "/cn/m/";

		ArrayList<String> mAirList = new ArrayList<String>();

		Log.d("chenlongbo", finalUrl);

		try {
			Document doc = Jsoup.connect(finalUrl).timeout(6000).get();
			Element result = doc.getElementById("xatzcaqv");
			Elements result2 = doc.getElementsByClass("aqimsg");

			if (result.attr("title") != null && result.text() != null) {
				mAirList.add(result.text());
				mAirList.add(result2.get(0).text());
			} else {
				mAirList.add("暂无");
				mAirList.add("暂无");
			}
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				mAirList.add("暂无");
				mAirList.add("暂无");
			}
		}

		List<AirData> airDatalist = new Select().from(AirData.class)
				.where("AD_CITYNAME = ?", cityname).execute();

		Log.d("chenlongbo", "空气质量队列数字：" + airDatalist.size());
		AirData airData = null;
		if (airDatalist.size() > 0) {
			Log.d("henlong", "success");
			Log.d("henlong", mAirList.get(0).toString());
			airData = airDatalist.get(0);
			airData.airnum = mAirList.get(0);
			airData.airstatus = mAirList.get(1);
			airData.save();
		} else {
			Log.d("henlong", "failed");
			airData = new AirData(cityname, mAirList.get(1), mAirList.get(0));
			airData.save();
		}

	}

	public void setLimitData() throws IOException, JSONException,
			MyHttpException {

		URL httpUrl = new URL(url_x);
		HttpURLConnection connection = (HttpURLConnection) httpUrl
				.openConnection();
		connection.setReadTimeout(6000);
		connection.setRequestMethod("GET");
		if (connection.getResponseCode() != 200) {
			throw new MyHttpException(connection.getResponseCode());
		}
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String str;
		while ((str = bufferedReader.readLine()) != null) {
			stringBuffer.append(str);
		}
		JSONObject response = new JSONObject(String.valueOf(stringBuffer));
		String oldTime = DataStore.getData(mContext, DataStore.SETTING_XH);
		Log.d("chenlongbo", "old时间戳" + oldTime);
		String newTime = response.getJSONObject("result").getString(
				"limitnums_timestamp");
		Log.d("chenlongbo", "new时间戳" + newTime);
		if (oldTime == null) {
			Log.d("chenlongbo", "oldtime为空");
			putInData(response);
			DataStore.setData(mContext, DataStore.SETTING_XH, newTime);
		} else if (!oldTime.equals(newTime)) {
			Log.d("chenlongbo", "时间戳更新");
			updateData(response);
			DataStore.setData(mContext, DataStore.SETTING_XH, newTime);
		}

	}

	private void putInData(JSONObject response) {
		JSONArray jsonArray = null;
		try {
			jsonArray = response.getJSONObject("result").getJSONArray("items");
			ActiveAndroid.beginTransaction();
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					LimitCityData limitCityData = new LimitCityData();// (obj.getString("cityid"),
																		// obj.getString("cityname"),
																		// obj.getString("limittimearea"),
																		// obj.getString("limitplatenum"),
																		// obj.getString("limitother"),
																		// obj.getString("limitinfo"),
																		// obj.getString("limitareaimg"));
					limitCityData.id = obj.getString("cityid");
					String name = PinyinHelper.convertToPinyinString(
							obj.getString("cityname"), "",
							PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
					limitCityData.name = name;
					limitCityData.limittimearea = obj
							.getString("limittimearea");
					limitCityData.limitplatenum = obj
							.getString("limitplatenum");
					limitCityData.limitother = obj.getString("limitother");
					limitCityData.limitinfo = obj.getString("limitinfo");
					limitCityData.limitareaimg = obj.getString("limitareaimg");
					limitCityData.save();
				}
				ActiveAndroid.setTransactionSuccessful();
			} finally {
				ActiveAndroid.endTransaction();
			}
			ActiveAndroid.beginTransaction();
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj1 = jsonArray.getJSONObject(i);
					JSONArray jsonArray1 = obj1.getJSONArray("limit");
					for (int j = 0; j < jsonArray1.length(); j++) {
						JSONObject obj2 = jsonArray1.getJSONObject(j);
						LimitTodayNum limitTodayNum = new LimitTodayNum();
						String name = PinyinHelper.convertToPinyinString(
								obj1.getString("cityname"), "",
								PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
						// limitTodayNum.name = obj1.getString("cityname");
						limitTodayNum.name = name;
						limitTodayNum.id = obj1.getString("cityid");
						limitTodayNum.date = obj2.getString("date");
						limitTodayNum.nums = obj2.getString("nums");
						limitTodayNum.save();
						// Log.d("chenlongbo", obj1.getString("cityname"));
						// Log.d("chenlongbo", obj1.getString("cityid"));
						// Log.d("chenlongbo", obj2.getString("date"));
						// Log.d("chenlongbo", obj2.getString("nums"));
					}
				}
				ActiveAndroid.setTransactionSuccessful();
			} finally {
				ActiveAndroid.endTransaction();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updateData(JSONObject response) {
		new Delete().from(LimitCityData.class).execute();
		new Delete().from(LimitTodayNum.class).execute();
		putInData(response);
	}
}
