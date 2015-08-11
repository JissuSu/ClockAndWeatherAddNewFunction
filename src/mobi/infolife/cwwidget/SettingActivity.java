package mobi.infolife.cwwidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import mobi.infolife.utils.AddressUtils;
import mobi.infolife.utils.BestWeatherManager;
import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.TaskUtils;
import mobi.infolife.utils.YahooCityDataHandler;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingActivity extends ListActivity {
	private boolean fullTimeStat = true;
	private boolean celsiusStat = true;
	private boolean showWeekInfo = true;

	LinearLayout refreshLayout;

	Spinner localeSpinner;

	TextView f_button;
	TextView c_button;

	Context context;
	int appWidgetId;
	ProgressDialog mProgressDialog;
	
	TextView manualLocationTitleText;
	TextView manualLocationDetailText;
	ImageView manualRadio;
	ImageView searchButton;
	LinearLayout manualLayout;
	LinearLayout inputLayout;
	LinearLayout resultLayout;
	boolean isAuto;
	Message msg1;
	Message msg2;
	String city;
	EditText cityText;
	private ArrayList<String> addressList = new ArrayList<String>();
	private ArrayList<String> cityList = new ArrayList<String>();
	private ArrayList<String> stateList = new ArrayList<String>();
	private ArrayList<String> countryList = new ArrayList<String>();
	private ArrayList<String> lonList = new ArrayList<String>();
	private ArrayList<String> latList = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		manualLocationTitleText = (TextView) findViewById(R.id.manual_location_title_text);
		manualLocationDetailText = (TextView) findViewById(R.id.manual_location_detail_text);
		manualRadio = (ImageView) findViewById(R.id.manual_location_radio);
		searchButton = (ImageView) findViewById(R.id.btn);
		manualLayout = (LinearLayout) findViewById(R.id.manual_location_layout);
		inputLayout = (LinearLayout) findViewById(R.id.input_location_layout);
		resultLayout = (LinearLayout) findViewById(R.id.search_result_layout);
		resultLayout.setVisibility(View.GONE);
		
		context = this;

		cityText = (EditText) findViewById(R.id.input);
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(context.getString(R.string.loading_data));
		
		Intent launchIntent = getIntent();
		Bundle extras = launchIntent.getExtras();
		if (extras != null) {
			appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			Preferences.setSavedId(context, appWidgetId);
			manualLayout.setOnClickListener(cl);
			searchButton.setOnClickListener(cl);
			CommonUtils.l("set click done");
			CommonUtils.l("get appWidgetId from launcher = " + appWidgetId);
			Intent cancelResultValue = new Intent();
			cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetId);
			setResult(RESULT_CANCELED, cancelResultValue);
		} else {
			finish();
		}
		isAuto = Preferences.getAutoAddressStat(context, appWidgetId);
		refreshViewBySettingLocation();
		appWidgetId = launchIntent.getExtras().getInt(
				AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);

		fullTimeStat = Preferences.get24FormatStat(context);
		celsiusStat = Preferences.getCelsiusStat(context);
		showWeekInfo = Preferences.getWeekInfoStat(context);

		refreshLayout = (LinearLayout) this
				.findViewById(R.id.refresh_interval_layout);

		f_button = (TextView) this.findViewById(R.id.f_button);
		c_button = (TextView) this.findViewById(R.id.c_button);

		localeSpinner = (Spinner) findViewById(R.id.refresh_spinner);

		refreshLayout.setOnClickListener(buttonListener);
		f_button.setOnClickListener(buttonListener);
		c_button.setOnClickListener(buttonListener);
		localeSpinner.setSelection(Preferences.getUpdateInterval(context));

		localeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Preferences.setUpdateInterval(context, position);

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		refreshViewBySetting();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public static void setScreenHeight(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int height = display.getHeight();
		Preferences.setScreenHeight(context, height);
		CommonUtils.l("Screen height:" + height);
	}

	OnClickListener buttonListener = new OnClickListener() {
		BestWeatherManager f = new BestWeatherManager();

		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.refresh_interval_layout:
				break;

			case R.id.c_button:
				celsiusStat = true;
				refreshButtonStat();

				f.loadWeatherDataFromXml(context, appWidgetId);
				Preferences.setCelsiusStat(context, celsiusStat);
				break;
			case R.id.f_button:
				celsiusStat = false;
				refreshButtonStat();

				f.loadWeatherDataFromXml(context, appWidgetId);
				Preferences.setCelsiusStat(context, celsiusStat);

				break;
			default:
				break;
			}
		}

	};

	void refreshButtonStat() {

		if (celsiusStat) {
			f_button.setTextColor(0xffb7b7b7);
			c_button.setTextColor(0xfffffffe);
			f_button.setBackgroundResource(R.drawable.setting_gray_button);
			c_button.setBackgroundResource(R.drawable.setting_green_button);
		} else {
			f_button.setTextColor(0xfffffffe);
			c_button.setTextColor(0xffb7b7b7);
			f_button.setBackgroundResource(R.drawable.setting_green_button);
			c_button.setBackgroundResource(R.drawable.setting_gray_button);
		}
	}


	void refreshViewBySetting() {
		refreshButtonStat();
	}

	void changeSwitcher1() {
		fullTimeStat = !fullTimeStat;
		Preferences.set24FormatStat(context, fullTimeStat);
		Preferences.setSetTimeStat(context, true);
	}

	void changeSwitcher2() {
		showWeekInfo = !showWeekInfo;
		Preferences.setWeekInfoStat(context, showWeekInfo);
	}

	void pressBack() {
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, resultValue);
		context.startService(new Intent(context, UpdateViewService.class));
		context.startService(new Intent(context, UpdateDataService.class));

		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			pressBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	void refreshViewBySettingLocation() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		if (isAuto) {
			manualLocationTitleText.setTextColor(0xff636363);
			manualLocationDetailText.setTextColor(0xff636363);
			manualRadio.setImageResource(R.drawable.selector_disable);
			inputLayout.setVisibility(View.GONE);

			String autoCity = Preferences.getShownAddress(context, appWidgetId);
			if (!TextUtils.equals(autoCity, Constants.NOTSET))

			manualLocationDetailText.setText(R.string.setting_tap_to_set);

			if (imm != null) {
				imm.hideSoftInputFromWindow(
						cityText.getApplicationWindowToken(), 0);

			}

		} else {
			manualLocationTitleText.setTextColor(0xfffffffe);
			manualLocationDetailText.setTextColor(0xff959595);
			manualRadio.setImageResource(R.drawable.selector_active);
			inputLayout.setVisibility(View.VISIBLE);

			String manualCity = Preferences.getShownAddress(context,
					appWidgetId);
			if (!TextUtils.equals(manualCity, Constants.NOTSET))
				manualLocationDetailText.setText(context
						.getString(R.string.current_city) + manualCity);

			cityText.requestFocus();
			if (imm != null) {

				imm.showSoftInput(cityText, InputMethodManager.HIDE_NOT_ALWAYS);

			}
		}

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		try {
			String key;
			Preferences.setShownAddress(context, city, appWidgetId);

			Preferences.setCityIDById(context, Constants.NOTSET, appWidgetId);
			Preferences.setLocatedCityLat(context,
					Double.parseDouble(latList.get(position)), appWidgetId);
			Preferences.setLocatedCityLon(context,
					Double.parseDouble(lonList.get(position)), appWidgetId);
			Preferences.setLocatedCity(context, cityList.get(position),
					appWidgetId);
			Preferences.setLocatedState(context, stateList.get(position),
					appWidgetId);
			Preferences.setLocatedCountry(context, countryList.get(position),
					appWidgetId);

				mProgressDialog.show();
			dumpWeatherDataThread tt = new dumpWeatherDataThread();
			tt.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OnClickListener cl = new OnClickListener() {
		@Override
		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
			case R.id.btn:
				city = cityText.getText().toString();

				if (city.length() < 1) {
					CommonUtils.showShortToast(context, "Not a valid name.");
				} else {
					if (!mProgressDialog.isShowing())
						mProgressDialog.show();
					getCityListThread tt = new getCityListThread();
					tt.start();
				}
				break;
			case R.id.manual_location_layout:
				isAuto = false;
				CommonUtils.l("manual on click");
				refreshViewBySettingLocation();
				Preferences.setAutoAddressStat(context, isAuto, appWidgetId);
				break;
			default:
				break;
			}

		}
	};

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Constants.DISMISS_PICKCITY_DIALOG_ID:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.cancel();
				break;
			case Constants.FAILURE_ID:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.cancel();
				CommonUtils.showLongToast(context,
						context.getString(R.string.unknow_error_happen));

				break;
			case Constants.GET_WEATHER_DATA_DONE_ID:
				CommonUtils.showShortToast(context,
						context.getString(R.string.get_weather_data_done));
				manualLocationDetailText.setText(context
						.getString(R.string.current_city)
						+ cityText.getText().toString());
				finish();
				break;
			case Constants.GET_CITY_DATA_DONE_ID:
				resultLayout.setVisibility(View.VISIBLE);
				CommonUtils.showShortToast(context,
						context.getString(R.string.get_city_data_done));
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(
							cityText.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

				}
				fillDataToList();

				break;
			case Constants.NO_DATA_ID:
				CommonUtils.showLongToast(context,
						context.getString(R.string.get_data_failure));
				break;
			case Constants.SERVER_NO_RESPONSE_ID:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.cancel();
				CommonUtils.showLongToast(context,
						context.getString(R.string.server_no_response));

				break;
			case Constants.DISMISS_ADDRESS_DIALOG_ID:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.cancel();
				String cityName = Preferences.getLocatedCity(context,
						appWidgetId);

				if (cityName.equals(Constants.NOTSET))
					CommonUtils.showShortToast(context,
							context.getString(R.string.toast_located_failure));
				else {
					CommonUtils.showShortToast(context,
							context.getString(R.string.toast_located_succeed)
									+ cityName);
				}
				break;

			}
		}
	};

	public class getCityListThread extends Thread {
		public void run() {
			try {
				city = cityText.getText().toString();
				CommonUtils.l("try to get data of " + city);
				String fileAddr = TaskUtils.getCityDataFileName(context);
				String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22"
						+ URLEncoder.encode(city.trim()) + "%22";
				HttpGet httpRequest = new HttpGet(url);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 100000);
				HttpConnectionParams.setSoTimeout(httpParams, 100000);

				HttpResponse httpResponse = new DefaultHttpClient(httpParams)
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					msg1 = mHandler
							.obtainMessage(Constants.DISMISS_PICKCITY_DIALOG_ID);
					msg1.sendToTarget();
					TaskUtils.writeInputStringToFile(strResult, fileAddr);
					if (strResult.contains("woeid")
							|| strResult.contains("currentconditions")) {
						msg2 = mHandler
								.obtainMessage(Constants.GET_CITY_DATA_DONE_ID);
						msg2.sendToTarget();
					} else {
						msg2 = mHandler.obtainMessage(Constants.NO_DATA_ID);
						msg2.sendToTarget();
					}
				} else {
					msg1 = mHandler
							.obtainMessage(Constants.SERVER_NO_RESPONSE_ID);
					msg1.sendToTarget();
				}

			} catch (Exception e) {
				e.printStackTrace();
				msg1 = mHandler.obtainMessage(Constants.FAILURE_ID);
				msg1.sendToTarget();
				CommonUtils.l("unknown error");

				e.printStackTrace();
			}
		}
	}

	public class dumpWeatherDataThread extends Thread {
		public void run() {
			try {
				String filePath = TaskUtils.getWeatherDataFileName(context,
						appWidgetId);
				CommonUtils.l("try to get data of " + city);
				String url = new BestWeatherManager().generateUrlLink(context,
						appWidgetId);
				HttpGet httpRequest = new HttpGet(url);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 100000);
				HttpConnectionParams.setSoTimeout(httpParams, 100000);

				HttpResponse httpResponse = new DefaultHttpClient(httpParams)
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					msg1 = mHandler
							.obtainMessage(Constants.DISMISS_PICKCITY_DIALOG_ID);
					msg1.sendToTarget();
					if (strResult.contains("current_condition")
							|| strResult.contains("currentconditions")) {
						TaskUtils.writeInputStringToFile(strResult, filePath);
						context.startService(new Intent(context,
								UpdateViewService.class));
						msg2 = mHandler
								.obtainMessage(Constants.GET_WEATHER_DATA_DONE_ID);
						msg2.sendToTarget();
					} else {
						msg2 = mHandler.obtainMessage(Constants.NO_DATA_ID);
						msg2.sendToTarget();
					}
				} else {
					msg1 = mHandler
							.obtainMessage(Constants.SERVER_NO_RESPONSE_ID);
					msg1.sendToTarget();
				}

			} catch (Exception e) {
				e.printStackTrace();
				msg1 = mHandler.obtainMessage(Constants.FAILURE_ID);
				msg1.sendToTarget();
				CommonUtils.l("unknown error");

				e.printStackTrace();
			}
		}
	}

	public void updateAddressData(final Context context, final int appWidgetId) {

		Thread updateAddressThread = new Thread(null, new Runnable() {
			public void run() {
				Message msg;
				BestWeatherManager f = new BestWeatherManager();
				if (f.downloadWeatherData(context, appWidgetId)) {
					f.loadWeatherDataFromXml(context, appWidgetId);
					msg = mHandler
							.obtainMessage(Constants.DISMISS_ADDRESS_DIALOG_ID);
				} else
					msg = mHandler.obtainMessage(Constants.FAILURE_ID);

				AddressUtils.updateAddress(context, appWidgetId);
				msg.sendToTarget();
			}
		}, "UpdateAddress");
		updateAddressThread.start();
	};

	public void fillDataToList() {
		String fileAddr = TaskUtils.getCityDataFileName(context);
		File file = new File(fileAddr);
		try {
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			YahooCityDataHandler gwh = new YahooCityDataHandler();
			xr.setContentHandler(gwh);
			xr.parse(is);
			int n = gwh.getListSize();
			if (n > 0) {
				addressList.clear();
				countryList.clear();
				cityList.clear();
				stateList.clear();

				int i = 0;
				while (i <= n - 1) {
					String address = gwh.getNameList(i) + "-"
							+ gwh.getAdminList(i) + "-" + gwh.getCountryList(i);
					addressList.add(address);
					countryList.add(gwh.getCountryList(i));
					stateList.add(gwh.getAdminList(i));
					cityList.add(gwh.getNameList(i));
					lonList.add(gwh.getLonList(i));
					latList.add(gwh.getLatList(i));

					i++;
				}
				setListAdapter(new ArrayAdapter<String>(this, R.layout.listrow,
						addressList));
			}
		} catch (Exception e) {
			CommonUtils.l("load city xml error");
			e.printStackTrace();
		}
	}
}
