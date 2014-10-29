package com.asep.embedded;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.asep.embedded.R;

public class ListAlarm extends Activity {

	private ListView listview;
	Button addAlarm;
	List<Alarm> listAlarm = new LinkedList<Alarm>();
	public static Handler alarmHandler;
	public static char request=0,receive=1, sendNewAlarm=2, sendEditAlarm=3, sendDeleteAlarm=4,sendStatus=5, sendStatusCompleted=6,beginNumOfDevice=6;
	String message="Please wait... Get the list of alarms...", title="Dialog Alarm";
	static final int ALARM_DIALOG_ID = 999, maxAlarmID=255;
	private int	hour, minute, positionAdapter;
	boolean formatTime=false, alarmStatus=false, flagOpenAlarm=false, flagDevice=false;;
	public static boolean flagEdit=false;
	List<Boolean> listDeviceAlarm;
	Switch alarmStatusSwitch;
	LinearLayout timeAlarmLin;
	TextView configuration, timeAlarm; 
	public static Alarm itemGlobal;
	CustomAdapterAlarm adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingAlarmHandler();
		setContentView(R.layout.list_alarm);
		initUI();
		Method.setContext(this);
		if(Method.getDeviceType()) 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		/*try {
			Thread.sleep(timeDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//adapter = new CustomAdapterAlarm(getApplicationContext());
		//listview.setAdapter(adapter);
	}

	private void settingAlarmHandler()
	{
		alarmHandler = new Handler()
		{
			public void handleMessage(android.os.Message msg) 
			{
				try
				{
					String text = (String) msg.obj;
					final String[] result = Method.parseData(text);
					final int id = Integer.parseInt(result[0]);
					if(text.equals("")){}
					else if(id==MainActivity.sendAlarm)
					{
						final int command = Integer.parseInt(result[1]);
						if(command==receive)
						{
							if(result[2].equals("0")) 
							{
								runOnUiThread(new Runnable() {								
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Method.closeDialog();
										adapter.updateList(listAlarm);
										Method.toastShort(getApplicationContext(), "Tidak ada alarm yang tersimpan pada device");
									}
								});
							}
							else if(Integer.parseInt(result[2])==maxAlarmID) 
							{
								
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Method.closeDialog();
										Method.clearListButton();
										for(int i=0;i<(listAlarm.size()+1);i++) Method.listButton.add(null);
										adapter.updateList(listAlarm);
										listview.setOnItemClickListener(new OnItemClickListener()
								        {
								            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
								            { 
								            	if(MainActivity.btc.getStatusConnect())
								            	{
								            		flagEdit = true;
								            		positionAdapter = position;
								            		openAlarmSettingPage();
								            	}
								            	else displayFailed();
								            }
								        });	
									}
								});
							}
							else
							{
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Alarm item = new Alarm();
										List<Boolean> list = new LinkedList<Boolean>();
										item.setId(Integer.parseInt(result[2]));
										if(result[3].equals("0")) item.setStatus(false);
										else item.setStatus(true);
										item.setHour(result[4]);
										item.setMinute(result[5]);	
										for(int i=0;i<Method.getNumOfDevice();i++) 
										{
											if(result[beginNumOfDevice].charAt(i)=='0')list.add(false);
											else list.add(true);
										}
										item.setDeviceStatus(list);
										listAlarm.add(item);
										
										if(MainActivity.btc.getStatusConnect())
										{
											MainActivity.btc.send(MainActivity.sendAlarm);
											MainActivity.btc.send(request);
											MainActivity.btc.send(MainActivity.endOfLine);
										}
										else displayFailed();
									}
								});
							}	
						}
						else if(command==sendStatusCompleted) 
						{
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									sendRequestListAlarm();
								}
							});
							
						}
						else if(command==sendStatus) 
						{
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									setStatusAlarmButton(result[2],result[3]);
								}
							});							
						}
					}
					else if(id==MainActivity.receiveAllDevice) 
					{
						MainActivity.hardwareHandler.obtainMessage(1,text).sendToTarget();
					}
				}
				catch(Exception ex){}
			}
		};
	}
	
	private void setStatusAlarmButton(String id, String status)
	{
		int idInt = Integer.parseInt(id);
		Button button = Method.getButtonStatusAlarm(idInt);
		int position = Method.getButtonPositionAlarm(idInt);
		Alarm item = adapter.getItem(position);
		//Drawable d = findViewById(resID).getBackground();
		Drawable d = button.getBackground();
		PorterDuffColorFilter filter;
		if(status.equalsIgnoreCase("0"))
		{
			button.setText("OFF");
			//button.setBackgroundColor(getResources().getColor(R.color.red));		
	        filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
	        item.setStatus(false);
		}
		else
		{
			button.setText("ON");
			//button.setBackgroundColor(getResources().getColor(R.color.green));
			filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
			item.setStatus(true);
			//if(id==9) Method.toastShort("Alarm telah diaktifkan");
		}
		d.setColorFilter(filter);
		//listAlarm = adapter.getList();
		//listAlarm.set(position, item);
		//initUI();
		//adapter.updateList(listAlarm);
		//listAlarm = new LinkedList<Alarm>();
		//setContentView(R.layout.list_alarm);
		//setContentView(R.layout.list_alarm);
		//initUI();
		//adapter.updateList(listAlarm);
		adapter.setAdapterItem(position, item);
	}
	
	int findMaxID()
	{
		int maxID=0;
		for(int i=0;i<adapter.getCount();i++)
		{
			itemGlobal = adapter.getItem(i);
			int id = itemGlobal.getId();
			if(maxID<id) maxID=id;
		}
		maxID++;
		return maxID;
	}
	
	/** Saving the current state of the activity
	    * for configuration changes [ Portrait <=> Landscape ]
	    *//*
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putBooleanArray("status", status);
	    }*/
	
