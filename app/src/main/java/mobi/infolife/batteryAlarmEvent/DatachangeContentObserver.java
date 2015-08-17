package mobi.infolife.batteryAlarmEvent;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;

import mobi.infolife.cwwidget.LoadAlarmViewService;
import mobi.infolife.cwwidget.LoadEventsViewService;

public class DatachangeContentObserver extends ContentObserver {
	
    private Context context;

    /**
     * Creates a content observer.
     *
//     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DatachangeContentObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }

    public void  onChange(boolean selfChange){
        super.onChange(selfChange);

        context.startService(new Intent(context, LoadAlarmViewService.class));
        context.startService(new Intent(context, LoadEventsViewService.class));
    }

}
