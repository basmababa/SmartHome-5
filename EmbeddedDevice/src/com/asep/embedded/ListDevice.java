package com.asep.embedded;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.asep.embedded.R;
import com.asep.embedded.CustomAdapter.ViewHolder;

public class ListDevice extends Activity {

	ListView listDev;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter adapter;
	//private String[] deviceName;
	List<DummyClass> list = new LinkedList<DummyClass>();
	EditText deviceName;
	String messageDialog, titleDialog="Setting Dialog";
	public static String mode;
	//public static Handler listDeviceHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//settingPageHandler();
		mode = getIntent().getExtras().getString("mode");
		//messageDialog="Get "+mode+" setting mode on device... Please wait...";
		//Method.beginDialog(this, messageDialog, titleDialog);
		if(Method.getDeviceType()) 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			//setContentView(R.layout.list_device);
			//listSetting = (ListView) findViewById(R.id.list);
		}
		else 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//setContentView(R.layout.list_device);
			//listSetting = (ListView) findViewById(R.id.list);
		}
		
		if(mode.equalsIgnoreCase(getString(R.string.change)))
		{
			setTitle(mode.toUpperCase());
			setContentView(R.layout.activity_list_setting);
			listDev = (ListView)findViewById(R.id.list);
			//listToggleDev = (ListView) findViewById(R.id.listViewToggleDevices);
			//setting = Method.getMode();//getResources().getStringArray(R.array.settingMenu);
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,  Method.getDeviceName());
			listDev.setAdapter(adapter);
			//final EditText input = new EditText(this);
			listDev.setOnItemClickListener(new OnItemClickListener()
	        {
	            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
	            { 
	            	//Intent i = new Intent(getApplicationContext(), ListDevice.class); 
	            	//i.putExtra("mode", setting[position]);
	                //startActivity(i);  
	            	showDialogInput(position,Method.getDeviceName(position));
	            	//setListAdapter();
	            	
	            }
	        });
		}
		else if(mode.equalsIgnoreCase(Method.alarm))
		{
			setTitle("MODE "+mode.toUpperCase()+" "+getString(R.string.setting).toUpperCase());
			setContentView(R.layout.list_device);
			listDev = (ListView)findViewById(R.id.listViewToggleDevices);
			list = Method.loadSettingDeviceAlarmMode();
			final CustomAdapter adapter = new CustomAdapter(getApplicationContext());
			listDev.setAdapter(adapter);
			adapter.updateList(list);
			adapter.notifyDataSetChanged();
			adapter.notifyDataSetInvalidated();
		}
		else
		{
			setTitle("MODE "+mode.toUpperCase()+" "+getString(R.string.setting).toUpperCase());
			setContentView(R.layout.list_device);
			listDev = (ListView)findViewById(R.id.listViewToggleDevices);
			//adapterToggle = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,  setting);
			//listSetting.setAdapter(adapter);
			list = Method.loadSettingPrefMode(mode);
			/*for(int i=0;i<5;i++)
			{
				DummyClass item = new DummyClass();
				item.setName("Perangkat " + i);
				if(i%2!=0) item.setStatus(true);
				else item.setStatus(false);
				list.add(item);
				//DummyClass item1 = list.get(0);
			}*/
			final CustomAdapter adapter = new CustomAdapter(getApplicationContext());
			listDev.setAdapter(adapter);
			adapter.updateList(list);
			/*listDev.setOnItemClickListener(new OnItemClickListener()
	        {
	            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
	            { 
	            	//int a = listDev.getCount();
	            	//DummyClass b = adapter.getItem(0);
	            	LinearLayout lLayout = (LinearLayout) arg1;

	                *//** Getting the toggle button corresponding to the clicked item *//*
	                Switch a = (Switch) lLayout.getChildAt(1);
	            	//AdapterView<Adapter> a =  (AdapterView<Adapter>) listDev.getItemAtPosition(0);
	            	//View s = (View) listDev.getItemAtPosition(0);
	            
	            	//View b =  (View) listDev.getItemAtPosition(0);
	            	//ToggleButton f = (ToggleButton) 
	            	//ToggleButton c = (ToggleButton) ((ViewGroup) b).getChildAt(1);
	            	//ToggleButton c = (ToggleButton) b.getStatus();
	            	//boolean d = f.isChecked();
	            	//boolean d = c.isChecked();
	            	Toast.makeText(getApplicationContext(), position+" "+a.isChecked(), Toast.LENGTH_SHORT).show();
	            	//Switch switchBTN = ViewHolder.get(arg1, R.id.switchBTN);
	            	//boolean statusButton = switchBTN.isChecked();
	            	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	            	SharedPreferences.Editor editor = sharedPref.edit();
	            	if(mode.equalsIgnoreCase(getString(R.string.daylight)))
	            	{
	            		editor.putBoolean("prefOD"+position,statusButton);
	            		Method.setDaylightValue(position,statusButton);
	            	}
	            	else if(mode.equalsIgnoreCase(getString(R.string.night)))
	            	{
	            		editor.putBoolean("prefON"+position,statusButton);
	            		Method.setNightValue(position,statusButton);
	            	}
	            	else if(mode.equalsIgnoreCase(getString(R.string.sleep)))
	            	{
	            		editor.putBoolean("prefOS"+position,statusButton);
	            		Method.setSleepValue(position,statusButton);
	            	}
	            	else if(mode.equalsIgnoreCase(getString(R.string.leave)))
	            	{
	            		editor.putBoolean("prefOL"+position,statusButton);
	            		Method.setLeaveValue(position,statusButton);
	            	}
	            	else //if(mode.equalsIgnoreCase(getString(R.string.timer)))
	            	{
	            		editor.putBoolean("prefOT"+position,statusButton);
	            		Method.setTimerValue(position,statusButton);
	            	}
	            	editor.commit();
	            }
	        });*/
		}
	}
	
	/*@Override
	public void onResume() 
	{
		super.onResume();
		Method.setActivityName(Method.settingMode);
		MainActivity.btc.send(MainActivity.sendSetting);
		if(mode.equalsIgnoreCase(getString(R.string.daylight)))
    	{			
			MainActivity.btc.send(MainActivity.modeDaylight);
    	}
    	else if(mode.equalsIgnoreCase(getString(R.string.night)))
    	{
    		MainActivity.btc.send(MainActivity.modeNight);
    	}
    	else if(mode.equalsIgnoreCase(getString(R.string.sleep)))
    	{
    		MainActivity.btc.send(MainActivity.modeSleep);
    	}
    	else //if(mode.equalsIgnoreCase(getString(R.string.leave)))
    	{
    		MainActivity.btc.send(MainActivity.modeLeave);
    	}
		MainActivity.btc.send(MainActivity.endOfLine);
    	else //if(mode.equalsIgnoreCase(getString(R.string.timer)))
    	{
    		editor.putBoolean("prefOT"+position,statusButton);
    		Method.setTimerValue(position,statusButton);
    	}	
	}*/
	
	/*@SuppressLint("HandlerLeak")
	private void settingPageHandler()
	{
		listDeviceHandler = new Handler()
		{
			public void handleMessage(android.os.Message msg) 
			{
				try
				{
					String b = (String) msg.obj;
					String[] result = Method.parseData(b);
					final int id = Integer.parseInt(result[0]);
					if(b.equals("")){}
					else if(id==MainActivity.sendSetting)
					{
						Method.closeDialog();
						list = Method.getSettingFromDevice(result);
						CustomAdapter adapter = new CustomAdapter(getApplicationContext());
						listDev.setAdapter(adapter);
						adapter.updateList(list);
						listDev.setOnItemClickListener(new OnItemClickListener()
				        {
				            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
				            { 
				            	Switch switchBTN = ViewHolder.get(arg1, R.id.switchBTN);
				            	boolean statusButton = switchBTN.isChecked();
				            	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				            	SharedPreferences.Editor editor = sharedPref.edit();
				            	if(mode.equalsIgnoreCase(getString(R.string.daylight)))
				            	{
				            		editor.putBoolean("prefOD"+position,statusButton);
				            		Method.setDaylightValue(position,statusButton);
				            	}
				            	else if(mode.equalsIgnoreCase(getString(R.string.night)))
				            	{
				            		editor.putBoolean("prefON"+position,statusButton);
				            		Method.setNightValue(position,statusButton);
				            	}
				            	else if(mode.equalsIgnoreCase(getString(R.string.sleep)))
				            	{
				            		editor.putBoolean("prefOS"+position,statusButton);
				            		Method.setSleepValue(position,statusButton);
				            	}
				            	else if(mode.equalsIgnoreCase(getString(R.string.leave)))
				            	{
				            		editor.putBoolean("prefOL"+position,statusButton);
				            		Method.setLeaveValue(position,statusButton);
				            	}
				            	else //if(mode.equalsIgnoreCase(getString(R.string.timer)))
				            	{
				            		editor.putBoolean("prefOT"+position,statusButton);
				            		Method.setTimerValue(position,statusButton);
				            	}
				            	editor.commit();
				            	//TextView textViewItem = ((TextView) view.findViewById(R.id.textViewItem));
				            }
				        });
					}
					
				}
				catch(Exception ex){}
			}
		};
	}*/
	
	private void showDialogInput(final int number, String name)
	{
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

	        alert.setTitle("Device Name "+(number+1));
	        final EditText deviceName = new EditText(this);
	        deviceName.setText(name);
	        alert.setView(deviceName);
	        //alert.setView(password);

	        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	        	String input = deviceName.getEditableText().toString();
	        	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	        	SharedPreferences.Editor editor = sharedPref.edit();
	        	editor.putString("prefN"+number,input);
	    		editor.commit();
	    		Method.setDeviceName(number, input);
	    		//setContentView(R.layout.activity_list_setting);
	    		//listDev = (ListView)findViewById(R.id.list); 
	    		updateListAdapter();		
	        }
	        });
	        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton){}
		    });
	        alert.show();
	}
	
	private void updateListAdapter()
	{
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,  Method.getDeviceName());
		listDev.setAdapter(adapter);	  
	}
	
	/*@Override
	public void onBackPressed() 
	{		
		super.onBackPressed();	
	}*/
}