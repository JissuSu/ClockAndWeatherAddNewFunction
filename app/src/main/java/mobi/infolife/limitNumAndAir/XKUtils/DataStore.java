package mobi.infolife.limitNumAndAir.XKUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStore {

	public static final String SETTING_XH = "setting_xh";
	public static final String FIRST = "first_xh";
	public static final String CITY_NAME = "city_xh";
	public static final String AIR_TIME = "air_time_xh";
	public static final String XH_TIME = "xh_time_xh";
	public static final String LOCATION_TIME = "loc_time_xh";

	public static final boolean STATUS_TRUE = true;
	public static final boolean STATUS_FALSE = false;

	public static String getData(Context context, String choice) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"setting", Context.MODE_PRIVATE);
		String isStore = sharedPreferences.getString(choice, "null");
		return isStore;
	}

	public static void setData(Context context, String choice, String status) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"setting", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(choice, status);
		editor.commit();
	}
}
