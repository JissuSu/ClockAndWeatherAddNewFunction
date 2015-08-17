package mobi.infolife.dateAndDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Created by infolife on 2015/8/5.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    public static String countryName = "ww";

    public DBManager(Context context) {
        helper = new DBHelper(context);
        try {
            helper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = helper.getWritableDatabase();
    }

    public void setCountryName(String name) {
        countryName = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public String buildTableName() {
        StringBuilder builder = new StringBuilder();
        builder.append("holiday_");
        builder.append(countryName);
        return builder.toString();
    }

    //获取与weekday相关的节日
    public Cursor queryWeekdayRelatedHoliday() {
        Cursor cursor = db.query(buildTableName(), null, "weekday_related=1", null, null, null, null);
        return cursor;
    }

    //获取与weekday不相关的节日
    public Cursor queryNotWeekdayRelatedHoliday() {
        Cursor cursor = db.query(buildTableName(), null, "weekday_related=0", null, null, null, null);
        return cursor;
    }

    //获取农历节日
    public Cursor queryLunerHoliday() {
        Cursor cursor = db.query(buildTableName(), null, "isluner=1", null, null, null, null);
        return cursor;
    }

    //获取非农历节日
    public Cursor queryNotLunerHoliday() {
        Cursor cursor = db.query(buildTableName(), null, "isluner=0", null, null, null, null);
        return cursor;
    }

    //获取与weekday相关节日的date_str
    public Cursor queryWeekdayRelatedDateStr() {
        Cursor cursor = db.query(buildTableName(), new String[]{"date_str"}, "weekday_related=1", null, null, null, null);
        return cursor;
    }


}