	@Override
	public void onResume() 
	{
		super.onResume();
		if(Method.getDeviceType()) 
		{
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, MainActivity.defScreenTimeOut);
		}
		if(!flagDevice)sendRequestListAlarm();
		else flagDevice=false;
		MainActivity.flagOff=false;
		/*Alarm item2 = new Alarm();
		item2.setHour("20");
		item2.setMinute("05");
		item2.setName("Bangun");
		item2.setId(1);
		item2.setStatus(false);
		for(int i=0;i<Method.getNumOfDevice();i++) item2.addDeviceStatus(false); 
		listAlarm.add(item2);
		adapter = new CustomAdapterAlarm(getApplicationContext());
		listview.setAdapter(adapter);
		adapter.updateList(listAlarm);
		listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            { 
            	//if(MainActivity.btc.getStatusConnect())
            	{
            		flagEdit = true;
            		positionAdapter = position;
            		openAlarmSettingPage();
            		//Method.setAlarmItem(item);
            		//Intent setting = new Intent(getApplicationContext(), ListSetting.class);     
            		//startActivity(setting);
            		flagEdit=true;
            		listDeviceAlarm = new LinkedList<Boolean>();
            		listDeviceAlarm = item.getDeviceStatus();
	            	hour = Integer.parseInt(item.getHour());
	            	minute = Integer.parseInt(item.getMinute());
	            	idAlarm = item.getId();
	            	showDialog(ALARM_DIALOG_ID);
            	}
            	//else displayFailed();
            	//Switch switchBTN = ViewHolder.get(arg1, R.id.switchAlarm);
            	//boolean statusButton = switchBTN.isChecked();
            	//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            	//SharedPreferences.Editor editor = sharedPref.edit();
            }
        });*/	
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MainActivity.flagOff=true;
	}
	
	void openAlarmSettingPage()
	{
		flagOpenAlarm = true;
		setContentView(R.layout.settingalarm);
		alarmStatusSwitch = (Switch) findViewById(R.id.switchAlarmBTN);
		timeAlarmLin = (LinearLayout) findViewById(R.id.linTimeAlarm);
		timeAlarm = (TextView) findViewById(R.id.textTimeAlarm);
		configuration = (TextView) findViewById(R.id.linConfiguration);
		
		if(flagEdit)
		{
			itemGlobal = adapter.getItem(positionAdapter);
			hour = Integer.parseInt(itemGlobal.getHour());
	    	minute = Integer.parseInt(itemGlobal.getMinute());
	    	
	    	//idAlarm = itemGlobal.getId();
			alarmStatusSwitch.setChecked(itemGlobal.getStatus());
			timeAlarm.setText(Method.getTimeFormat(itemGlobal.getHour(), itemGlobal.getMinute()));
		}
		else
		{
			itemGlobal = new Alarm();
			//itemGlobal.setName("Bangun");
			if(adapter.getCount()==0) itemGlobal.setId(1);
			else 
			{
				int max = findMaxID();
				itemGlobal.setId(max);
			}
			itemGlobal.setStatus(true);
			itemGlobal.clearDeviceStatus();
			for(int i=0;i<Method.getNumOfDevice();i++) itemGlobal.addDeviceStatus(false); 	
			String timeNow = getTime();
			itemGlobal.setHour(""+hour);
			itemGlobal.setMinute(""+minute);	
	    	//idAlarm = itemGlobal.getId();
			alarmStatusSwitch.setChecked(itemGlobal.getStatus());
			timeAlarm.setText(timeNow);
			showDialog(ALARM_DIALOG_ID);
		}
		alarmStatusSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				itemGlobal.setStatus(alarmStatusSwitch.isChecked());
				
			}
		});
		timeAlarmLin.setOnClickListener(new OnClickListener() {					
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(flagEdit) showDialog(ALARM_DIALOG_ID);
				else
				{
					getTime();
					showDialog(ALARM_DIALOG_ID);
				}
			}
		});
		configuration.setOnClickListener(new OnClickListener() {					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flagDevice=true;
				Intent setting = new Intent(getApplicationContext(), ListDevice.class);  
				setting.putExtra("mode", Method.alarm);
        		startActivity(setting);
			}
		});
	}
	
	String getTime()
	{
		Calendar c = Calendar.getInstance();
		/*day = c.get(Calendar.DAY_OF_WEEK);
		date = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH)+1;
		year = c.get(Calendar.YEAR) - 2000;*/
	    Date clockNow = c.getTime();
	    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
	    String now = formatter.format(clockNow);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		return now;
	}
	
	private void initUI()
	{
		listview = (ListView) findViewById(R.id.listAlarm);
		adapter = new CustomAdapterAlarm(getApplicationContext());
		listview.setAdapter(adapter);	
		registerForContextMenu(listview);
	}
	
	@SuppressLint("SimpleDateFormat")
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) 
		{
			hour = selectedHour;
			minute = selectedMinute;
			timeAlarm.setText(Method.getTimeFormat(hour,minute));
			itemGlobal.setHour(String.valueOf(hour));
			itemGlobal.setMinute(String.valueOf(minute));
			/*if(MainActivity.btc.getStatusConnect())
			{				
				if(flagEdit)
				{
					MainActivity.btc.send(MainActivity.sendAlarm);
					MainActivity.btc.send(sendAllAlarm);				
					MainActivity.btc.send((char)idAlarm);
					MainActivity.btc.send((char)hour);
					MainActivity.btc.send((char)minute);
					for(int i=0;i<Method.getNumOfDevice();i++) 
					{
						if(listDeviceAlarm.get(i)) MainActivity.btc.send((char)1);
						else MainActivity.btc.send((char)0);
					}
					MainActivity.btc.send(MainActivity.endOfLine);
					//Tambahin Progress Dialog, nunggu konfirmasi dari Hardware apakah alarmnya sudah ditambah atau belum
					//writeTime();
				}
			}
			else displayFailed();*/
		}
	};
	
	public void addNewAlarm(View v)
	{		
		flagEdit=false;
		getTime();
		openAlarmSettingPage();
	}

	public void saveAlarmSetting(View v)
	{		
		if(MainActivity.btc.getStatusConnect())
		{
			MainActivity.btc.send(MainActivity.sendAlarm);
			if(flagEdit)MainActivity.btc.send(sendEditAlarm);	
			else MainActivity.btc.send(sendNewAlarm);	
			MainActivity.btc.send((char)itemGlobal.getId());
			if(itemGlobal.getStatus())MainActivity.btc.send((char)1);
			else MainActivity.btc.send((char)0);
			MainActivity.btc.send((char)Integer.parseInt(itemGlobal.getHour()));
			MainActivity.btc.send((char)Integer.parseInt(itemGlobal.getMinute()));
			for(int i=0;i<Method.getNumOfDevice();i++) 
			{
				if(itemGlobal.getDeviceStatus(i)) MainActivity.btc.send((char)1);
				else MainActivity.btc.send((char)0);
			}
			MainActivity.btc.send(MainActivity.endOfLine);
		}
		else displayFailed();
		backToAlarmSettingPage();
	}
	public void cancelAlarmSetting(View v)
	{		
		backToAlarmSettingPage();
		sendRequestListAlarm();
	}
	public void refreshAlarm(View v)
	{		
		sendRequestListAlarm();	
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) 
		{
			case ALARM_DIALOG_ID:
				return new TimePickerDialog(this,timePickerListener, hour, minute,formatTime);
		}
		return null;
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_alarm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	void displayFailed()
	{
		Method.toastShort(getApplicationContext(), MainActivity.textFailedConnect);
	}
	
	@Override
	public void onBackPressed() {
		backToAlarmSettingPage();	
		sendRequestListAlarm();
	}
	
	void sendRequestListAlarm()
	{
		if(MainActivity.btc.getStatusConnect())
		{
			//setContentView(R.layout.list_alarm);
			//initUI();
			//listview = (ListView) findViewById(R.id.listAlarm);
			//adapter = new CustomAdapterAlarm(getApplicationContext());
			//listview.setAdapter(adapter);	
			//registerForContextMenu(listview);
			listAlarm = new LinkedList<Alarm>();
			Method.beginDialog(this, message, title);
			MainActivity.btc.send(MainActivity.sendAlarm);
			MainActivity.btc.send(request);
			MainActivity.btc.send(MainActivity.endOfLine);
		}
		else displayFailed();
	}
	
	void backToAlarmSettingPage()
	{
		if(flagOpenAlarm) 
		{
			setContentView(R.layout.list_alarm);
			initUI();
			//Method.clearListButton();
			//for(int i=0;i<(listAlarm.size()+1);i++) Method.listButton.add(null);
			//adapter.updateList(listAlarm);
			flagOpenAlarm=false;
		}
		else finish();
	}
	 @Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,  
	             ContextMenuInfo menuInfo) {  
	        // TODO Auto-generated method stub  
	        super.onCreateContextMenu(menu, v, menuInfo);  
	        MenuInflater m = getMenuInflater();  
	        m.inflate(R.menu.menu_alarm, menu);  
	        menu.setHeaderTitle("Select The Action");   
	   }  
	    @Override  
	   public boolean onContextItemSelected(MenuItem item) {  
	    	switch(item.getItemId()){  
	             case R.id.deleteAlarm:  
	            	 AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();  
	                   positionAdapter = (int) info.id;  
			                   AlertDialog.Builder builder = new AlertDialog.Builder(ListAlarm.this);
			               	builder
			           	    	.setTitle(getString(R.string.titleDialogDelete))
			           	    	.setMessage(getString(R.string.confirm))
			           	    	.setIcon(android.R.drawable.ic_dialog_alert)
			           	    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			           	    	{
			           	    		public void onClick(DialogInterface dialog, int which) 
			           	    	    {			      	
			           	    	    	deleteAlarm();
			           	    	    }
			           	    	})
			               	.setNegativeButton("No", null)
			               	.show();
	                   
	                   //list.remove(position);  
	                  //this.adapter.notifyDataSetChanged();  
	                  return true;  
		   case R.id.editAlarm: 
			   info = (AdapterContextMenuInfo) item.getMenuInfo();  
	           positionAdapter = (int) info.id; 
	           flagEdit=true;
	           openAlarmSettingPage();
	           return true;  
	        }  
	        return super.onContextItemSelected(item);  
	   }  
	    
	    void deleteAlarm()
	    {
	    	if(MainActivity.btc.getStatusConnect())
	    	{
	    		itemGlobal = adapter.getItem(positionAdapter);
	    		MainActivity.btc.send(MainActivity.sendAlarm);
		    	MainActivity.btc.send(sendDeleteAlarm);
		    	MainActivity.btc.send((char)itemGlobal.getId());
		    	MainActivity.btc.send(MainActivity.endOfLine);
	    	}
	    	else displayFailed();
	    }
}
