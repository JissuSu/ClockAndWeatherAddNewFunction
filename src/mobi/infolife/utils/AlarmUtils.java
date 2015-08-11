package mobi.infolife.utils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import mobi.infolife.cwwidget.UpdateViewService;

public class AlarmUtils {


    private static  Context context;
    private static String nextAlarm;

    public AlarmUtils(Context context){
        this.context = context;
    }

    public static BroadcastReceiver alarmChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context, UpdateViewService.class));
        }
    };

    public static void setNextAlarm(){
        ContentResolver contentResolver = context.getContentResolver();
        nextAlarm = Settings.System.getString(contentResolver,
                Settings.System.NEXT_ALARM_FORMATTED);
        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.NEXT_ALARM_FORMATTED),true,new DatachangeContentObserver(context,new Handler()));
    }


    public static String getNextAlarm(){
        AlarmUtils.setNextAlarm();
        return  nextAlarm;
    }

}
