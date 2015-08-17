package mobi.infolife.limitNumAndAir.XKMain;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import mobi.infolife.limitNumAndAir.XKUtils.DataStore;
import mobi.infolife.limitNumAndAir.XKUtils.MyHttpException;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONException;

import java.io.IOException;

public class XKDownLoadUtil extends IntentService {

	private XKLoadUtil xkLoadUtil;

	public XKDownLoadUtil() {
		super("XKDownLoadUtil");
		xkLoadUtil = new XKLoadUtil(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// 获取当前时间
		Log.d("chenlongbo", "onHandleIntent");
		final DateTime dateTime = new DateTime();
		final String nowTime = getNowTime(dateTime);
		if (intent.getBooleanExtra("ifLoad", false)) {
			// 刷新
			DataStore.setData(this, DataStore.AIR_TIME, getNowTime(dateTime));
			DataStore.setData(this, DataStore.XH_TIME, getNowTime(dateTime));
			String mCityName = DataStore.getData(this, DataStore.CITY_NAME);
			new AsyncTask<String, Void, Void>() {

				@Override
				protected Void doInBackground(String... params) {
					try {
						xkLoadUtil.setAirQuility(params[0]);
						xkLoadUtil.setLimitData();
					} catch (Exception e) {
						e.printStackTrace();
						cancel(true);
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void aVoid) {
					super.onPostExecute(aVoid);
					Log.d("chenlongbo", "两次下载完成");
				}

			}.execute(mCityName);

		} else {
			String[] timeListAir = DataStore.getData(this, DataStore.AIR_TIME)
					.split("-");
			DateTime oldTime = new DateTime(Integer.valueOf(timeListAir[0]),
					Integer.valueOf(timeListAir[1]),
					Integer.valueOf(timeListAir[2]),
					Integer.valueOf(timeListAir[3]),
					Integer.valueOf(timeListAir[4]));
			// if (Hours.hoursBetween(oldTime, dateTime).getHours() >= 1) {
			if (Minutes.minutesBetween(oldTime, dateTime).getMinutes() >= 1) {

				new AsyncTask<Context, Void, Void>() {
					@Override
					protected Void doInBackground(Context... params) {
						try {
							xkLoadUtil.setAirQuility(DataStore.getData(
									params[0], DataStore.CITY_NAME));
							DataStore.setData(getApplicationContext(),
									DataStore.AIR_TIME, nowTime);
						} catch (IOException e) {
							e.printStackTrace();
							Log.d("chenlongbo", "轮询获取空气指数失败");
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void aVoid) {
						super.onPostExecute(aVoid);
						Log.d("chenlongbo", "空气下载完成");
					}

				}.execute(this);

			} else {
				Log.d("chenlongbo", oldTime.toDateTime().toString());
				Log.d("chenlongbo", dateTime.toDateTime().toString());
			}
			String[] timeListLim = DataStore.getData(this, DataStore.XH_TIME)
					.split("-");
			DateTime oldTime3 = new DateTime(Integer.valueOf(timeListLim[0]),
					Integer.valueOf(timeListLim[1]),
					Integer.valueOf(timeListLim[2]),
					Integer.valueOf(timeListLim[3]),
					Integer.valueOf(timeListLim[4]));
			// if (Days.daysBetween(oldTime3, dateTime).getDays() >= 7) {
			if (Minutes.minutesBetween(oldTime3, dateTime).getMinutes() >= 3) {

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						try {
							Log.d("chenlongbo", "下载了限号");
							xkLoadUtil.setLimitData();
							DataStore.setData(getApplicationContext(),
									DataStore.XH_TIME, nowTime);
						} catch (IOException e) {
							e.printStackTrace();
							Log.d("chenlongbo", "轮询处理限号数据，网络连接失败");
						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("chenlongbo", "轮询处理限号数据，json解析失败");
						} catch (MyHttpException e) {
							e.printStackTrace();
							if (e.getErrorCode() == 404
									|| e.getErrorCode() == 400
									|| e.getErrorCode() == 403
									|| e.getErrorCode() == 500) {
								// DataStore.setData(getApplicationContext(),
								// DataStore.CITY_NAME,
								// getNowTime(dateTime.minusDays(6)));
								DataStore.setData(getApplicationContext(),
										DataStore.XH_TIME, nowTime);
							}
						}
						return null;
					}
				}.execute();

			}
		}
	}

	private String getNowTime(DateTime dateTime) {
		int year = dateTime.getYear();
		int month = dateTime.getMonthOfYear();
		int day = dateTime.getDayOfMonth();
		int hour = dateTime.getHourOfDay();
		int minute = dateTime.getMinuteOfHour();
		String nowTime = String.valueOf(year) + "-" + String.valueOf(month)
				+ "-" + String.valueOf(day) + "-" + String.valueOf(hour) + "-"
				+ String.valueOf(minute);
		return nowTime;
	}

}
