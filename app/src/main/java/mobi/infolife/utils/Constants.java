/**
 * 
 */
package mobi.infolife.utils;

import mobi.infolife.cwwidget.R;
import mobi.infolife.cwwidget.R.drawable;
import android.view.View;
import android.widget.AdapterView;

public class Constants {
	// public final static boolean DBG = true;
	public final static String TAG = "SWW";
	public final static boolean LOG = true;
	public final static boolean TLOG = true;
	//public final static Object[] handlerPool = { BestWeatherManager.class , FreeWeatherManager.class	};
	
	public static int[] intervalArray = { 1800, 3600, 10800, 21600, 43200,
			86400 };
	// for backup options.
	public static String FULL = "full";
	public static String WEATHER = "weather";

			

	public final static int BACKUP_SMS = 1;
	public final static int RESTORE_SMS = 2;

	public final static int SHOW_SMS = 3;
	public final static int SDK_FROYO = 8;

	public final static long TEN_SECOND = 10 * 1000;
	public final static long ONE_MINUTE = 60 * 1000;
	public final static long ONE_HOUR = 60 * 60 * 1000;
	public final static long ONE_DAY = ONE_HOUR * 24;
	public final static long ONE_WEEK = ONE_DAY * 7;
	public final static long ONE_MONTH = ONE_WEEK * 4;

	public final static int REMIND_ONE = 1;
	public final static int REMIND_TEN = 10;
	public final static int REMIND_TWENTY = 20;
	public final static int REMIND_FIFTY = 50;
	public final static int REMIND_HUNDRED = 100;
	public final static int REMIND_FIVEHUNDRED = 500;

	public final static String BUDDLE_KEY_WIDGETID = "appwidgetid";
	public final static String FILE_EXIST = "fileExist";
	public static final String BACKUP_DIR_NAME = "SMSbackup";
	public static final String SAMSUNG_EXTERNAL_SD_DIR_NAME = "external_sd";
	public static final String ARCHIVE_NAME = "ARCHIVE-SMS";
	public static final String NO_BACKUPFILE = "no_backup_file";
	public static final String TEMP_ARCHIVE_DBFILE = "temp_archive_backup_file";
	public static final String SHOW_ERROR = "show_sms_error";
	public static final String NORMAL_BACKUP_ERROR = "normal_backup_error";
	public static final String ARCHIVE_BACKUP_ERROR = "acrchive_backup_error";
	public static final String NO_SMS_NEEDBACKUP = "no_sms_need_backup";
	public static final String SPLITSYMBOL = "addresssplit";
	public static final String SPLITER = "#WEATHERSPLITER#";
	public final static String ACTION_READY = "mobi.intuitit.android.hpp.ACTION_READY";

	public static final String NOTSET = "Loading";

	public final static String DELETE_ALL = "delete_all";
	public static final String DELETE_OLD = "delete_old";
	public static final String DELETE_STR = "detele_stranger";
	// public static final String FOLDER_ADDR = CommonUtils.getSDCardDirPath()
	// + "/SWwidget";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7 Weather/";
	public final static int TODAY_CONDITION_INDEX = 0;
	public final static int TODAY_TEMP_INDEX = 1;
	public final static int TODAY_LOW_INDEX = 2;
	public final static int TODAY_HIGH_INDEX = 3;
	public final static int TODAY_ICON_INDEX = 4;
	public final static int FIRSTDAY_ICON_INDEX = 5;
	public final static int FISRTDAY_HIGH_INDEX = 6;
	public final static int FISRTDAY_LOW_INDEX = 7;
	public final static int SECONDDAY_ICON_INDEX = 8;
	public final static int SECONDDAY_HIGH_INDEX = 9;
	public final static int SECONDDAY_LOW_INDEX = 10;
	public final static int THIRDDAY_HIGH_INDEX = 11;
	public final static int THIRDDAY_LOW_INDEX = 12;
	public final static int THIRDDAY_ICON_INDEX = 13;
	public final static int FISRTDAY_NAME_INDEX = 14;
	public final static int SECONDDAY_NAME_INDEX = 15;
	public final static int THIRDDAY_NAME_INDEX = 16;
	public final static int TODAY_HUMIDITY_INDEX = 17;
	public final static int TODAY_WIND_INDEX= 18;

