package mobi.infolife.cwwidget;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class RefreshIntervalListPreferences extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);    
        addPreferencesFromResource(R.xml.interval_setting);    
    }   
}
