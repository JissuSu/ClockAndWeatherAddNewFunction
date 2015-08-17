package mobi.infolife.utils;

import android.content.Context;

import mobi.infolife.cwwidget.Preferences;

public class BaseWeatherManager {

    public boolean downloadWeatherData(Context context, int appWidgetId) {
        String url = generateUrlLink(context, appWidgetId);
        CommonUtils.l("URLＮＥＷ：" + url);

        String hl = Preferences.getCurrentCL(context);
        String fileAddr = TaskUtils
                .getWeatherDataFileName(context, appWidgetId);
        String data = TaskUtils.dumpHttpDataToString(url, hl);
        if (data != Constants.NOTSET) {
            TaskUtils.writeInputStringToFile(data, fileAddr);
            return true;
        } else {
            return false;
        }
    }

    String generateUrlLink(Context context, int appWidgetId) {
        return null;
    }

    public String[] parseWeatherData(Context context, int appWidgetId) {
        String data = Preferences.getWeatherData(context, appWidgetId);

        //CommonUtils.l(appWidgetId+" saved weather data is"+data);
        return CommonUtils.stringToStringArray(data);
    }


}

