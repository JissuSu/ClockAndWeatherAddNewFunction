package mobi.infolife.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import mobi.infolife.cwwidget.Preferences;
import mobi.infolife.cwwidget.R;
import android.content.Context;
import android.os.Environment;
import android.util.Xml;

public class BaseWeatherManager {

	public boolean downloadWeatherData(Context context, int appWidgetId) {
		String url = generateUrlLink(context, appWidgetId);
		CommonUtils.l("URL£Î£Å£×£º"+url);

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
		return CommonUtils.stringToStringArray(data);	}

	
}
// public static boolean generatePureXmlData(String[] weather) {
// File newxmlfile = new
// File(Environment.getExternalStorageDirectory().getAbsolutePath()
// + "/raw.xml");
// CommonUtils.l("Start write file:"+newxmlfile);
//
// try {
// if (!newxmlfile.exists())
// newxmlfile.createNewFile();
// } catch (IOException e) {
//
// e.printStackTrace();
// return false;
// }
// // we have to bind the new file with a FileOutputStream
// FileOutputStream fileos = null;
// try {
// fileos = new FileOutputStream(newxmlfile);
// } catch (FileNotFoundException e) {
// e.printStackTrace();
// return false;
// }
// // we create a XmlSerializer in order to write xml data
// XmlSerializer serializer = Xml.newSerializer();
// try {
// // we set the FileOutputStream as output for the serializer, using
// // UTF-8 encoding
// serializer.setOutput(fileos, "UTF-8");
// // Write <?xml declaration with encoding (if encoding not null) and
// // standalone flag (if standalone not null)
// serializer.startDocument(null, Boolean.valueOf(true));
// // set indentation option
// serializer.setFeature(
// "http://xmlpull.org/v1/doc/features.html#indent-output",
// true);
// // start a tag called "root"
// serializer.startTag(null, "weatherdata");
//
//
// serializer.startTag(null, "TODAY_CONDITION");
// serializer.text(weather[Constants.TODAY_CONDITION_INDEX]);
// serializer.endTag(null, "TODAY_CONDITION");
//
// serializer.startTag(null, "TODAY_WIND");
// serializer.text(weather[Constants.TODAY_WIND_INDEX]);
// serializer.endTag(null, "TODAY_WIND");
//
// serializer.startTag(null, "TODAY_HUMIDITY");
// serializer.text(weather[Constants.TODAY_HUMIDITY_INDEX]);
// serializer.endTag(null, "TODAY_HUMIDITY");
//
// serializer.startTag(null, "TODAY_TEMP");
// serializer.text(weather[Constants.TODAY_TEMP_INDEX]);
// serializer.endTag(null, "TODAY_TEMP");
//
// serializer.startTag(null, "TODAY_LOW");
// serializer.text(weather[Constants.TODAY_LOW_INDEX]);
// serializer.endTag(null, "TODAY_LOW");
//
// serializer.startTag(null, "TODAY_HIGH");
// serializer.text(weather[Constants.TODAY_HIGH_INDEX]);
// serializer.endTag(null, "TODAY_HIGH");
//
// serializer.startTag(null, "FISRTDAY_LOW");
// serializer.text(weather[Constants.FISRTDAY_LOW_INDEX]);
// serializer.endTag(null, "FISRTDAY_LOW");
//
// serializer.startTag(null, "SECONDDAY_LOW");
// serializer.text(weather[Constants.SECONDDAY_LOW_INDEX]);
// serializer.endTag(null, "SECONDDAY_LOW");
//
// serializer.startTag(null, "THIRDDAY_LOW");
// serializer.text(weather[Constants.THIRDDAY_LOW_INDEX]);
// serializer.endTag(null, "THIRDDAY_LOW");
//
// serializer.startTag(null, "FISRTDAY_HIGH");
// serializer.text(weather[Constants.FISRTDAY_HIGH_INDEX]);
// serializer.endTag(null, "FISRTDAY_HIGH");
//
// serializer.startTag(null, "SECONDDAY_HIGH");
// serializer.text(weather[Constants.SECONDDAY_HIGH_INDEX]);
// serializer.endTag(null, "SECONDDAY_HIGH");
//
// serializer.startTag(null, "THIRDDAY_HIGH");
// serializer.text(weather[Constants.THIRDDAY_HIGH_INDEX]);
// serializer.endTag(null, "THIRDDAY_HIGH");
//
// serializer.startTag(null, "FISRTDAY_NAME");
// serializer.text(weather[Constants.FISRTDAY_NAME_INDEX]);
// serializer.endTag(null, "FISRTDAY_NAME");
//
// serializer.startTag(null, "SECONDDAY_NAME");
// serializer.text(weather[Constants.SECONDDAY_NAME_INDEX]);
// serializer.endTag(null, "SECONDDAY_NAME");
//
// serializer.startTag(null, "THIRDDAY_NAME");
// serializer.text(weather[Constants.THIRDDAY_NAME_INDEX]);
// serializer.endTag(null, "THIRDDAY_NAME");
//
// serializer.startTag(null, "TODAY_ICON");
// serializer.text(weather[Constants.TODAY_ICON_INDEX]);
// serializer.endTag(null, "TODAY_ICON");
//
// serializer.startTag(null, "FIRSTDAY_ICON");
// serializer.text(weather[Constants.FIRSTDAY_ICON_INDEX]);
// serializer.endTag(null, "FIRSTDAY_ICON");
//
// serializer.startTag(null, "SECONDDAY_ICON");
// serializer.text(weather[Constants.SECONDDAY_ICON_INDEX]);
// serializer.endTag(null, "SECONDDAY_ICON");
//
// serializer.startTag(null, "THIRDDAY_ICON");
// serializer.text(weather[Constants.THIRDDAY_ICON_INDEX]);
// serializer.endTag(null, "THIRDDAY_ICON");
//
// serializer.endTag(null, "weatherdata");
//
// serializer.endDocument();
// serializer.flush();
// fileos.close();
// } catch (Exception e) {
// CommonUtils.l("Exception when serialize");
//
// e.printStackTrace();
// return false;
// }
// return true;
// }

