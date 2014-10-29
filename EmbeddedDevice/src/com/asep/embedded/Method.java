package com.asep.embedded;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

public class Method 
{
	private static int numOfDevice;
	private static Context context;
	//private List<String> mode = new LinkedList<String>();
	private static String[] mode = new String[7],deviceName;
	private static String defaultMessageDialog = "Connecting to SmartHome Device... Please wait...",
			dialogWaiting = defaultMessageDialog, endString="\n", charSplit=" ";
	//private static List<DummyClass> list = new LinkedList<DummyClass>();
	private static boolean[] daylightValue, nightValue, sleepValue, leaveValue, timerValue;
	private static boolean tabletSize;
	private static ProgressDialog pDialog;
	private static StringBuilder sb = new StringBuilder();
	private static String activityName;
	public static String main="main", settingMode="setting_mode",alarm="alarm";
	static Alarm alarmItem;
	public static List<Button> listButton = new LinkedList<Button>();
	
	public static void setNumOfDevice(int device)
	{
		numOfDevice=device;
	}
	public static int getNumOfDevice()
	{
		return numOfDevice;
	}
	public static void setContext(Context Context)
	{
		context = Context;
	}
	public static void setMode(String[] inputMode)
	{
		mode = inputMode;
	}
	public static String[] getMode()
	{
		return mode;
	}
	
	public static void setDaylightValue(boolean[] status)
	{
		daylightValue=status;
	}
	public static void setNightValue(boolean[] status)
	{
		nightValue=status;
	}
	public static void setSleepValue(boolean[] status)
	{
		sleepValue=status;
	}
	public static void setLeaveValue(boolean[] status)
	{
		leaveValue=status;
	}
	public static void setTimerValue(boolean[] status)
	{
		timerValue=status;
	}
	public static void setDeviceName(String[] name)
	{
		deviceName=name;
	}
	public static void setDeviceName(int position, String name)
	{
		deviceName[position]=name;
	}
	public static String getDeviceName(int position)
	{
		return deviceName[position];
	}
	public static void setDeviceType(boolean type)
	{
		tabletSize = type;
	}
	
	public static boolean getDeviceType()
	{
		return tabletSize;
	}
	public void setMessageDialog(String msg)
	{
		dialogWaiting = msg;
	}
	
	public static void setDefaultMessageDialog()
	{
		dialogWaiting = defaultMessageDialog;
	}
	public void setEndString(String string)
	{
		endString = string;
	}	
	public void setCharSplit (String character)
	{
		charSplit = character;
	}
	public static String[] parseData(String data)
	{
		String[] cleanData = data.split(charSplit);
		return cleanData;
	}	
	public static String[] parseData(String data, String split)
	{
		String[] cleanData = data.split(split);
		return cleanData;
	}
	public static int getNumOfDay(String day)
	{
		int numOfDay=0;
		if(day.equalsIgnoreCase("sunday")) numOfDay=1;
		else if(day.equalsIgnoreCase("monday")) numOfDay= 2;
		else if(day.equalsIgnoreCase("tuesday")) numOfDay= 3;
		else if(day.equalsIgnoreCase("wednesday")) numOfDay= 4;
		else if(day.equalsIgnoreCase("thursday")) numOfDay= 5;
		else if(day.equalsIgnoreCase("friday")) numOfDay= 6;
		else if(day.equalsIgnoreCase("saturday")) numOfDay= 7;
		return numOfDay;
	}
	public static void beginDialog()
	{
		pDialog = new ProgressDialog(context);
        pDialog.setMessage(dialogWaiting);
        pDialog.setTitle("Please Wait");
        pDialog.setIndeterminate(false);
        //pDialog.setCancelable(true);
        pDialog.show();
	}
	public static void beginDialog(Context contexts, String message, String title)
	{
		pDialog = new ProgressDialog(contexts);
        pDialog.setMessage(message);
        pDialog.setTitle(title);
        pDialog.setIndeterminate(false);
        //pDialog.setCancelable(true);
        pDialog.show();
	}
	