	public static final int[] numberDrawable = { R.drawable.zero,
			R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
			R.drawable.five, R.drawable.six, R.drawable.seven,
			R.drawable.eight, R.drawable.nine };

	public static final int[] numberDrawableH = { R.drawable.zero_h,
			R.drawable.one_h, R.drawable.two_h, R.drawable.three_h,
			R.drawable.four_h, R.drawable.five_h, R.drawable.six_h,
			R.drawable.seven_h, R.drawable.eight_h, R.drawable.nine_h };

	public static final int[] numberDrawableM = { R.drawable.zero_m,
			R.drawable.one_m, R.drawable.two_m, R.drawable.three_m,
			R.drawable.four_m, R.drawable.five_m, R.drawable.six_m,
			R.drawable.seven_m, R.drawable.eight_m, R.drawable.nine_m };

	public final static int INDEX_BIG_RAIN_DARK = 0;
	public final static int INDEX_BIG_RAIN_LIGHT = 1;
	public final static int INDEX_CLEAR_DARK = 2;
	public final static int INDEX_CLEAR_LIGHT = 3;
	public final static int INDEX_CLOUDY = 4;
	public final static int INDEX_DUST_DARK = 5;
	public final static int INDEX_DUST_LIGHT = 6;
	public final static int INDEX_HAIL = 7;
	public final static int INDEX_HAZE = 8;
	public final static int INDEX_ICY = 9;
	public final static int INDEX_FOG_DARK = 10;
	public final static int INDEX_FOG_LIGHT = 11;
	public final static int INDEX_PART_CLOUD_DARK = 12;
	public final static int INDEX_PART_CLOUD_LIGHT = 13;
	public final static int INDEX_SMALL_RAIN_DARK = 14;
	public final static int INDEX_SMALL_RAIN_LIGHT = 15;
	public final static int INDEX_SMALL_SNOW = 16;
	public final static int INDEX_SNOW = 17;
	public final static int INDEX_SNOW_RAIN = 18;
	public final static int INDEX_THUNDER_DARK = 19;
	public final static int INDEX_THUNDER_LIGHT = 20;
	public final static int INDEX_WIND = 21;
	public final static int INDEX_UNKNOW = 22;

	public static final int DISMISS_WEATHER_DIALOG_ID = 0x18883;
	public static final int DISMISS_PICKCITY_DIALOG_ID = 0x18884;
	public static final int DISMISS_ADDRESS_DIALOG_ID = 0x18885;
	public static final int GET_WEATHER_DATA_DONE_ID = 0x18886;
	public static final int NO_DATA_ID = 0x18887;
	public static final int SERVER_NO_RESPONSE_ID = 0x18888;
	public static final int GET_CITY_DATA_DONE_ID = 0x18889;

	public static final int SHOW_ID = 0x17774;
	public static final int FAILURE_ID = 0x12274;

	public static final int[] weatherDrawable = { R.drawable.big_rain_dark,
			R.drawable.big_rain_light, R.drawable.clear_dark,
			R.drawable.clear_light, R.drawable.cloudy, R.drawable.dust_dark,
			R.drawable.dust_light, R.drawable.hail, R.drawable.haze,
			R.drawable.icy, R.drawable.fog_dark, R.drawable.fog_light,
			R.drawable.partly_cloudy_dark, R.drawable.partly_cloudy_light,
			R.drawable.small_rain_dark, R.drawable.small_rain_light,
			R.drawable.small_snow, R.drawable.snow, R.drawable.snowrain,
			R.drawable.thunder_dark, R.drawable.thunder_light, R.drawable.wind,
			R.drawable.unknow };

