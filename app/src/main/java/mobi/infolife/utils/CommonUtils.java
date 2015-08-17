/**
 *
 */
package mobi.infolife.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mobi.infolife.cwwidget.CWRemoteException;
import mobi.infolife.cwwidget.R;
import mobi.infolife.cwwidget.R.raw;
import mobi.infolife.cwwidget.Preferences;
import mobi.infolife.widget.framework.MyAppWidgetManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class CommonUtils {
	public final static boolean DBG = true;

	public static boolean isAutoLogin = false;
	public static String emailaddr = null;

	public static String format24Date(long time) {
		Date d = new Date(time);
		return new SimpleDateFormat("MM/dd HH:mm").format(d);
	}

	public static String format12Date(long time) {
		Date d = new Date(time);
		return new SimpleDateFormat("MM/dd KK:mm").format(d);
	}

	public static int getCurrentVersionCode(Context mContext) {
		PackageManager manager = mContext.getPackageManager();
		String packageName = mContext.getPackageName();
		try {
			PackageInfo info = manager.getPackageInfo(packageName, 0);
			return info.versionCode;
		} catch (Exception e) {
			return 1;
		}
	}

	public static void showLongToast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String content) {
		// BUG
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public static boolean isSamSungDevice() {
		File dir = new File(getExternalStorageDirPath() + "/"
				+ Constants.SAMSUNG_EXTERNAL_SD_DIR_NAME);
		if (dir.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static String exec(String cmd) {
		String output = "";

		try {
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream ins = process.getInputStream();
			byte[] buffer = new byte[1024];
			StringBuilder builder = new StringBuilder();
			int bytesRead = 0;

			while ((bytesRead = ins.read(buffer, 0, buffer.length)) != -1) {
				builder.append(new String(buffer, 0, bytesRead));
			}

			output = builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

	public static boolean isSamSungExternalSDMounted() {
		if (exec("mount").indexOf("external_sd") >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String getExternalStorageDirPath() {
		File sdCardDir = Environment.getExternalStorageDirectory();
		return sdCardDir.getAbsolutePath();
	}

	public static String getDefaultBackupPath() {
		return getSDCardDirPath() + "/" + Constants.BACKUP_DIR_NAME;
	}

	public static String getSDCardDirPath() {
		if (isSamSungDevice()) {
			if (isSamSungExternalSDMounted()) {
				return getExternalStorageDirPath() + "/"
						+ Constants.SAMSUNG_EXTERNAL_SD_DIR_NAME;
			} else {
				return getExternalStorageDirPath();
			}
		} else {
			return getExternalStorageDirPath();
		}
	}

	public static void gotoMarket(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://market.android.com/search?q=pname:"
						+ context.getPackageName()));
		context.startActivity(intent);
	}

	public static void l(String logString) {
		if (Constants.LOG)
			Log.i(Constants.TAG, logString);
	}

	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);

		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;

	}

	public static String getColoredHtmlString(String txt, int color) {
		if (txt == null)
			return null;
		// int color = 0xf94424;
		return ("<font color=\"#" + Integer.toHexString(color) + "\">" + txt + "</font>");
	}

	public static void changeLocaleSetting(Context c) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName("com.android.settings",
				"com.android.settings.DateTimeSettings");
		c.startActivity(intent);
	}

	public static void playTone(Context c) {
		AudioManager audioManager = (AudioManager) c
				.getSystemService(Context.AUDIO_SERVICE);

		ToneGenerator mToneGenerator = new ToneGenerator(
				AudioManager.STREAM_DTMF, 80);

		int ringerMode = audioManager.getRingerMode();
		if (ringerMode == AudioManager.RINGER_MODE_SILENT
				|| ringerMode == AudioManager.RINGER_MODE_VIBRATE) { // 靜音或是震動則不發出聲音
			return;
		}
		MediaPlayer mp = MediaPlayer.create(c, R.raw.click);
		mp.start();

	}

	public static List<Integer> addArrayToList(List<Integer> l, int[] a) {
		if (a != null && l != null) {
			for (int i = 0; i <= a.length - 1; i++) {
				l.add(a[i]);
			}
		}
		return l;
	}

	public static int[] combineArray(int[] a, int[] b) {
		if (a == null && b == null)
			return null;
		if (a == null && b != null)
			return b;
		if (b == null && a != null)
			return a;

		int al = a.length;
		int bl = b.length;

		int[] c = new int[al + bl];

		for (int i = 0; i <= al - 1; i++) {
			c[i] = a[i];
		}
		for (int i = 0; i <= bl - 1; i++) {
			c[al + i] = b[i];
		}
		for (int i = 0; i <= al + bl - 1; i++) {
			l("array" + i + "=" + c[i]);
		}
		return c;
	}

	public static Intent getClassName(Context c, Intent intent) {
		boolean haveDeskclock = false;
		List<PackageInfo> packs = c.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);

			if (p.packageName.equals("com.htc.android.worldclock"))
				return intent.setClassName("com.htc.android.worldclock",
						"com.htc.android.worldclock.WorldClockTabControl");
			if (p.packageName.equals("com.motorola.blur.alarmclock"))
				return intent.setClassName("com.motorola.blur.alarmclock",
						"com.motorola.blur.alarmclock.AlarmClock");

			if (p.packageName.equals("com.sec.android.app.clockpackage"))
				return intent.setClassName("com.sec.android.app.clockpackage",
						"com.sec.android.app.clockpackage.ClockPackage");
			if (p.packageName.equals("com.lge.alarm"))
				return intent.setClassName("com.lge.alarm",
						"com.lge.alarm.Super_Clock");
			if (p.packageName.equals("com.android.deskclock"))
				return intent.setClassName("com.android.deskclock",
						"com.android.deskclock.DeskClock");
			if (p.packageName.equals("com.android.alarmclock"))
				return intent.setClassName("com.android.alarmclock",
						"com.android.alarmclock.AlarmClock");
			if (p.packageName.equals("com.google.android.deskclock")) {
				haveDeskclock = true;
			}
		}

		if (haveDeskclock)
			return intent.setClassName("com.google.android.deskclock",
					"com.android.deskclock.AlarmClock");

		return intent.setClassName("com.android.settings",
				"com.android.settings.DateTimeSettings");
	}

	public static int[] initIds(MyAppWidgetManager myAppWidgetManager,
			ComponentName cn) throws CWRemoteException {
		// CommonUtils.l("initIds");
		int[] widgetIds = null;

		try {
			widgetIds = myAppWidgetManager.getAppWidgetIds(cn);
			// CommonUtils.l("1.widgetIds:" + widgetIds);

			if (widgetIds == null) {
				throw new CWRemoteException();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return widgetIds;
	}

	public static int getWeekNum() {
		Calendar c = Calendar.getInstance();
		// c.set(Calendar.DAY_OF_YEAR, 1);
		return c.get(Calendar.WEEK_OF_YEAR);

	}

	public static void killSelf() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static String stringArrayToString(String[] data) {
		if (data == null)
			return null;

		String value = "";
		for (String a : data) {
			value += a;
			value += Constants.SPLITER;
		//CommonUtils.l(Constants.SPLITER+"+"+a);

		}
		return value;
	}

	public static String[] stringToStringArray(String data) {
		if (data == null)
			return null;
		if (data == Constants.NOTSET)
			return null;
 		return data.split(Constants.SPLITER);

	}

	static String getWeedDayByOffSet(int offset) {
		long now = System.currentTimeMillis();
		Date date = new Date();
		date.setTime(now + offset * Constants.ONE_DAY);
		String weekdayString = String.format("%ta", date);
		return weekdayString;

	}

	public static String transferToCTemp(String tempF,boolean highStat ) {
	    if(tempF.trim().length()<1)
			return "error";
		try {
			String tempC = "";
			if(highStat)
				tempC = String.valueOf(Math.ceil((Integer.parseInt(tempF) - 32) / 1.8));
			else
				tempC = String.valueOf(Math.floor((Integer.parseInt(tempF) - 32) / 1.8));

			int dotIndex = tempC.indexOf(".");
			tempC = tempC.substring(0, dotIndex);
			if(TextUtils.equals(tempC, "-0")){
				tempC="0";
			}
			return tempC;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	public static String transferToFTemp(String tempC,boolean highStat) {
	    if(tempC.trim().length()<1)
				return "error";
		try {
			String tempF ="";
			if(highStat)
				tempF = String.valueOf(Math.ceil((Integer.parseInt(tempC) * 1.8 + 32)));
			else
				tempF = String.valueOf(Math.floor((Integer.parseInt(tempC) * 1.8 + 32)));

			int dotIndex = tempF.indexOf(".");
			tempF = tempF.substring(0, dotIndex);
			if(TextUtils.equals(tempF, "-0")){
				tempF="0";
			}
			return tempF;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	public static boolean getBooleanConfig(Context context, String name){
		try{
			boolean isGone = context.getResources().
					getBoolean(getResourseIdByName(context.
							getClassLoader().loadClass(context.getPackageName()+ ".R"),"bool",name));
			return isGone;
		} catch(Exception e){
			return false;
		}
	}

	public static int getResourseIdByName(Class clazz, String className,
			String name) {
		int id = 0;
		try {

			Class[] classes = clazz.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if (desireClass != null)
				id = desireClass.getField(name).getInt(desireClass);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static void startApplicationWithPackageName(Context c, String packagename) {
		try {
			Intent launchIntent = c.getPackageManager().getLaunchIntentForPackage(packagename);
			c.startActivity(launchIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Intent getClockClassNameNew(Context c, Intent intent) {
		String packageName = Preferences.getClockPackageName(c);
		String activityName = Preferences.getClockActivityName(c);

		// if exist , return saved value
		if (!TextUtils.equals(packageName, Constants.NOTSET)) {
			intent = c.getPackageManager().getLaunchIntentForPackage(
					packageName);
			if (intent != null) {
				return intent;
			} else {
				intent = getIntentFromPackageName(c, packageName);
				return intent;
			}
		}
		// not exist , try get, if fail , return open settings

		intent = new Intent(AlarmClock.ACTION_SET_ALARM);
		intent.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
		intent.putExtra(AlarmClock.EXTRA_HOUR, 10);
		intent.putExtra(AlarmClock.EXTRA_MINUTES, 30);

		List<ResolveInfo> resInfo = c.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.GET_UNINSTALLED_PACKAGES);
		packageName = resInfo.get(0).activityInfo.packageName;
		activityName = resInfo.get(0).activityInfo.name;

		// don not find alarm activity , return setting
		if (TextUtils.equals(packageName, Constants.NOTSET)) {
			packageName = "com.android.settings";
			activityName = "com.android.settings.DateTimeSettings";
		}
		// find alarm activity , save name

		else {
			Preferences.setClockPackageName(c, packageName);
			Preferences.setClockActivityName(c, activityName);
		}
		return intent.setClassName(packageName, activityName);

	}

	public static Intent getIntentFromPackageName(Context c, String packageName) {
		Intent intent = new Intent();
		intent.setClassName(packageName, getClassName(c, packageName));
		return intent;

	}

	public static String getClassName(Context c, String packageName) {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = c.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		String className = "";

		for (ResolveInfo ri : list) {
			if (ri.activityInfo.packageName.equals(packageName)) {
				className = ri.activityInfo.name;
				break;
			}
		}
		return className;
	}

	public static Intent getCalendarClassNameNew(Context c, Intent intent) {
		String packageName = Preferences.getCalendarPackageName(c);
		String activityName = Preferences.getCalendarActivityName(c);

		// if exist , return saved value
		if (!TextUtils.equals(packageName, Constants.NOTSET)) {
			intent = c.getPackageManager().getLaunchIntentForPackage(
					packageName);
			if (intent != null) {
				return intent;
			} else {
				intent = getIntentFromPackageName(c, packageName);
				return intent;
			}
		}

		// not exist , try get, if fail , return open settings
		intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("title", "Some title");
		intent.putExtra("description", "Some description");

		List<ResolveInfo> resInfo = c.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.GET_UNINSTALLED_PACKAGES);
		packageName = resInfo.get(0).activityInfo.packageName;
		activityName = resInfo.get(0).activityInfo.name;

		// don not find alarm activity , return setting
		if (TextUtils.equals(packageName, Constants.NOTSET)) {
			packageName = "com.android.settings";
			activityName = "com.android.settings.DateTimeSettings";
		}
		// find alarm activity , save name

		else {
			Preferences.setCalendarPackageName(c, packageName);
			Preferences.setCalendarActivityName(c, activityName);
		}

		return intent.setClassName(packageName, activityName);
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}
