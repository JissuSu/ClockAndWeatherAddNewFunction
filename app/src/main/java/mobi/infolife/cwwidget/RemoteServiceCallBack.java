package mobi.infolife.cwwidget;

import android.content.Context;
import android.content.Intent;
import mobi.infolife.widget.framework.MyAppWidgetManager.CallBack;

public class RemoteServiceCallBack implements CallBack {
	private static CallBack mCallBack = null;
	private Context mContext;
	
	public static CallBack getInstance(Context context) {
		if (mCallBack == null) {
			mCallBack = new RemoteServiceCallBack(context);
		}
		
		return mCallBack;
	}
	
	private RemoteServiceCallBack(Context context) {
		mContext = context;
	}

	@Override
	public void onServiceConnected() {
		mContext.startService(new Intent(mContext, UpdateDataService.class));
		mContext.startService(new Intent(mContext, UpdateViewService.class));
	}

	@Override
	public void onServiceDisconnected() {
		mContext.stopService(new Intent(mContext, UpdateDataService.class));
		mContext.stopService(new Intent(mContext, UpdateViewService.class));
	}

}