	public interface Defs {
		public final static int VIEW = 0;
		public final static int DELETE = 1;
		public final static int SEND = 2;
		public final static int EXPORT = 3;

		boolean onItemLongClick(AdapterView parent, View v, int position,
				long id);
	}

	public static String[] keypool = { "855e365293104026122808",
			"bd2399e27e104310122808", "4815cb4c38065926122808",
			"f103aae133105734122808", "f8214daa92110024122808",
			"251dd15864145107122908", "4bef3cbd43145804122908",
			"b8f78632fc145949122908", "8191652db5150143122908",
			"3256fe43cf145031122908", "3472015d0e074427122709",
			"a2d91f3a2a074507122709", "8c404d94a9074741122709",
			"8b67f198f7080125122709", "baf936bebe080242122709",
			"3a8bdf616a081232122709", "4c16bf2799081442122709",
			"14d52b6982082333122709", "f4dcf29c30075922122709",
			"3fdf279e83082720122709", "0c40ef79d7074600122709",
			"645ce54eae082638122709", "24b4c29bc0082425122709",
			"9b587590ca082603122709", "9b0670b355082805122709",
			"94afc1d6af082742122709", "d8e2fb0637080642122709" };
	
    public final static int INDEX_BATTERY_A = 0;
    public final static int INDEX_BATTERY_B = 1;
    public final static int INDEX_BATTERY_C = 2;
    public final static int INDEX_BATTERY_D = 3;
    public final static int INDEX_BATTERY_E = 4;
    public final static int INDEX_BATTERY_F = 5;
    public final static int INDEX_BATTERY_G = 6;
    public final static int INDEX_BATTERY_H = 7;
    public final static int INDEX_BATTERY_I = 8;
    public final static int INDEX_BATTERY_J = 9;
    public final static int INDEX_BATTERY_ACHARGE = 10;
    public final static int INDEX_BATTERY_BCHARGE = 11;
    public final static int INDEX_BATTERY_CCHARGE = 12;
    public final static int INDEX_BATTERY_DCHARGE = 13;
    public final static int INDEX_BATTERY_ECHARGE = 14;
    public final static int INDEX_BATTERY_FCHARGE = 15;
    public final static int INDEX_BATTERY_GCHARGE = 16;
    public final static int INDEX_BATTERY_HCHARGE = 17;
    public final static int INDEX_BATTERY_ICHARGE = 18;
    public final static int INDEX_BATTERY_JCHARGE = 19;

    public static final int[] batteryDrawable = {
            R.drawable.batterya, R.drawable.batteryb, R.drawable.batteryc,
            R.drawable.batteryd, R.drawable.batterye, R.drawable.batteryf,
            R.drawable.batteryg, R.drawable.batteryh, R.drawable.batteryi,
            R.drawable.batteryj, R.drawable.batteryacharge, R.drawable.bcharge,
            R.drawable.batteryccharge, R.drawable.batterydcharge, R.drawable.batteryecharge,
            R.drawable.batteryfcharge, R.drawable.batterygcharge, R.drawable.batteryhcharge,
            R.drawable.batteryicharge, R.drawable.batteryjcharge};
    
    public static final String ACTION_CLICK_DATE="mobi.infolife.utils.action.CLICK_WIDGET_DATE";
    public static final String ACTION_LOAD_LIMIT_NUM="mobi.infolife.utils.action.LOAD_LIMIT_NUM";
    public static final String ACTION_CLICK_CALENDAR="mobi.infolife.utils.action.CLICK_WIDGET_CALENDAR";
	public static final int FUNCTION_BATTERY = 0;
	public static final int FUNCTION_ALARM= 1;
	public static final int FUNCTION_XK = 2;
	public static final int FUNCTION_EVENTS = 3;
	public static final int FUNCTION_LUNER_AND_HOLIDAY = 4;
	public static final  String[] functionAdded={"battery", "Alarm", "XK", "Events","LunerAndHoliday"};
}
