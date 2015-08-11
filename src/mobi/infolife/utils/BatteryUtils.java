package mobi.infolife.utils;

import mobi.infolife.cwwidget.UpdateViewService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryUtils {
    private static int currentBatteryLevel;
    private static int currentBatteryStatus;
    public static boolean ifContainBattery;
    
    public static BroadcastReceiver batteryReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentBatteryLevel=intent.getIntExtra("level", 0);
            currentBatteryStatus=intent.getIntExtra("status", 0);
        }
    };

    public static BroadcastReceiver powerConnectedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	context.startService(new Intent(context, UpdateViewService.class));
        }
    };

    public static BroadcastReceiver powerDisconnectedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	context.startService(new Intent(context, UpdateViewService.class));
        }
    };

    public static BroadcastReceiver screenOrientationChangedReceiver=new BroadcastReceiver() {
        public void onReceive(Context context,Intent intent){
        	context.startService(new Intent(context, UpdateViewService.class));
        }
    };

    public static int getBatteryImageId() {
        if(currentBatteryStatus==2||currentBatteryStatus==5)
        {
            if(currentBatteryLevel>=95)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_JCHARGE];
            }
            else if(currentBatteryLevel>=85&& currentBatteryLevel<95)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_ICHARGE];
            }
            else if(currentBatteryLevel>=75&& currentBatteryLevel<85)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_HCHARGE];
            }
            else if(currentBatteryLevel>=65&& currentBatteryLevel<75)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_GCHARGE];
            }
            else if(currentBatteryLevel>=55&& currentBatteryLevel<65)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_FCHARGE];
            }
            else if(currentBatteryLevel>=45&& currentBatteryLevel<55)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_ECHARGE];
            }
            else if(currentBatteryLevel>=35&& currentBatteryLevel<45)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_DCHARGE];
            }
            else if(currentBatteryLevel>=25&& currentBatteryLevel<35)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_CCHARGE];
            }
            else if(currentBatteryLevel>=15&& currentBatteryLevel<25)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_BCHARGE];
            }
            else
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_ACHARGE];
            }
        }
        else
        {
            if(currentBatteryLevel>=95)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_J];
            }
            else if(currentBatteryLevel>=85&& currentBatteryLevel<95)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_I];
            }
            else if(currentBatteryLevel>=75&& currentBatteryLevel<85)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_H];
            }
            else if(currentBatteryLevel>=65&& currentBatteryLevel<75)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_G];
            }
            else if(currentBatteryLevel>=55&& currentBatteryLevel<65)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_F];
            }
            else if(currentBatteryLevel>=45&& currentBatteryLevel<55)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_E];
            }
            else if(currentBatteryLevel>=35&& currentBatteryLevel<45)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_D];
            }
            else if(currentBatteryLevel>=25&& currentBatteryLevel<35)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_C];
            }
            else if(currentBatteryLevel>=15&& currentBatteryLevel<25)
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_B];
            }
            else
            {
                return Constants.batteryDrawable[Constants.INDEX_BATTERY_A];
            }
        }
    }

    public static int getCurrentBatteryLevel(){
        return  currentBatteryLevel;
    }
}
