package mobi.infolife.limitNumAndAir.XKMain;

import com.activeandroid.query.Select;

import java.util.List;

import org.joda.time.DateTime;

import mobi.infolife.limitNumAndAir.XKModel.AirData;
import mobi.infolife.limitNumAndAir.XKModel.LimitCityData;
import mobi.infolife.limitNumAndAir.XKModel.LimitTodayNum;

/**
 * Created by longlong on 15-8-5.
 */
public class XKGetUtil {

	public static AirData getAirQuility(String cityname) {
		List<AirData> airDatalist = new Select().from(AirData.class)
				.where("AD_CITYNAME = ?", cityname).execute();

		if (airDatalist.size() == 0) {
			return null;
		}
		return airDatalist.get(0);
	}

	public static LimitTodayNum getLimitTodayData(String cityname) {

		String dateTime = new DateTime().toString("yyyy-MM-dd");

		List<LimitTodayNum> today = new Select().from(LimitTodayNum.class)
				.where("LTN_NAME = ? and LTN_DATE = ?", cityname, dateTime)
				.execute();
		if (today.size() == 0) {
			return null;
		}

		return today.get(0);

	}

	public static LimitCityData getLimitRuleData(String cityname) {

		List<LimitCityData> rule = new Select().from(LimitCityData.class)
				.where("LCD_NAME = ?", cityname).execute();

		if (rule.size() == 0) {
			return null;
		}
		return rule.get(0);
	}

}
