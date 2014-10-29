package com.asep.embedded;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.asep.embedded.R;

public class SettingPage extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		if(Method.getDeviceType()) 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		String category = getIntent().getExtras().getString("category").toString();
		if(category.equals(getString(R.string.daylight))) 
		{
			//addPreferencesFromResource(R.xml.settingdaylight);
			Intent i = new Intent(getApplicationContext(),ListDevice.class);
			startActivity(i);	
		}
		else if(category.equals(getString(R.string.night))) addPreferencesFromResource(R.xml.settingnight);
		else if(category.equals(getString(R.string.sleep))) addPreferencesFromResource(R.xml.settingsleep);
		else if(category.equals(getString(R.string.leave))) addPreferencesFromResource(R.xml.settingleave);
		else if(category.equals(getString(R.string.alarm1))) addPreferencesFromResource(R.xml.settingalarm1);
		else if(category.equals(getString(R.string.alarm2))) addPreferencesFromResource(R.xml.settingalarm2);
		else if(category.equals(getString(R.string.timer))) addPreferencesFromResource(R.xml.settingtimer);
		else 
			{
				//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
				//String channel1 = sharedPref.getString("prefN1","1");			 
				addPreferencesFromResource(R.xml.settingchannel);	
			}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		super.onConfigurationChanged(newConfig);
	}
}
