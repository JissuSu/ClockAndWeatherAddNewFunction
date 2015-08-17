/**
 *
 */
package mobi.infolife.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.infolife.limitNumAndAir.XKMain.XKGetUtil;
import mobi.infolife.limitNumAndAir.XKModel.AirData;
import mobi.infolife.limitNumAndAir.XKModel.LimitTodayNum;
import mobi.infolife.limitNumAndAir.XKUtils.DataStore;
import mobi.infolife.batteryAlarmEvent.AlarmUtils;
import mobi.infolife.batteryAlarmEvent.BatteryUtils;
import mobi.infolife.batteryAlarmEvent.EventUtils;
import mobi.infolife.cwwidget.Preferences;
import mobi.infolife.cwwidget.R;
import mobi.infolife.cwwidget.WeatherDetailActivity;
import mobi.infolife.dateAndDB.DBManager;
import mobi.infolife.dateAndDB.DateUtil;

public class ViewUtils {

    public static void updateWeekNumView(RemoteViews views, Context context) {
        if (Preferences.getWeekInfoStat(context)) {
            views.setTextViewText(R.id.weeknum, " " + CommonUtils.getWeekNum());
            views.setViewVisibility(R.id.weeknum, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.weeknum, View.GONE);
        }
    }

    public static RemoteViews getFullRemoteviews(Context context,
                                                 int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.full);

