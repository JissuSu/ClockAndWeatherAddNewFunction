/**
 * 
 */
package mobi.infolife.utils;

import android.util.Log;

/**
 * @author eong
 *
 */
public class TimeTracker {
	private long start;
	public void start(){
		start = System.currentTimeMillis();
	}
	
	public long getExecuteTime(){
		return System.currentTimeMillis() - start;
	}
	
	public void printExecuteTime(String desc){
		Log.d("TimeTracker1", desc + ":" + getExecuteTime());
	}
}
