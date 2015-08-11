package mobi.infolife.utils;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import mobi.infolife.cwwidget.UpdateViewService;

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
        context.startService(new Intent(context, UpdateViewService.class));
    }

}
