package mobi.infolife.cwwidget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import mobi.infolife.widget.framework.MyAppWidgetManager;

public class CWWidgetFactory {
	private static CWWidgetFactory cwWidgetFactory = null;
	private List<CWWidget> mCWWidgetList = null;
	
	public static CWWidgetFactory getInstance() {
		if (cwWidgetFactory == null) {
			cwWidgetFactory = new CWWidgetFactory();
		}
		
		return cwWidgetFactory;
	}
	
	public List<CWWidget> getCWWidgets(Context context, MyAppWidgetManager myAppWidgetManager) {
		//CommonUtils.l("getCWWidgets");
		if (mCWWidgetList == null) {
			//CommonUtils.l("getCWWidgets init objects");
			mCWWidgetList = new ArrayList<CWWidget>();
			mCWWidgetList.add(new FullWidget(context, myAppWidgetManager));
 			mCWWidgetList.add(new ClockWidget(context, myAppWidgetManager));
 			mCWWidgetList.add(new WeatherWidget(context, myAppWidgetManager));
 
		}
		
		return mCWWidgetList;
	}
}