	public static void closeDialog()
	{
		try
		{
			/*if (pDialog != null && pDialog.isShowing()) 
			{
				//pDialog.cancel();
				pDialog.dismiss();
				pDialog=null;
			}*/
			pDialog.dismiss();
			//pDialog.cancel();
			pDialog=null;
		}
		catch(Exception ex){}
	}
	public static void setActivityName(String name)
	{
		activityName = name;
	}
	public static String getActivityName()
	{
		return activityName;
	}
	public static String filterMessage(String message)
	{
		String sbprint="";
		sb.append(message);                                               
        int endOfLineIndex = sb.indexOf(endString);                            
        if (endOfLineIndex > 0) 
        {                                            
            sbprint = sb.substring(0, endOfLineIndex);
            sb.delete(0, sb.length());
        }
        return sbprint;
	}
	public static void toastShort(Context context,String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	public static void toastShort(String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	public static void toastLong(Context context,String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	public static boolean[] getDaylightValue()
	{
		return daylightValue;
	}
	public static void setDaylightValue(int position, boolean status)
	{
		daylightValue[position]=status;
	}
	public static boolean[] getNightValue()
	{
		return nightValue;
	}
	public static void setNightValue(int position, boolean status)
	{
		nightValue[position]=status;
	}
	public static boolean[] getSleepValue()
	{
		return sleepValue;
	}
	public static void setSleepValue(int position, boolean status)
	{
		sleepValue[position]=status;
	}
	public static boolean[] getLeaveValue()
	{
		return leaveValue;
	}
	public static void setLeaveValue(int position, boolean status)
	{
		leaveValue[position]=status;
	}
	public static boolean[] setTimerValue()
	{
		return timerValue;
	}
	public static void setTimerValue(int position, boolean status)
	{
		timerValue[position]=status;
	}
	public static String[] getDeviceName()
	{
		return deviceName;
	}
	
	public static List<DummyClass> loadSettingPrefMode(String inputMode)
	{
		//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		List<DummyClass> list = new LinkedList<DummyClass>();
		for(int i=0;i<numOfDevice;i++)
		{
			DummyClass item = new DummyClass();
			item.setName(deviceName[i]);
			if(inputMode.equalsIgnoreCase(mode[0]))
			{
				//setDaylightValue[i] = sharedPref.getBoolean("prefOC"+(i+1),false);
				item.setStatus(daylightValue[i]);
			}
			else if(inputMode.equalsIgnoreCase(mode[1]))
			{
				//setNightValue[i] = sharedPref.getBoolean("prefON"+(i+1),false);
				item.setStatus(nightValue[i]);
			}
			else if(inputMode.equalsIgnoreCase(mode[2]))
			{
				item.setStatus(sleepValue[i]);
			}
			else if(inputMode.equalsIgnoreCase(mode[3]))
			{
				item.setStatus(leaveValue[i]);
			}
			else //if(inputMode.equalsIgnoreCase(mode[4]))
			{
				item.setStatus(timerValue[i]);
			}
			/*else if(inputMode.equals(mode[5]))
			{
				
			}
			else //if(inputMode.equals(mode[2]))
			{
				
			}*/
			list.add(item);
		}
		return list;
		/*for(int i=0;i<numOfDevice;i++)
		{
			if(mode.equalsIgnoreCase())
			setDaylightValue[i] = sharedPref.getBoolean("prefOC"+(i+1),false);
			setNightValue[i] = sharedPref.getBoolean("prefON"+(i+1),false);
			setSleepValue[i] = sharedPref.getBoolean("prefOS"+(i+1),false);
			setLeaveValue[i] = sharedPref.getBoolean("prefOL"+(i+1),false);
			if(setDaylightValue[i]) daylightValue[i] = 1; else daylightValue[i] = 0;
			if(setNightValue[i]) nightValue[i] = 1; else nightValue[i] = 0;
			if(setSleepValue[i]) sleepValue[i] = 1; else sleepValue[i] = 0;
			if(setLeaveValue[i]) leaveValue[i] = 1; else leaveValue[i] = 0;
			
			setTimerValue[i] = sharedPref.getBoolean("prefOT"+(i+1),false);
			if(setTimerValue[i]) timerValue[i] = 1; else timerValue[i] = 0;
			for(int j=0;j<numOfAlarm;j++)
			{
				setAlarmValue[j][i] = sharedPref.getBoolean("prefOA"+(j+1)+"."+(i+1),false);
				if(setAlarmValue[j][i]) alarmValue[j][i] = 1; else alarmValue[j][i] = 0;
			}
			name[i] = sharedPref.getString("prefN"+(i+1),""+(i+1));
			String buttonName = "t"+(i+1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i]);
		}
		for(int i=0;i<numOfAlarm;i++)
		{
			name[i+numOfEquipment] = sharedPref.getString("prefA"+(i+1),"Alarm "+(i+1));
			String buttonName = "t"+(i+numberButtonAlarm1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i+numOfEquipment]);
		}*/
	}
	
	public static List<DummyClass> getSettingFromDevice(String[] input)
	{
		//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		List<DummyClass> list = new LinkedList<DummyClass>();
		boolean status;
		for(int i=0;i<numOfDevice;i++)
		{
			DummyClass item = new DummyClass();
			item.setName(deviceName[i]);
			if(input[i+1].equals("0")) status=false;
			else status=true;
			item.setStatus(status);
			list.add(item);
		}
		return list;
		/*for(int i=0;i<numOfDevice;i++)
		{
			if(mode.equalsIgnoreCase())
			setDaylightValue[i] = sharedPref.getBoolean("prefOC"+(i+1),false);
			setNightValue[i] = sharedPref.getBoolean("prefON"+(i+1),false);
			setSleepValue[i] = sharedPref.getBoolean("prefOS"+(i+1),false);
			setLeaveValue[i] = sharedPref.getBoolean("prefOL"+(i+1),false);
			if(setDaylightValue[i]) daylightValue[i] = 1; else daylightValue[i] = 0;
			if(setNightValue[i]) nightValue[i] = 1; else nightValue[i] = 0;
			if(setSleepValue[i]) sleepValue[i] = 1; else sleepValue[i] = 0;
			if(setLeaveValue[i]) leaveValue[i] = 1; else leaveValue[i] = 0;
			
			setTimerValue[i] = sharedPref.getBoolean("prefOT"+(i+1),false);
			if(setTimerValue[i]) timerValue[i] = 1; else timerValue[i] = 0;
			for(int j=0;j<numOfAlarm;j++)
			{
				setAlarmValue[j][i] = sharedPref.getBoolean("prefOA"+(j+1)+"."+(i+1),false);
				if(setAlarmValue[j][i]) alarmValue[j][i] = 1; else alarmValue[j][i] = 0;
			}
			name[i] = sharedPref.getString("prefN"+(i+1),""+(i+1));
			String buttonName = "t"+(i+1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i]);
		}
		for(int i=0;i<numOfAlarm;i++)
		{
			name[i+numOfEquipment] = sharedPref.getString("prefA"+(i+1),"Alarm "+(i+1));
			String buttonName = "t"+(i+numberButtonAlarm1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i+numOfEquipment]);
		}*/
	}
	public static void saveOneSettingDevice(int position, boolean statusButton)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	if(ListDevice.mode.equalsIgnoreCase(mode[0]))
    	{
    		editor.putBoolean("prefOD"+position,statusButton);
    		Method.setDaylightValue(position,statusButton);
    	}
    	else if(ListDevice.mode.equalsIgnoreCase(mode[1]))
    	{
    		editor.putBoolean("prefON"+position,statusButton);
    		Method.setNightValue(position,statusButton);
    	}
    	else if(ListDevice.mode.equalsIgnoreCase(mode[2]))
    	{
    		editor.putBoolean("prefOS"+position,statusButton);
    		Method.setSleepValue(position,statusButton);
    	}
    	else if(ListDevice.mode.equalsIgnoreCase(mode[3]))
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
	
	public static void sendStatusAlarm(int id, char status)
	{
		MainActivity.btc.send(MainActivity.sendAlarm);
		MainActivity.btc.send(ListAlarm.sendStatus);
		MainActivity.btc.send((char) id);
		MainActivity.btc.send(status);
		MainActivity.btc.send(MainActivity.endOfLine);
		//if(status) MainActivity.btc.send((char)0);
		//else MainActivity.btc.send((char)1);
	}
	public static void setAlarmItem(Alarm item)
	{
		alarmItem = item;
	}
	public static Alarm getAlarmItem()
	{
		return alarmItem;
	}
	public static List<DummyClass> loadSettingDeviceAlarmMode()
	{
		//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		List<DummyClass> list = new LinkedList<DummyClass>();
		for(int i=0;i<numOfDevice;i++)
		{
			DummyClass item = new DummyClass();
			item.setName(deviceName[i]);
			item.setStatus(ListAlarm.itemGlobal.getDeviceStatus(i));
			list.add(item);
		}
		return list;
		/*for(int i=0;i<numOfDevice;i++)
		{
			if(mode.equalsIgnoreCase())
			setDaylightValue[i] = sharedPref.getBoolean("prefOC"+(i+1),false);
			setNightValue[i] = sharedPref.getBoolean("prefON"+(i+1),false);
			setSleepValue[i] = sharedPref.getBoolean("prefOS"+(i+1),false);
			setLeaveValue[i] = sharedPref.getBoolean("prefOL"+(i+1),false);
			if(setDaylightValue[i]) daylightValue[i] = 1; else daylightValue[i] = 0;
			if(setNightValue[i]) nightValue[i] = 1; else nightValue[i] = 0;
			if(setSleepValue[i]) sleepValue[i] = 1; else sleepValue[i] = 0;
			if(setLeaveValue[i]) leaveValue[i] = 1; else leaveValue[i] = 0;
			
			setTimerValue[i] = sharedPref.getBoolean("prefOT"+(i+1),false);
			if(setTimerValue[i]) timerValue[i] = 1; else timerValue[i] = 0;
			for(int j=0;j<numOfAlarm;j++)
			{
				setAlarmValue[j][i] = sharedPref.getBoolean("prefOA"+(j+1)+"."+(i+1),false);
				if(setAlarmValue[j][i]) alarmValue[j][i] = 1; else alarmValue[j][i] = 0;
			}
			name[i] = sharedPref.getString("prefN"+(i+1),""+(i+1));
			String buttonName = "t"+(i+1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i]);
		}
		for(int i=0;i<numOfAlarm;i++)
		{
			name[i+numOfEquipment] = sharedPref.getString("prefA"+(i+1),"Alarm "+(i+1));
			String buttonName = "t"+(i+numberButtonAlarm1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i+numOfEquipment]);
		}*/
	}
	public static String getTimeFormat(String hour, String minute)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		Date date=null;
		try {
			date = sdf.parse(hour+":"+minute);
		}catch(Exception ex){}	    
		return sdf.format(date);
	}
	public static String getTimeFormat(int hour, int minute)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		Date date=null;
		try {
			date = sdf.parse(hour+":"+minute);
		}catch(Exception ex){}	    
		return sdf.format(date);
	}
	public static void setButtonStatusAlarm(int position, Button button)
	{
		listButton.set(position, button);
	}
	public static void addButtonStatusAlarm(Button button)
	{
		listButton.add(button);
	}
	public static Button getButtonStatusAlarm(int id)
	{
		int i=0;
		Button button = null;
		while(true)
		{
			button = listButton.get(i);
			if(Integer.parseInt(button.getTag().toString())==id) break;
			else i++;
		}
		return button;
	}
	public static int getButtonPositionAlarm(int id)
	{
		int i=0;
		Button button = null;
		while(true)
		{
			button = listButton.get(i);
			if(Integer.parseInt(button.getTag().toString())==id) break;
			else i++;
		}
		return i;
	}
	public static void clearListButton()
	{
		listButton = new LinkedList<Button>();
	}
	//public static Button get
}