        loadTimeInfoToView(context, views);
        loadSimpleAddressInfoToView(context, views, appWidgetId);
        loadSimpleWeatherInfoToView(context, views, appWidgetId);
        loadSimpleWeatherClickInfoToView(context, views, appWidgetId);
        loadHumidityWindDressing(views, context, appWidgetId);
        return views;
    }

    public static void loadTimeInfoToView(Context context, RemoteViews views) {
        Date date = new Date();

        int hour = date.getHours();
        if (Preferences.get24FormatStat(context)) {
            // timeFormate = new SimpleDateFormat("kk:mm");
            views.setViewVisibility(R.id.msign, View.INVISIBLE);
        } else {
            views.setViewVisibility(R.id.msign, View.VISIBLE);

            views.setTextViewText(R.id.msign, "AM");

            if (hour > 12) {
                hour = hour - 12;
                views.setTextViewText(R.id.msign, "PM");
            }
            // timeFormate = new SimpleDateFormat("hh:mm");
            // String msign = new SimpleDateFormat("a").format(date);
            // views.setTextViewText(R.id.msign, msign);
        }
        int minute = date.getMinutes();

        Bitmap bitmap1 = null;
        Bitmap bitmap2 = null;
        Bitmap bitmap3 = null;
        Bitmap bitmap4 = null;
        Bitmap bitmap5 = null;

        bitmap1 = BuildClockAndTemp.buildClockBitMap(context, String.valueOf((int) hour / 10));
        bitmap2 = BuildClockAndTemp.buildClockBitMap(context, String.valueOf(hour % 10));
        bitmap3 = BuildClockAndTemp.buildClockBitMap(context, ":");
        bitmap4 = BuildClockAndTemp.buildClockBitMap(context, String.valueOf(minute / 10));
        bitmap5 = BuildClockAndTemp.buildClockBitMap(context, String.valueOf(minute % 10));

        views.setImageViewBitmap(R.id.clock1, bitmap1);
        views.setImageViewBitmap(R.id.clock2, bitmap2);
        views.setImageViewBitmap(R.id.clock3, bitmap3);
        views.setImageViewBitmap(R.id.clock4, bitmap4);
        views.setImageViewBitmap(R.id.clock5, bitmap5);


        updateWeekNumView(views, context);
        // String timeString = timeFormate.format(date);
        String dateString = String.format(
                context.getString(R.string.date_formater), date, date);
        String weekdayString = String.format("%ta", date);

        views.setTextViewText(R.id.weekday, weekdayString);
        // views.setTextViewText(R.id.clock, timeString);
        views.setTextViewText(R.id.date, dateString);

    }

    public static void loadSimpleWeatherClickInfoToView(Context context,
                                                        RemoteViews views, int appWidgetId) {

        Intent intent = new Intent(context, WeatherDetailActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // intent.putExtra("TYPE", "FULL");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                Constants.FULL);
        // Utils.l("appWidgetId passed =" + appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.weatherlinear, pendingIntent);

        Intent dateIntent = new Intent().setAction(Constants.ACTION_CLICK_DATE);
        dateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        dateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                Constants.FULL);

        PendingIntent datePendingIntent = PendingIntent.getBroadcast(context,
                appWidgetId, dateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.datelinear, datePendingIntent);

    }

    public static void loadSimpleWeatherInfoToView(Context context,
                                                   RemoteViews views, int appWidgetId) {

        String filePath = TaskUtils
                .getWeatherDataFileName(context, appWidgetId);
        File weatherData = new File(filePath);
        if (!weatherData.exists()) {
            Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
            return;
        }

        BaseWeatherManager f = new BaseWeatherManager();
        String weatherString[] = f.parseWeatherData(context, appWidgetId);

        if (weatherString == null) {
            Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
            return;
        }

        Bitmap bitmap1 = null;
        Bitmap bitmap2 = null;
        Bitmap bitmap3 = null;
        Bitmap bitmap4 = null;
        if (weatherString[Constants.TODAY_TEMP_INDEX]!=null){
            char temp[] = new char[4];
            for(int i=0;i<weatherString[Constants.TODAY_TEMP_INDEX].length()&&i<4;i++){
                temp[i] =weatherString[Constants.TODAY_TEMP_INDEX].charAt(i);
            }
            bitmap1 = BuildClockAndTemp.buildTempBitMap(context, String.valueOf(temp[0]));
            bitmap2 = BuildClockAndTemp.buildTempBitMap(context, String.valueOf(temp[1]));
            bitmap3 = BuildClockAndTemp.buildTempBitMap(context, String.valueOf(temp[2]));
            bitmap4 = BuildClockAndTemp.buildTempBitMap(context, String.valueOf(temp[3]));


            views.setImageViewBitmap(R.id.temp1,bitmap1);
            views.setImageViewBitmap(R.id.temp2,bitmap2);
            views.setImageViewBitmap(R.id.temp3,bitmap3);
            views.setImageViewBitmap(R.id.temp4, bitmap4);
        }

        views.setTextViewText(R.id.condition,
                weatherString[Constants.TODAY_CONDITION_INDEX]);
        views.setTextViewText(R.id.low, weatherString[Constants.TODAY_LOW_INDEX]);
        views.setTextViewText(R.id.high, weatherString[Constants.TODAY_HIGH_INDEX]);

        views.setImageViewResource(R.id.todayimage, ViewUtils
                .getWeatherImageId(weatherString[Constants.TODAY_ICON_INDEX],
                        false));
    }

    public static void loadSimpleAddressInfoToView(Context context,
                                                   RemoteViews views, int appWidgetId) {
        String addressString = Preferences
                .getShownAddress(context, appWidgetId);

        views.setTextViewText(R.id.address, addressString);

    }

    public static int getWeatherImageId(String iconUrl, boolean alwaysLight) {
        // CommonUtils.l("icon=" + iconUrl);
        Date date = new Date();
        // CommonUtils.l("ICONURL" + iconUrl);
        boolean ISLIGHT = true;
        int hour = date.getHours();
        if (hour > 18 || hour < 6)
            ISLIGHT = false;
        if (alwaysLight)
            ISLIGHT = true;
        if (iconUrl != null) {
            if (iconUrl.contains("sunny") || iconUrl.contains("mostly_sunny")
                    || iconUrl.contains("clear")
                    || TextUtils.equals(iconUrl.trim(), "01")
                    || TextUtils.equals(iconUrl.trim(), "33")
                    || TextUtils.equals(iconUrl.trim(), "30")
                    || TextUtils.equals(iconUrl.trim(), "02")
                    || TextUtils.equals(iconUrl.trim(), "34")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_CLEAR_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_CLEAR_DARK];
            } else if (iconUrl.contains("rain") || iconUrl.contains("shower")
                    || iconUrl.contains("lightrain")
                    || TextUtils.equals(iconUrl.trim(), "12")
                    || TextUtils.equals(iconUrl.trim(), "13")
                    || TextUtils.equals(iconUrl.trim(), "14")
                    || TextUtils.equals(iconUrl.trim(), "39")
                    || TextUtils.equals(iconUrl.trim(), "40")
                    || TextUtils.equals(iconUrl.trim(), "26")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_SMALL_RAIN_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_SMALL_RAIN_DARK];
            } else if (iconUrl.contains("heavyrain")
                    || TextUtils.equals(iconUrl.trim(), "18")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_BIG_RAIN_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_BIG_RAIN_DARK];
            } else if (iconUrl.contains("fog") || iconUrl.contains("smoke")
                    || iconUrl.contains("mist")
                    || TextUtils.equals(iconUrl.trim(), "05")
                    || TextUtils.equals(iconUrl.trim(), "37")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_FOG_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_FOG_DARK];
            } else if (iconUrl.contains("thunderstorm")
                    || iconUrl.contains("storm")
                    || TextUtils.equals(iconUrl.trim(), "15")
                    || TextUtils.equals(iconUrl.trim(), "16")
                    || TextUtils.equals(iconUrl.trim(), "17")
                    || TextUtils.equals(iconUrl.trim(), "41")
                    || TextUtils.equals(iconUrl.trim(), "42")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_THUNDER_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_THUNDER_DARK];
            } else if (iconUrl.contains("haze")
                    || TextUtils.equals(iconUrl.trim(), "11")) {
                return Constants.weatherDrawable[Constants.INDEX_HAZE];
            } else if (iconUrl.contains("hail")

                    || TextUtils.equals(iconUrl.trim(), "25")) {
                return Constants.weatherDrawable[Constants.INDEX_HAIL];
            } else if (iconUrl.contains("flurries")
                    || TextUtils.equals(iconUrl.trim(), "19")
                    || TextUtils.equals(iconUrl.trim(), "20")
                    || TextUtils.equals(iconUrl.trim(), "43")) {
                return Constants.weatherDrawable[Constants.INDEX_SMALL_SNOW];
            } else if (iconUrl.contains("chance_of_snow")
                    || iconUrl.contains("snow")
                    || TextUtils.equals(iconUrl.trim(), "23")
                    || TextUtils.equals(iconUrl.trim(), "21")
                    || TextUtils.equals(iconUrl.trim(), "22")
                    || TextUtils.equals(iconUrl.trim(), "44")) {
                return Constants.weatherDrawable[Constants.INDEX_SNOW];

            } else if (iconUrl.contains("sleet")
                    || TextUtils.equals(iconUrl.trim(), "29")) {
                return Constants.weatherDrawable[Constants.INDEX_SNOW_RAIN];

            } else if (iconUrl.contains("dust")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_DUST_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_DUST_DARK];

            } else if (iconUrl.contains("icy")
                    || TextUtils.equals(iconUrl.trim(), "24")
                    || TextUtils.equals(iconUrl.trim(), "31")) {
                return Constants.weatherDrawable[Constants.INDEX_ICY];

            } else if (iconUrl.contains("wind")
                    || TextUtils.equals(iconUrl.trim(), "32")) {
                return Constants.weatherDrawable[Constants.INDEX_WIND];

            } else if (iconUrl.contains("cloud")
                    || iconUrl.contains("overcast")
                    || TextUtils.equals(iconUrl.trim(), "03")
                    || TextUtils.equals(iconUrl.trim(), "04")
                    || TextUtils.equals(iconUrl.trim(), "06")
                    || TextUtils.equals(iconUrl.trim(), "07")
                    || TextUtils.equals(iconUrl.trim(), "08")
                    || TextUtils.equals(iconUrl.trim(), "35")
                    || TextUtils.equals(iconUrl.trim(), "36")
                    || TextUtils.equals(iconUrl.trim(), "38")) {
                if (ISLIGHT)
                    return Constants.weatherDrawable[Constants.INDEX_PART_CLOUD_LIGHT];
                else
                    return Constants.weatherDrawable[Constants.INDEX_PART_CLOUD_DARK];
            }
        }

        return Constants.weatherDrawable[Constants.INDEX_UNKNOW];

    }

    public static void widgetUpdate(Context context, RemoteViews views,
                                    int appWidgetId) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(appWidgetId, views);
        // MyAppWidgetManager.updateAppWidget(context, views,
        // new int[] { appWidgetId });
    }

    public static void widgetUpdate(Context context, RemoteViews views,
                                    int[] appWidgetId) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(appWidgetId, views);
        // MyAppWidgetManager.updateAppWidget(context, views, appWidgetId);
    }

    public static RemoteViews loadBatteryView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.full);
        views.setImageViewResource(R.id.batteryView,
                BatteryUtils.getBatteryImageId());
        views.setTextViewText(R.id.batteryText,
                BatteryUtils.getCurrentBatteryLevel() + "%");
        return views;
    }

    public static RemoteViews loadXKView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.full);
        AirData airData;
        LimitTodayNum limitTodayNum;
        if (DataStore.getData(context, DataStore.CITY_NAME).equals("null")) {
            airData = new AirData("暂无", "暂无", "暂无");
            limitTodayNum = new LimitTodayNum("暂无", "暂无", "暂无", "暂无");
        } else {
            airData = XKGetUtil.getAirQuility(DataStore.getData(context,
                    DataStore.CITY_NAME));
            limitTodayNum = XKGetUtil.getLimitTodayData(DataStore.getData(
                    context, DataStore.CITY_NAME));
            if (airData == null) {
                airData = new AirData("暂无", "暂无", "暂无");
            }
            if (limitTodayNum == null) {
                limitTodayNum = new LimitTodayNum("暂无", "暂无", "暂无",
                        "暂无");
            }
        }
        views.setTextViewText(R.id.airquility, airData.airnum
                + airData.airstatus);
        views.setTextViewText(R.id.limitnum, limitTodayNum.nums);
        return views;
    }

    public static RemoteViews loadAlarmView(Context c) {
        RemoteViews views = new RemoteViews(c.getPackageName(),
                R.layout.full);
        AlarmUtils alarmUtils = new AlarmUtils(c);
        views.setTextViewText(R.id.alarmText, alarmUtils.getNextAlarm());
        Intent intenta;
        if (CommonUtils.isIntentAvailable(c, "android.intent.action.SHOW_ALARMS")) {
            intenta = new Intent("android.intent.action.SHOW_ALARMS");
        } else {
            intenta = new Intent(AlarmClock.ACTION_SET_ALARM);
        }
        PendingIntent pendingIntenta = PendingIntent.getActivity(c, 0, intenta, 0);
        views.setOnClickPendingIntent(R.id.alarmText, pendingIntenta);
        return views;
    }

    public static RemoteViews loadEventsView(Context c) {
        RemoteViews views = new RemoteViews(c.getPackageName(),
                R.layout.full);
        EventUtils eventUtils = new EventUtils(c);
        views.setTextViewText(R.id.eventText, eventUtils.getNextEvent());
        long id = eventUtils.getEventId();
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://com.android.calendar/events"), id);
        Intent intente = new Intent(Intent.ACTION_VIEW).setData(uri);
        PendingIntent pendingIntente = PendingIntent.getActivity(c, 0, intente, 0);
        views.setOnClickPendingIntent(R.id.eventText, pendingIntente);
        return views;
    }

    public static RemoteViews loadLunerAndHoliday(Context c, int appWidgetId) {
        RemoteViews views = new RemoteViews(c.getPackageName(),
                R.layout.full);
        Time time = new Time();
        time.setToNow();
        int year = time.year;
        int month = time.month + 1;
        int monthDay = time.monthDay;
        int weekday = time.weekDay;
        String countryName = Preferences.getLocatedCountry(c, appWidgetId);

        if(countryName.equals("United States")){
            DBManager.countryName = "UnitedStates";
        }else if(countryName.equals("China")){
            DBManager.countryName="China";
        }else{
            DBManager.countryName="ww";
        }

        DateUtil du = new DateUtil(c, year, month, monthDay, weekday);

        String dateStr = du.getLunerDateString();
        String sbayear = du.getSBAyear();
        String weekDay = du.getWeekDay();
        String holiday = du.getHolidayStr();

        views.setTextViewText(R.id.txtDay, dateStr);
        views.setTextViewText(R.id.txtMonth, sbayear);
        views.setTextViewText(R.id.txtWeekDay, weekDay);
        views.setTextViewText(R.id.txtFestival, holiday);

        Intent calendarIntent = new Intent().setAction(Constants.ACTION_CLICK_CALENDAR);

        PendingIntent calendarPendingIntent = PendingIntent.getBroadcast(c,
                0, calendarIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.luner_layout, calendarPendingIntent);
        return views;
    }

    public static void loadHumidityWindDressing(RemoteViews views,Context context,int appWidgetId){
        String humidity="",wind="",dressing="";
        Pattern p = Pattern.compile("[0-9\\.]+");
        Matcher m;
        BestWeatherManager f = new BestWeatherManager();
        String weatherString[] = f.parseWeatherData(context, appWidgetId);
        if (weatherString != null) {
            CommonUtils.l("update detail weather activity view");
            m = p.matcher(weatherString[Constants.TODAY_HUMIDITY_INDEX]);
            while(m.find()){
                humidity=m.group();
            }
            wind=weatherString[Constants.TODAY_WIND_INDEX];
            String[] arr=getThreeSubString(wind);
            views.setTextViewText(R.id.humidity, humidity+"%");
            views.setTextViewText(R.id.wind, arr[0]+"/"+arr[1]+"/"+arr[2]);
            views.setTextViewText(R.id.dressing, "43");
        }else{
            Preferences.setNeedInternetUpdateStat(context, true, appWidgetId);
            CommonUtils.l("weather string is null");
        }
        views.setTextViewText(R.id.dressing,"43"+"%");
    }
    //first:direction; second: number; third: unit
    public static String[] getThreeSubString(String wind){
        String[] strArr=new String[3];
        int firstSpace=wind.indexOf(" ");
        String sub1=wind.substring(firstSpace+1);
        int secondSpace=sub1.indexOf(" ");
        String one=sub1.substring(0, secondSpace);
        strArr[0]=one;
        String sub2=sub1.substring(secondSpace+1);
        int thirdSpace=sub2.indexOf(" ");
        String two=sub2.substring(0,thirdSpace);
        strArr[1]=two;
        String sub3=sub2.substring(thirdSpace+1);
        strArr[2]=sub3;
        return strArr;
    }

}
