package mobi.infolife.cwwidget;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class CWWidgetFactory {
	private static CWWidgetFactory cwWidgetFactory = null;
	private List<CWWidget> mCWWidgetList = null;
	
	public static CWWidgetFactory getInstance() {
		if (cwWidgetFactory == null) {
			cwWidgetFactory = new CWWidgetFactory();
		}
		
		return cwWidgetFactory;
	}
	
	public List<CWWidget> getCWWidgets(Context context) {
		if (mCWWidgetList == null) {
			mCWWidgetList = new ArrayList<CWWidget>();
			mCWWidgetList.add(new FullWidget(context));
 
		}
		
		return mCWWidgetList;
	}
}

