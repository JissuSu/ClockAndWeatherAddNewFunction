package mobi.infolife.cwwidget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mobi.infolife.utils.CommonUtils;
import mobi.infolife.utils.Constants;
import mobi.infolife.utils.TaskUtils;
import mobi.infolife.utils.ViewUtils;
import mobi.infolife.widget.framework.MyAppWidgetManager;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.RemoteViews;

public class ClockWidget extends AppWidgetProvider implements CWWidget {
	private static ClockWidget sInstance;
	
	public ClockWidget() {
		super();
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		CommonUtils.l("-On delete");

		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		CommonUtils.l("-On disable");

		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		CommonUtils.l("-On Enable");

		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		CommonUtils.l("-On Receive");
		final String action = intent.getAction();
		if (action != null) {
			if (action.equals(MyAppWidgetManager.REBIND_ACTION)
					|| action.equals(Constants.ACTION_READY)) {
				context.startService(new Intent(context,
						UpdateViewService.class)
						.setAction(MyAppWidgetManager.REBIND_ACTION));
				context.startService(new Intent(context,
						ScreenStatService.class)
						.setAction(MyAppWidgetManager.REBIND_ACTION));

			}else if(action.equals(Constants.ACTION_CLICK_DATE)){
				CommonUtils.getClockClassNameNew(context, intent);
				String packageName = Preferences.getClockPackageName(context);
				if(!packageName.equals(Constants.NOTSET)){
					CommonUtils.startApplicationWithPackageName(context, packageName);
				}else{
					CommonUtils.startApplicationWithPackageName(context, "com.android.settings");
				}
			}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		CommonUtils.l("-On update");

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews views = ViewUtils.getTimeRemoteViews(context);

//		for (int appWidgetId : appWidgetIds) {
//			TaskUtils.widgetUpdate(context, views, appWidgetId);
//		}
		mAppWidgetManager = AppWidgetManager.getInstance(context);  

 		mAppWidgetManager.updateAppWidget(new ComponentName(context,
				ClockWidget.class), views);

 		context.startService(new Intent(context, UpdateViewService.class));
		context.startService(new Intent(context, ScreenStatService.class));

	}



 
	
//	private ComponentName[] mComponentNames = {
//		new ComponentName("mobi.infolife.cwwidget", "mobi.infolife.cwwidget.ClockWidget"),
//	};
//		
	private MyAppWidgetManager mMyAppWidgetManager;
	private AppWidgetManager mAppWidgetManager;

	private Context mContext;
	
	public ClockWidget(Context context, MyAppWidgetManager myAppWidgetManager) {
		mContext = context;
		mMyAppWidgetManager = myAppWidgetManager;
	}

	@Override
	public void updateView() throws CWRemoteException {
		CommonUtils.l("updateClockView");
		
		RemoteViews clockView = ViewUtils.getTimeRemoteViews(mContext);

//		int[] ezClockWidgetIds = CommonUtils.initIds(mMyAppWidgetManager,
//				new ComponentName(mContext, this.getClass()));
//
//		if (ezClockWidgetIds != null) {
//			if (ezClockWidgetIds.length > 0) {
//				CommonUtils.l(ezClockWidgetIds.length
//						+ " clock widget need update");
//				TaskUtils.widgetUpdate(mContext, clockView, ezClockWidgetIds);
//			} else
//				CommonUtils.l("id length is zero");
//
//		} else{
//			CommonUtils.l("id is null");
//		}
		mAppWidgetManager = AppWidgetManager.getInstance(mContext);
		int[] clockWidgetIds = mAppWidgetManager
				.getAppWidgetIds(new ComponentName(mContext, this.getClass()));
		if (clockWidgetIds != null) {

			if (clockWidgetIds.length > 0) {
				CommonUtils.l(clockWidgetIds.length
						+ " weather widget need update");
				for (int i = 0; i <= clockWidgetIds.length - 1; i++) {
					clockView = ViewUtils.getTimeRemoteViews(mContext);

					mAppWidgetManager.updateAppWidget(clockWidgetIds[i],
							clockView);
				}
			}
		}
// 		mAppWidgetManager.updateAppWidget(new ComponentName(mContext,
//				ClockWidget.class), clockView);
	}

	@Override
	public void loadData() {
		//CommonUtils.l("loadData");
	}

	@Override
	public void loadPreferences() {
		//CommonUtils.l("loadPreferences");
	}

}
