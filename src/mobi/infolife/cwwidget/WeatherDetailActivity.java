package mobi.infolife.cwwidget;

import com.google.analytics.tracking.android.EasyTracker;

import mobi.infolife.utils.AddressUtils;
import mobi.infolife.utils.BestWeatherManager;
import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.TaskUtils;
import mobi.infolife.utils.ViewUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WeatherDetailActivity extends Activity {
	static final String MY_LAUNCHER_PACKAGE_NAME = "mobi.infolife.launcher2";
	int appWidgetId;
	String type;
	Context context;
	ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
		setContentView(R.layout.detail);
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		context = this;
		appWidgetId = this
				.getIntent()
				.getExtras()
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
		type = this.getIntent().getExtras()
				.getString(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER);
		if (Preferences.getShownAddress(context, appWidgetId)
				.equals(Constants.NOTSET)){
			updateWeather();
		}
	}

	public void onResume() {
		super.onResume();
		setView();

	}

	public void onPause() {
		super.onPause();
		RemoteViews rv;
		if (TextUtils.equals(type, Constants.WEATHER)) {
			rv = ViewUtils.getWeatherRemoteviews(context, appWidgetId);
			TaskUtils.widgetUpdate(context, rv, appWidgetId);
		}

		if (TextUtils.equals(type, Constants.FULL)) {
			rv = ViewUtils.getFullRemoteviews(context, appWidgetId);
			TaskUtils.widgetUpdate(context, rv, appWidgetId);
		}

	}

	void setView() {
		// boolean sdCardMounted = CommonUtils.isSDCardMounted();

		ImageView setting_button = (ImageView) findViewById(R.id.settings);
		ImageView refresh_button = (ImageView) findViewById(R.id.refresh);
		LinearLayout parentLayout = (LinearLayout) findViewById(R.id.linearlayoutParent);
		LinearLayout detailLayout = (LinearLayout) findViewById(R.id.detailLayout);

		setting_button.setOnClickListener(buttonListener);
		refresh_button.setOnClickListener(buttonListener);
		detailLayout.setOnClickListener(buttonListener);
		parentLayout.setOnClickListener(buttonListener);

		TextView address = (TextView) findViewById(R.id.address);
		String addressString = Preferences
				.getShownAddress(context, appWidgetId);
		address.setText(addressString);

		BestWeatherManager f = new BestWeatherManager();
		String weatherString[] = f.parseWeatherData(context, appWidgetId);
		if (weatherString != null) {
			CommonUtils.l("update detail weather activity view");
			setViewData(weatherString);
		} else {
			Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
			CommonUtils.l("weather string is null");
		}

	}

	void setViewData(String[] ws) {
		TextView today_low_high = (TextView) findViewById(R.id.today_low_high);
		TextView today_temp = (TextView) findViewById(R.id.today_temp);
		TextView today_condition = (TextView) findViewById(R.id.condition);
		TextView today_humidity = (TextView) findViewById(R.id.humidity);
		TextView today_wind = (TextView) findViewById(R.id.wind);
		// TextView date = (TextView) findViewById(R.id.date);
		TextView updatetime = (TextView) findViewById(R.id.updatetime);
		TextView day_one = (TextView) findViewById(R.id.day_one);
		TextView day_two = (TextView) findViewById(R.id.day_two);
		TextView day_three = (TextView) findViewById(R.id.day_three);
		TextView temp_one = (TextView) findViewById(R.id.temp_one);
		TextView temp_two = (TextView) findViewById(R.id.temp_two);
		TextView temp_three = (TextView) findViewById(R.id.temp_three);

		ImageView today_image = (ImageView) findViewById(R.id.today_image);
		ImageView image_one = (ImageView) findViewById(R.id.image_one);
		ImageView image_two = (ImageView) findViewById(R.id.image_two);
		ImageView image_three = (ImageView) findViewById(R.id.image_three);

		// Date nowDate = new Date();
		// String dateString = String.format(
		// context.getString(R.string.date_formater), nowDate, nowDate);

		updatetime.setText(context.getString(R.string.update)
				+ "  "
				+ CommonUtils.format24Date(Preferences.getUpdateTime(context,
						appWidgetId)));

		// date.setText(dateString);

		today_temp.setText(ws[Constants.TODAY_TEMP_INDEX]);
		today_low_high.setText("H:" + ws[Constants.TODAY_HIGH_INDEX] + "   L:"
				+ ws[Constants.TODAY_LOW_INDEX]);
		today_condition.setText(ws[Constants.TODAY_CONDITION_INDEX]);
		today_humidity.setText(ws[Constants.TODAY_HUMIDITY_INDEX]);
		today_wind.setText(ws[Constants.TODAY_WIND_INDEX]);
		day_one.setText(ws[Constants.FISRTDAY_NAME_INDEX]);
		day_two.setText(ws[Constants.SECONDDAY_NAME_INDEX]);
		day_three.setText(ws[Constants.THIRDDAY_NAME_INDEX]);
		temp_one.setText(ws[Constants.FISRTDAY_LOW_INDEX] + "/"
				+ ws[Constants.FISRTDAY_HIGH_INDEX]);
		temp_two.setText(ws[Constants.SECONDDAY_LOW_INDEX] + "/"
				+ ws[Constants.SECONDDAY_HIGH_INDEX]);
		temp_three.setText(ws[Constants.THIRDDAY_LOW_INDEX] + "/"
				+ ws[Constants.THIRDDAY_HIGH_INDEX]);

		today_image.setImageResource(ViewUtils.getWeatherImageId(
				ws[Constants.TODAY_ICON_INDEX], false));
		image_one.setImageResource(ViewUtils.getWeatherImageId(
				ws[Constants.FIRSTDAY_ICON_INDEX], true));
		image_two.setImageResource(ViewUtils.getWeatherImageId(
				ws[Constants.SECONDDAY_ICON_INDEX], true));
		image_three.setImageResource(ViewUtils.getWeatherImageId(
				ws[Constants.THIRDDAY_ICON_INDEX], true));

	}

	OnClickListener buttonListener = new OnClickListener() {

		public void onClick(View v) {
			CommonUtils.l("on click.");

			int buttonid = v.getId();
			switch (buttonid) {

			case R.id.settings:
				Intent intent = new Intent(WeatherDetailActivity.this,
						SettingActivity.class);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						appWidgetId);
				startActivity(intent);
				break;
			case R.id.linearlayoutParent:
				// CommonUtils.l("parent click.");
				// Animation anim = AnimationUtils.loadAnimation(context,
				// R.anim.zoom_exit);
				// findViewById(R.id.linearlayoutParent).startAnimation(anim);
				// for(int i = 0; i<=10000000;i++){
				//
				// }
				finish();
				break;
			case R.id.detailLayout:
//				Intent intent1 = new Intent(WeatherDetailActivity.this,UpgradeActivity.class);
//				startActivity(intent1);
				break;
				
			case R.id.refresh:
				updateWeather();
				break;

			default:
				break;
			}

		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Constants.DISMISS_WEATHER_DIALOG_ID:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.cancel();
				CommonUtils.showShortToast(context, "Update done");
				setView();
				break;

			case Constants.FAILURE_ID:
				CommonUtils.showShortToast(context,
						context.getString(R.string.toast_data_failure));

				break;

			}
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		
	}
	
	private void updateWeather(){
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(context
				.getString(R.string.loading_data));
		mProgressDialog.show();
		Thread updateWeatherThread = new Thread(null, new Runnable() {
			public void run() {

				Message msg;

				boolean AutoGetAddress = Preferences
						.getAutoAddressStat(context, appWidgetId);

				if (Preferences.getShownAddress(context, appWidgetId)
						.equals(Constants.NOTSET)) {
					AutoGetAddress = true;
					Preferences.setAutoAddressStat(context, true,
							appWidgetId);
				}

				// get address failure, need update again
				if (AutoGetAddress) {
					if (!AddressUtils.updateAddress(context,
							appWidgetId))
						msg = mHandler
								.obtainMessage(Constants.FAILURE_ID);

				}

				BestWeatherManager f = new BestWeatherManager();
				if (f.downloadWeatherData(context, appWidgetId)) {
					f.loadWeatherDataFromXml(context, appWidgetId);
					msg = mHandler
							.obtainMessage(Constants.DISMISS_WEATHER_DIALOG_ID);
				} else
					msg = mHandler.obtainMessage(Constants.FAILURE_ID);

				Preferences.setUpdateTime(context,
						System.currentTimeMillis(), appWidgetId);
				context.startService(new Intent(context,
						UpdateViewService.class));
				msg.sendToTarget();
			}
		}, "UpdateWeather");
		updateWeatherThread.start();
	}
}
