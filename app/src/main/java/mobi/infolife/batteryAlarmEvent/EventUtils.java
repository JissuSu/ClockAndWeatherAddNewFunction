package mobi.infolife.batteryAlarmEvent;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Handler;
import android.util.Log;

public class EventUtils {
	
	private static Context context;
    private String nextEvent = "";
    private String dtstart;
    private String allday;
    private String title;
    private String dtend;
    private long _id;
    ContentResolver contentResolver;
    Cursor cursor = null;

    public EventUtils(Context context) {
        this.context = context;
    }

    public void getEvent() {
        String[] projection = new String[]{"title", "allDay", "dtend", "dtstart", "hasAlarm", "_id"};
        Uri calendars = Uri.parse("content://com.android.calendar/events");
        Log.v("WTF", "1");
        contentResolver = context.getContentResolver();
        Log.v("WTF", "2");
        try {
        	cursor = contentResolver.query(calendars, projection,  "dtend>" + System.currentTimeMillis() + "   AND calendar_displayName not like '%Holiday%'    AND  deleted != 1", null, null);
        	Log.v("WTF", "3");
            if (cursor.getCount() != 0&&cursor!=null) {
                SimpleDateFormat sf = null;
                cursor.moveToFirst();
                int titleColumn = cursor.getColumnIndex("title");
                Log.v("WTF", "4");
                int allDayColumn = cursor.getColumnIndex("allDay");
                int dtendColumn = cursor.getColumnIndex("dtend");
                int dtstartColumn = cursor.getColumnIndex("dtstart");
                int _idColumn = cursor.getColumnIndex("_id");
                title = cursor.getString(titleColumn);
                allday = cursor.getString(allDayColumn);
                dtend = cursor.getString(dtendColumn);
                dtstart = cursor.getString(dtstartColumn);
                _id = Long.parseLong(cursor.getString(_idColumn));
                System.out.print(title);

                Date d_dtstart = new Date(Long.parseLong(dtstart));
                Date d_dtend = new Date(Long.parseLong(dtend));
                if (allday.equals("1")) {
                    sf = new SimpleDateFormat("yyyy MM dd");
                    nextEvent = title + "\n" + sf.format(d_dtstart);
                } else {

                    sf = new SimpleDateFormat("hh:mm");
                    nextEvent = title + "\n" + sf.format(d_dtstart) + "-" + sf.format(d_dtend);
                }
            }
        } finally {
            if (cursor != null) {
                try {

                    cursor.close();

                } catch (Exception e) {
                    //ignore this
                }
            }
        }


        contentResolver.registerContentObserver(calendars, true, new DatachangeContentObserver(context, new Handler()));
    }



	public Long getEventId() {
        getEvent();
        return _id;
    }


    public String getNextEvent() {
        getEvent();
        return nextEvent;
    }

}
