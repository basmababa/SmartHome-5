package com.asep.embedded;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.asep.embedded.R;

public class ListSetting extends Activity {
	//String[] setting = getResources().getStringArray(R.array.settingMenu);
		private ListView listSetting;
		private ArrayAdapter adapter;
		private String[] setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		if(Method.getDeviceType()) 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.activity_list_setting);
			listSetting = (ListView) findViewById(R.id.list);
		}
		else 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.activity_list_setting);
			listSetting = (ListView) findViewById(R.id.list);
		}
		//Resources res = getResources();
		setting = Method.getMode();//getResources().getStringArray(R.array.settingMenu);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,  setting);
		listSetting.setAdapter(adapter);
		
		listSetting.setOnItemClickListener(new OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            { 
            	Intent i = new Intent(getApplicationContext(), ListDevice.class); 
            	i.putExtra("mode", setting[position]);
                startActivity(i);  
            }
        });
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		super.onConfigurationChanged(newConfig);
	}
}
