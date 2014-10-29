package com.asep.embedded;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.asep.embedded.R;


public class MainActivity extends Activity implements SensorEventListener{
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private WindowManager mWindowManager;
	private WakeLock w;
	private KeyguardLock kl;
	public static BluetoothConnection btc = new BluetoothConnection();
	//private int delayBluetooth=2800;
	/*
	#define idDevice 1
	#define idLedDimmer 2
	#define idLedRGB 3
	#define idLedRed 0
	#define idLedGreen 1
	#define idLedBlue 2
	#define idPrintStatus 4
	#define idStatusInfo 5
	#define idConnect 6
	#define idDisconnect 7
	*/
	public static char sendDevice=1, sendLedDimmer=2, sendLedRGB=3, idLedRed=0, idLedGreen=1,idLedBlue=2,
					   sendConnect=6, sendDisconnect=7, sendAlarm=2, sendTimer=3, receiveAllDevice=4,idMode=4, modeDaylight=0,modeNight=1, modeSleep=2, modeLeave=3,
				 receiveTimeRTC=5, receiveStatusInfo=5, sendLed=7, setAlarmClock=8, setTimerClock=9, sendSetting=10, sendDummy=29, endOfLine=105,
				 sendAlarm1=0,sendAlarm2=1, 
				 sendInfoAlarm1=0, sendInfoAlarm2=1, sendInfoTimer=2, sendInfoRTC=3,
				 sendBeginDevice=0,sendBeginAlarm1=1,sendBeginAlarm2=2,sendBeginTimer=3;
	private int	numOfEquipment=1, numOfAlarm=2, hour, minute, second, day, date, month, year,timeOut=4,
			    numberButtonAlarm1=8, numberButtonAlarm2=9, counterAlarm=0,delayCheckConnection=2000, numOfButton=9;
	public static int defScreenTimeOut=180000;
			    //numButtonNotUsed=numOfButton-numOfEquipment;
	private String mode, textAlarmInfo="", textTimerInfo = "",
						 textMessageStatusInfo = "Get status of Device... Please Wait...",
						 defaultMessageDialog = "Connecting to Device... Please wait...";
	public static String textFailedConnect="Android belum terkoneksi dengan Device!!!";
	private String[] result, name = new String[numOfEquipment];
	public boolean formatTime=false, flagOnDisconnect=false, flagOnFinish=false, dialogStatusOpen=false, flagReceive=false,
				   flagBegin=false, statusTimer=false, tabletSize=false, flagSetting=false, flagOpenAlarm=false;
	public static boolean flagOff=false;
	public static Handler hardwareHandler;
	public Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18, daylight, night, sleep,leave;
	private TextView statusKoneksi;
	private boolean[] setDaylightValue = new boolean[numOfEquipment];
	private boolean[] setNightValue = new boolean[numOfEquipment];
	private boolean[][] setAlarmValue = new boolean[numOfAlarm][numOfEquipment];
	private boolean[] setSleepValue = new boolean[numOfEquipment];
	private boolean[] setLeaveValue = new boolean[numOfEquipment];
	private boolean[] setTimerValue = new boolean[numOfEquipment];
	//private char[] daylightValue = new char[numOfEquipment];
	//private char[] nightValue = new char[numOfEquipment];
	//private char[] sleepValue = new char[numOfEquipment];
	//private char[] leaveValue = new char[numOfEquipment];
	private char[][] alarmValue = new char[numOfAlarm][numOfEquipment];
	//private char[] timerValue = new char[numOfEquipment];
	private char handshakeCode='a', exitCode='b';
	static final int ALARM_DIALOG_ID = 999, TIMER_DIALOG_ID = 111;
	final Context context = this;
	private ProgressDialog pDialog;
	private String dialogWaiting = defaultMessageDialog;
	SeekBar seekbarLedDimmer;
	VerticalSeekBar seekbarRed, seekbarGreen, seekbarBlue;
	TextView textRed, textGreen, textBlue, textDimmer;
	
	
	//SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
	//int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
	//long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Method.setDeviceType(getResources().getBoolean(R.bool.isTablet));
		tabletSize = Method.getDeviceType();
		if(tabletSize) 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			//setContentView(R.layout.activity_main);
			setContentView(R.layout.activity_main_multidevice);
			mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
			mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
			
			 KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			 kl = km.newKeyguardLock("MyKeyguardLock");
			 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			 w = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					             | PowerManager.ACQUIRE_CAUSES_WAKEUP
					             | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
			//numberButtonAlarm1=17; numberButtonAlarm2=18; numOfButton=18; 
			//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		}
		else  
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//setContentView(R.layout.activity_main);
			setContentView(R.layout.activity_main);
			//numberButtonAlarm1=17; numberButtonAlarm2=18; numOfButton=18; 
		}
		initUI();
		loadSeekBarRGB();
		Method.setContext(this);
		Method.setNumOfDevice(numOfEquipment);
		Method.setMode(getResources().getStringArray(R.array.settingMenu));
		getMessageFromHardware(); btc.setContext(this); 
		btc.setHandler(hardwareHandler);
		seekbarLedDimmer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(btc.getStatusConnect())
				{
					btc.send(sendLedDimmer);
					btc.send((char)0);
					btc.send((char)progress);
					btc.send(endOfLine);					
				}
				textDimmer.setText(""+progress);
			}
		});
		//Button b7 = (Button) findViewById(R.id.b7);
		//Button b8 = (Button) findViewById(R.id.b8);
		//b7.setEnabled(false);
		//b8.setEnabled(false);
		 setAllColor();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		//closeDialog();
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		super.onConfigurationChanged(newConfig);
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}
	
	void loadSeekBarRGB()
	{
		seekbarRed=(VerticalSeekBar)findViewById(R.id.seekbar_red);
		seekbarGreen=(VerticalSeekBar)findViewById(R.id.seekbar_green);
		seekbarBlue=(VerticalSeekBar)findViewById(R.id.seekbar_blue);
        textRed=(TextView)findViewById(R.id.textview_red);
        textGreen=(TextView)findViewById(R.id.textview_green);
        textBlue=(TextView)findViewById(R.id.textview_blue);
        
        seekbarRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub			
			}	
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub		
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(btc.getStatusConnect() && flagReceive)
				{
					btc.send(sendLedRGB);
					btc.send((char) 0);
					btc.send(idLedRed);
					btc.send((char)progress);
					btc.send(endOfLine);
				}
				textRed.setText(progress+"");
			}
		});  
        seekbarGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {		
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}		
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}	
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(btc.getStatusConnect() && flagReceive)
				{
					btc.send(sendLedRGB);
					btc.send((char) 0);
					btc.send(idLedGreen);
					btc.send((char)progress);
					btc.send(endOfLine);
				}
				textGreen.setText(progress+"");		
			}
		});
        seekbarBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {		
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}		
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}	
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(btc.getStatusConnect() && flagReceive)
				{
					btc.send(sendLedRGB);
					btc.send((char) 0);
					btc.send(idLedBlue);
					btc.send((char)progress);
					btc.send(endOfLine);
				}
				textBlue.setText(progress+"");		
			}
		});
	}
	
	 public void beginDialog()
		{
		 	//pDialog = new ProgressDialog(context);
		 	pDialog = new ProgressDialog(context);
			pDialog.setTitle("Please Wait");
	        pDialog.setIndeterminate(true);
	        pDialog.setCancelable(false);	
	        pDialog.setMessage(dialogWaiting);
	        pDialog.show();
		}
	 
	 private void closeDialog()
		{
			try
			{
				/*if (pDialog != null && pDialog.isShowing()) 
				{
					pDialog.cancel();
					pDialog.dismiss();
					pDialog=null;
				}*/
				pDialog.dismiss();
				//pDialog.cancel();
				//pDialog=null;
			}
			catch(Exception ex){}
		}
	 
	@SuppressLint("HandlerLeak")
	public void getMessageFromHardware()
	{
		hardwareHandler = new Handler() 
		{
			public void handleMessage(android.os.Message msg) 
			{
				try
				{
					String b = (String) msg.obj;
					if(b.equalsIgnoreCase("sukses")) 
					{
						runOnUiThread(new Runnable() {		
							@Override
							public void run() {
								closeDialog();
								setTextKoneksi(true);
								flagReceive = true; //counterAlarm=0;
								askAllStatusDevice();
								//flagBegin = true; statusTimer=false;
								//sendStatusInfo(receiveAllDevice, sendBeginDevice);
								//closeDialog();
								//Method.toastShort("Sudah terkoneksi dengan sistem");
								//Method.toastShort("Sudah Terkoneksi dengan Hardware");
							}
						});			
					}
					else if(b.equalsIgnoreCase("gagal")) Method.toastLong(getApplicationContext(),"Gagal terkoneksi dengan Device. Periksa kembali jalur koneksi");
					else if(b.equals("")){}
					else
					{
						try
						{
							result = Method.parseData(b);
							final int id = Integer.parseInt(result[0]);
							if(id == sendDevice)
							{
								final int num = Integer.parseInt(result[1]);
								final int status = Integer.parseInt(result[2]);
								runOnUiThread(new Runnable() {				
									@Override
									public void run() 
									{
										setInterface(num+1,status);
									}
								});	
							}
							else if(id==receiveAllDevice) 
							{
								final String[] statusDevice = result;
								runOnUiThread(new Runnable() {				
									@Override
									public void run() 
									{
										setInterfaceAll(statusDevice);
										//if(flagBegin) sendStatusInfo(receiveAllDevice, sendBeginAlarm1);
									}
								});
							}
							/*else if(id==sendTimer)
							{
								final int status = Integer.parseInt(result[1]);
								final int hourT = Integer.parseInt(result[2]);
								final int minuteT = Integer.parseInt(result[3]);
								runOnUiThread(new Runnable() {				
									@Override
									public void run() 
									{
										if(flagBegin)
										{
											//closeDialog();
											if(status==1) statusTimer=true;
											flagBegin=false;
										}
										else if(dialogStatusOpen)
										{
											if(status==1) 
											{
												textTimerInfo += "- Timer ON selama ";
												if(hourT!=0) textTimerInfo+=hourT+" jam ";
												if(minuteT!=0) textTimerInfo+=minuteT+" menit";
												statusTimer=true;
												//textTimerInfo += "- Timer ON selama "+result[2]+":"+result[3];
											}
											else {textTimerInfo += "- Timer OFF";statusTimer=false;}
											sendStatusInfo(receiveStatusInfo, sendInfoRTC);
										}
										else
										{
											if(status==1)
											{
												String text = "Timer telah diaktifkan selama ";
												if(hourT!=0) text+=hourT+" jam ";
												if(minuteT!=0) text+=minuteT+" menit ";
												Method.toastShort(getApplicationContext(),text);
												statusTimer=true;
											}
											else {Method.toastShort(getApplicationContext(),"Timer berhasil dimatikan!");statusTimer=false;}
										}
									}
								});
							}*/
							else if(id==idMode)
							{
								final int mode = Integer.parseInt(result[1]);
								runOnUiThread(new Runnable() {				
									@Override
									public void run() 
									{
										setInterfaceAll2(mode);
									}
								});
							}
							else if(id==receiveTimeRTC)
							{
								final String time = result[1];
								final String date = result[2];
								runOnUiThread(new Runnable() {							
									@Override
									public void run() 
									{
										if(dialogStatusOpen)
										{
											//closeDialog();
											//String textRTCInfo = "- RTC di Device : "+time+" "+date;
											String textRTCInfo = "Real-Time on Device\n";
											textRTCInfo+="Clock --> "+time+"\n";
											textRTCInfo+="Date  --> "+date;
											//textAlarmInfo = textAlarmInfo.trim();
											dialogStatusOpen=false;
											Method.toastLong(getApplicationContext(), textRTCInfo);
											//loadDialogStatusInfo(textAlarmInfo, textTimerInfo, textRTCInfo);
											//loadDialogStatusInfo(textAlarmInfo, textTimerInfo, textRTCInfo);
										}
										else Method.toastLong(getApplicationContext(),"Waktu RTC sukses diatur pada pukul "+time+" tanggal "+date);
									}
								});
							}
							/*else if(id==receiveStatusInfo)
							{
								runOnUiThread(new Runnable() {								
									@Override
									public void run() {
										loadDialogStatusInfo(result);
									}
								});
							}*/
							/*else if(id==sendAlarm)
							{
								final int numAlarm = Integer.parseInt(result[1]);
								final int status = Integer.parseInt(result[2]);
								final String time = result[3];
								runOnUiThread(new Runnable() {				
									@Override
									public void run() 
									{
										if(result[1].equals("1")) 
										{
											String text = "Alarm telah diaktifkan pada pukul "+result[2];
											Method.toastShort(text);
										}		
										if(dialogStatusOpen)
										{
											//int numAlarm = Integer.parseInt(result[1]);
											if(status==1)
											{
												textAlarmInfo += "- Alarm "+(numAlarm+1)+" ON pada pukul "+time+"\n";  
											}
											else textAlarmInfo += "- Alarm "+(numAlarm+1)+" OFF"+"\n";
											counterAlarm++;
											//sendStatusInfo(receiveStatusInfo, sendAlarm2);
											if(counterAlarm<numOfAlarm)
											{
												sendStatusInfo(receiveStatusInfo, sendInfoAlarm2);
											}
											else {counterAlarm=0;sendStatusInfo(receiveStatusInfo, sendInfoTimer);}
											
										}
										else if(flagBegin) 
										{
											//setInterfaceAlarm(numAlarm, status);
											if(counterAlarm==0) 
											{
												counterAlarm++;
												sendStatusInfo(receiveAllDevice, sendBeginAlarm2);
											}
											else //if(counterAlarm==1)
											{
												counterAlarm=0;
												sendStatusInfo(receiveAllDevice, sendBeginTimer);
											}
											//counterAlarm++;
											//if(counterAlarm==numOfAlarm){flagBegin=false;counterAlarm=0;}
										}
										else
										{
											//setInterfaceAlarm(numAlarm, status);
											if(status==1)
											{
												String text = "Alarm "+(numAlarm+1)+" telah diaktifkan pada pukul "+time;
												Method.toastShort(getApplicationContext(),text);
											}
										}
									}
								});	
							}*/
							/*else if(id==sendDummy)
								runOnUiThread(new Runnable() {									
									@Override
									public void run() 
									{
										
									}
								});*/
							//else if(id==receiveDateRTC) Method.toastShort("Tanggal RTC sukses diatur pada tanggal "+result[1]);	
						}
						catch(Exception e){}//{Method.toastShort("Received data are error or corupt!");}
					}
				}
				catch(Exception e){}
			};
		};
	}
	
	public void loadDialogStatusInfo(String textAlarm, String textTimer, String textRTC)
	{	
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Status Device");	
		
		TextView alarm = (TextView) dialog.findViewById(R.id.alarm);
		TextView timer = (TextView) dialog.findViewById(R.id.timer);
		TextView rtc = (TextView) dialog.findViewById(R.id.rtc);
		
		alarm.setText(textAlarm);
		timer.setText(textTimer);
		rtc.setText(textRTC);
		dialog.show();
	}
	
	private void sendStatusInfo(char header, char data)
	{
		btc.send(header);
		btc.send(data);
		btc.send(endOfLine);
	}
	private void askAllStatusDevice()
	{
		btc.send(receiveAllDevice);
		btc.send(endOfLine);
	}
	
	private void setAllColor()
	{
		Drawable d;
		PorterDuffColorFilter filter;
		for(int i=1;i<=numOfEquipment;i++)
		{
			String buttonName = "b"+i;
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			Button button = (Button) findViewById(resID);
			//Drawable d = findViewById(resID).getBackground();
			d = button.getBackground();
			button.setText("OFF");
			//button.setBackgroundColor(getResources().getColor(R.color.green));
			filter = new PorterDuffColorFilter(Color.parseColor("#b5af9e"), PorterDuff.Mode.SRC_ATOP);
				//if(id==9) Method.toastShort("Alarm telah diaktifkan");
			d.setColorFilter(filter);
		}
		filter = new PorterDuffColorFilter(Color.parseColor("#4169e1"), PorterDuff.Mode.SRC_ATOP);
		/*d = daylight.getBackground();
		d.setColorFilter(filter);
		d = night.getBackground();
		d.setColorFilter(filter);
		d = sleep.getBackground();
		d.setColorFilter(filter);
		d = leave.getBackground();
		d.setColorFilter(filter);*/
	}
	
	private void setInterface(int id, int status)
	{
		String buttonName = "b"+id;
		int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
		Button button = (Button) findViewById(resID);
		//Drawable d = findViewById(resID).getBackground();
		Drawable d = button.getBackground();
		PorterDuffColorFilter filter;
		if(status==0)
		{
			button.setText("OFF");
			//button.setBackgroundColor(getResources().getColor(R.color.red));		
	        filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		}
		else
		{
			button.setText("ON");
			//button.setBackgroundColor(getResources().getColor(R.color.green));
			filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
			//if(id==9) Method.toastShort("Alarm telah diaktifkan");
		}
		d.setColorFilter(filter);
	}
	
	//Set interface from Hardware --> printStatusAndroid
	public void setInterfaceAll(String[] statusDevice)
	{
		for(int i=1;i<=numOfEquipment;i++)
		{		
				String buttonName = "b"+i;
				int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
				Button button = (Button) findViewById(resID);
				//Drawable d = findViewById(resID).getBackground();
				Drawable d = button.getBackground();
				PorterDuffColorFilter filter;
				if(statusDevice[1].charAt(i-1)=='0')
				{
					button.setText("OFF");
					//button.setBackgroundColor(getResources().getColor(R.color.red));
					filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
				}
				else
				{
					button.setText("ON");
					//button.setBackgroundColor(getResources().getColor(R.color.green));
					filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
				}
				d.setColorFilter(filter);
		}
	}
	
	private void setInterfaceAlarm(int number, int status)
	{	
		String buttonName="";		
		if(number==0)  buttonName = "b"+numberButtonAlarm1;
		else buttonName = "b"+numberButtonAlarm2;
		int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
		Button button = (Button) findViewById(resID);
		//Drawable d = findViewById(resID).getBackground();
		Drawable d = button.getBackground();
		PorterDuffColorFilter filter;
		if(status==0)
		{
			button.setText("OFF");
			//button.setBackgroundColor(getResources().getColor(R.color.red));
			filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		}
		else
		{
			button.setText("ON");
			//button.setBackgroundColor(getResources().getColor(R.color.green));
			filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
		}
		d.setColorFilter(filter);
	}
	
	private void setInterfaceAll2(int mode)
	{
		for(int i=0;i<numOfEquipment;i++)
		{
			String buttonName = "b"+(i+1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			Button button = (Button) findViewById(resID);
			//Drawable d = findViewById(resID).getBackground();
			Drawable d = button.getBackground();
			PorterDuffColorFilter filter;
			if(mode==modeDaylight)
			{
				if(setDaylightValue[i])
				{
					button.setText("ON");
					//button.setBackgroundColor(getResources().getColor(R.color.green));
					filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
				}
				else
				{		
					button.setText("OFF");
					//button.setBackgroundColor(getResources().getColor(R.color.red));
					filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
				}
			}
			else if(mode==modeNight)
			{
				if(setNightValue[i])
				{
					button.setText("ON");
					//button.setBackgroundColor(getResources().getColor(R.color.green));
					filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
				}
				else
				{		
					button.setText("OFF");
					//button.setBackgroundColor(getResources().getColor(R.color.red));
					filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
				}
			}
			else if(mode==modeSleep)
			{
				if(setSleepValue[i])
				{
					button.setText("ON");
					//button.setBackgroundColor(getResources().getColor(R.color.green));
					filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
				}
				else
				{		
					button.setText("OFF");
					//button.setBackgroundColor(getResources().getColor(R.color.red));
					filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
				}
			}
			else
			{
				if(setLeaveValue[i])
				{
					button.setText("ON");
					//button.setBackgroundColor(getResources().getColor(R.color.green));
					filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
				}
				else
				{		
					button.setText("OFF");
					//button.setBackgroundColor(getResources().getColor(R.color.red));
					filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
				}
			}
			d.setColorFilter(filter);
		}
	}
	
	public void onButtonClicked(View v)
	{
		if(btc.getStatusConnect() && flagReceive)
		{
			String buttonName = getResources().getResourceEntryName(v.getId());
			String textButton = ((Button) v).getText().toString();
			char FirstChar = buttonName.charAt(0);
			//char char1 = buttonName.charAt(1);
			/*if(buttonLength>2)
			{
				//char char2 = buttonName.charAt(2);
				//String a =
				String textTrim = buttonName.substring(2);
			}
			else
			{
				
			}*/
			//char FirstChar = buttonName.charAt(0);
			//char LastChar = buttonName.charAt(1);
			if(FirstChar == 'b') 
			{
				String textTrim = buttonName.substring(1,(buttonName.length()));
				char LastChar = (char) (Integer.parseInt(textTrim)-1);
				sendCommand(LastChar, textButton);
			}
			else sendCommand(buttonName);
			btc.send(endOfLine);
		}
		else displayFailed();
	}
	
	private void displayFailed()
	{
		Method.toastShort(getApplicationContext(),textFailedConnect);
	}
	
	private void sendCommand(char number, String status)
	{
		char statusNumber;
		//number = (char) ((int)number - 48);
		if(status.equalsIgnoreCase("OFF")) statusNumber=1;
		else statusNumber=0;
		int numberAlarm = number+1;
		if(numberAlarm==(char)numberButtonAlarm1 || numberAlarm==(char)numberButtonAlarm2)
		{
			btc.send(sendAlarm);
			if(numberAlarm==(char)numberButtonAlarm1) btc.send(sendAlarm1);
			else btc.send(sendAlarm2);
		}
		else
		{
			//number = (char) ((int)number - 1);
			btc.send(sendDevice);
			btc.send(number);
		}
		btc.send(statusNumber);
	}
	private void sendCommand(String buttonName)
	{
		if(buttonName.equalsIgnoreCase("daylightButton"))
		{
			btc.send(idMode);
			btc.send(modeDaylight);
			for(int i=0;i<numOfEquipment;i++) 
				if(setDaylightValue[i]) btc.send((char)1);
				else btc.send((char)0);
		}
		else if(buttonName.equalsIgnoreCase("nightButton"))
		{
			btc.send(idMode);
			btc.send(modeNight);
			for(int i=0;i<numOfEquipment;i++) //btc.send(nightValue[i]);
				if(setNightValue[i]) btc.send((char)1);
				else btc.send((char)0);
		}
		else if(buttonName.equalsIgnoreCase("sleepButton"))
		{
			btc.send(idMode);
			btc.send(modeSleep);
			for(int i=0;i<numOfEquipment;i++) //btc.send(sleepValue[i]);
				if(setSleepValue[i]) btc.send((char)1);
				else btc.send((char)0);
		}
		else
		{
			btc.send(idMode);
			btc.send(modeLeave);
			for(int i=0;i<numOfEquipment;i++) //btc.send(leaveValue[i]);
				if(setLeaveValue[i]) btc.send((char)1);
				else btc.send((char)0);
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		if(tabletSize) 
		{
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defScreenTimeOut);
		}
		flagOff=false;
		Method.setActivityName(Method.main);
		Method.setContext(this);
		//loadSettingPref();
		flagOpenAlarm=false;
		connectToHardware();
	}
	
	private void loadSettingPref()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		for(int i=0;i<numOfEquipment;i++)
		{
			setDaylightValue[i] = sharedPref.getBoolean("prefOD"+i,false);
			setNightValue[i] = sharedPref.getBoolean("prefON"+i,false);
			setSleepValue[i] = sharedPref.getBoolean("prefOS"+i,false);
			setLeaveValue[i] = sharedPref.getBoolean("prefOL"+i,false);
			setTimerValue[i] = sharedPref.getBoolean("prefOT"+i,false);
			name[i] = sharedPref.getString("prefN"+i,"Channel "+(i+1));
			Method.setDaylightValue(setDaylightValue);
			Method.setNightValue(setNightValue);
			Method.setSleepValue(setSleepValue);
			Method.setLeaveValue(setLeaveValue);
			Method.setTimerValue(setTimerValue);
			Method.setDeviceName(name);
			//if(setDaylightValue[i]) daylightValue[i] = 1; else daylightValue[i] = 0;
			//if(setNightValue[i]) nightValue[i] = 1; else nightValue[i] = 0;
			//if(setSleepValue[i]) sleepValue[i] = 1; else sleepValue[i] = 0;
			//if(setLeaveValue[i]) leaveValue[i] = 1; else leaveValue[i] = 0;
			
			//if(setTimerValue[i]) timerValue[i] = 1; else timerValue[i] = 0;
			/*for(int j=0;j<numOfAlarm;j++)
			{
				setAlarmValue[j][i] = sharedPref.getBoolean("prefOA"+i+"."+i,false);
				if(setAlarmValue[j][i]) alarmValue[j][i] = 1; else alarmValue[j][i] = 0;
			}*/
			String buttonName = "t"+(i+1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i]);
		}
		/*for(int i=0;i<numOfAlarm;i++)
		{
			name[i+numOfEquipment] = sharedPref.getString("prefA"+(i+1),"Alarm "+(i+1));
			String buttonName = "t"+(i+numberButtonAlarm1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			TextView textview =(TextView) findViewById(resID);
			textview.setText(""+name[i+numOfEquipment]);
		}*/
	}
	
	private void connectToHardware()
	{		
		Method.setDefaultMessageDialog();
		/*if(tabletSize) 
		{
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
		if(!btc.getBTStatus()) btc.turnOnBluetooth();
		if(flagSetting) flagSetting=false;
		if(!btc.getStatusConnect() && !flagReceive) 
		{
			beginDialog();
			new Thread(new Runnable() 
			{
				@Override
				public void run() 
				{
					while(!btc.getBTStatus());
					try 
					{
						final boolean status = btc.createConnection();	
						if(status) 
						{
							checkHandshake();
						}
						else closeDialog();				
						new Thread(new Runnable() {								
							@Override
							public void run() 
							{
								while(btc.getStatusConnect())
								{
									try {
										Thread.sleep(delayCheckConnection);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								runOnUiThread(new Runnable() 
								{					
									@Override
									public void run() 
									{	
										flagReceive=false;
										if(!flagOnFinish) {backtoDisconnect();}//Method.toastShort("Koneksi android dengan Device terputus");
										if(status && flagOnDisconnect)Method.toastShort(getApplicationContext(),"Koneksi android dengan Device terputus");
										else if(!status && !flagOnDisconnect) Method.toastShort(getApplicationContext(),"Gagal Terkoneksi dengan Device");
									}
								});		
							}
						}).start();						
					} catch (Exception e) {}
				}
			}).start();
		}
		else if(btc.getStatusConnect() && !flagReceive)
		{
			beginDialog();
			checkHandshake();
		}
	}
	
	private void backtoDisconnect()
	{
		setTextKoneksi(false);
		for(int i=0;i<numOfEquipment;i++)
		{
			String buttonName = "b"+(i+1);
			int resID = getResources().getIdentifier(buttonName , "id", getPackageName());
			Button button = (Button) findViewById(resID);
			Drawable d = button.getBackground();
			button.invalidateDrawable(d);
			/*Drawable d = findViewById(resID).getBackground();
            findViewById(resID).invalidateDrawable(d);*/
            d.clearColorFilter();
		}
		//b1.setBackgroundColor(Color.GRAY);
		//b2.setBackgroundColor(Color.GRAY);
		//b3.setBackgroundColor(Color.GRAY);
		//b4.setBackgroundColor(Color.GRAY);
		//b5.setBackgroundColor(Color.GRAY);
		//b6.setBackgroundColor(Color.GRAY);
		//b7.setBackgroundColor(Color.GRAY);
		//b8.setBackgroundColor(Color.GRAY);
		//b9.setBackgroundColor(Color.GRAY);
	}
	
	private void setTextKoneksi(boolean status)
	{
		if(status==false) 
		{
			statusKoneksi.setText("Status koneksi : OFF");
			statusKoneksi.setTextColor(getResources().getColor(R.color.red));
		}
		else 
		{
			statusKoneksi.setText("Status koneksi : ON");
			statusKoneksi.setTextColor(getResources().getColor(R.color.green));
		}
	}
	
	@Override
	public void onBackPressed() 
	{		
		super.onBackPressed();
    	flagOnFinish = true;
		disconnectFromHardware();
    	if(btc.getBTStatus() && !tabletSize) btc.turnOffBluetooth();
		finish(); 
		/*AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
    	builder
	    	.setTitle("Exit Application")
	    	.setMessage("Are you sure?")
	    	.setIcon(android.R.drawable.ic_dialog_alert)
	    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
	    	{
	    		public void onClick(DialogInterface dialog, int which) 
	    	    {			      	
	    			flagOnDisconnect=true;
	    	    	disconnectFromHardware();
	    	    	flagOnDisconnect=false;
	    	    	btc.turnOffBluetooth();
	    			finish();    	    	
	    	    }
	    	})
    	.setNegativeButton("No", null)
    	.show();*/
    	/*
    	flagOnDisconnect=true;
	    	    	disconnectFromHardware();
	    	    	flagOnDisconnect=false;
	    			finish(); 
    	  */
	}
	
	private void disconnectFromHardware()
	{
		if(btc.getBTStatus())
		{
			if(btc.getStatusConnect()) 
			{
				btc.send(sendDisconnect);
				btc.send(endOfLine);
				btc.closeSocket();
				flagReceive=false;
			}
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		if(tabletSize && !flagSetting && !flagOpenAlarm)
		{
			flagOnDisconnect=true;
	    	disconnectFromHardware();
	    	flagOnDisconnect=false;
		}
		flagOff=true;
	}
	
	/*@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if(btc.getStatusConnect()) getMenuInflater().inflate(R.menu.main2, menu);
		else getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	private void initUI() 
	{
		statusKoneksi = (TextView) findViewById(R.id.statusBTText);
		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		/*b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
		b6 = (Button) findViewById(R.id.b6);*/
		seekbarLedDimmer = (SeekBar) findViewById(R.id.seekbar_led_dimmer);
		textDimmer = (TextView) findViewById(R.id.textview_led_dimmer);
		/*b7 = (Button) findViewById(R.id.b7);
		b8 = (Button) findViewById(R.id.b8);
		b9 = (Button) findViewById(R.id.b9);
		b10 = (Button) findViewById(R.id.b10);
		b11 = (Button) findViewById(R.id.b11);
		b12 = (Button) findViewById(R.id.b12);
		b13 = (Button) findViewById(R.id.b13);
		b14 = (Button) findViewById(R.id.b14);
		b15 = (Button) findViewById(R.id.b15);
		b16 = (Button) findViewById(R.id.b16);
		//b17 = (Button) findViewById(R.id.b17);
		//b18 = (Button) findViewById(R.id.b18);
		daylight = (Button) findViewById(R.id.daylightButton);
		night = (Button) findViewById(R.id.nightButton);
		sleep = (Button) findViewById(R.id.sleepButton);
		leave = (Button) findViewById(R.id.leaveButton);*/
	}
	
	/*
	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n Username: "
				+ sharedPrefs.getString("prefUsername", "NULL"));

		builder.append("\n Send report:"
				+ sharedPrefs.getBoolean("prefSendReport", false));

		builder.append("\n Sync Frequency: "
				+ sharedPrefs.getString("prefSyncFrequency", "NULL"));

		TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);

		settingsTextView.setText(builder.toString());
	}
	*/
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
		//if(btc.getStatusConnect()) getMenuInflater().inflate(R.menu.main2, menu);
       if(btc.getStatusConnect() && flagReceive)
        {
    	   getMenuInflater().inflate(R.menu.menu_disconnect, menu);
    	   //getMenuInflater().inflate(R.menu.menu_non_timer, menu);
    	   //if(statusTimer) getMenuInflater().inflate(R.menu.main2, menu);
        	//else getMenuInflater().inflate(R.menu.main2nontimer, menu);
        }
		else getMenuInflater().inflate(R.menu.menu_connect, menu);
			//getMenuInflater().inflate(R.menu.main, menu);
        //getMenuInflater().inflate(R.menu.main2, menu);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.settingMode:
	        	flagSetting=true;
	        	Intent setting = new Intent(getApplicationContext(), ListSetting.class);     
        		startActivity(setting);
	            return true;
	        case R.id.reconnect:	
	        	connectToHardware();
	            return true;
	        case R.id.disconnect: 
	        	flagOnDisconnect=true;
	        	disconnectFromHardware();
				Method.toastShort("Koneksi android sudah sukses diputuskan dengan hardware");
				flagOnDisconnect=false;
	            return true;
	        case R.id.status: 
	        	//textAlarmInfo="";
	        	//textTimerInfo="";
	        	//loadDialogStatusInfo();
	        	dialogStatusOpen=true;
	        	//pDialog.setMessage(textMessageStatusInfo);
	        	//beginDialog();
	        	/*runOnUiThread(new Runnable() {					
					@Override
					public void run() {beginDialog();}
				});*/
	        	sendStatusInfo(receiveStatusInfo, sendInfoAlarm1);
	            return true;
	        case R.id.settingTimeRTC:  
	        	settingTimeRTC(receiveTimeRTC);
	            return true;
	        case R.id.timeroff:  
	        	if(btc.getStatusConnect() && flagReceive) settingTimerOff();
	        	else displayFailed();
	            return true;
	        case R.id.settingAlarm:  
	        	flagOpenAlarm=true;
	        	Method.setActivityName(Method.alarm);
	        	Intent settingAlarm = new Intent(getApplicationContext(), ListAlarm.class);     
        		startActivity(settingAlarm);
	            return true;
	       /* case R.id.settingAlarm:  
	        	mode = getResources().getString(R.string.alarm1);
	        	showDialog(ALARM_DIALOG_ID);
	            return true;*/
	       /* case R.id.settingAlarm1:  
	        	mode = getResources().getString(R.string.alarm1);
	        	showDialog(ALARM_DIALOG_ID);
	            return true;
	        case R.id.settingAlarm2:  
	        	mode = getResources().getString(R.string.alarm2);
	        	showDialog(ALARM_DIALOG_ID);
	            return true;*/
	        case R.id.settingTimer:  
	        	mode = getResources().getString(R.string.timer);
	        	showDialog(TIMER_DIALOG_ID);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void settingTimerOff()
	{
		btc.send(sendTimer);
		btc.send((char) 0);
		btc.send(endOfLine);
	}
	
	private void settingTimeRTC(char ids)
	{
		
		if(btc.getStatusConnect() && flagReceive)
		{
			final Calendar c = Calendar.getInstance();
			day = c.get(Calendar.DAY_OF_WEEK);
			date = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH)+1;
			year = c.get(Calendar.YEAR) - 2000;
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			second = c.get(Calendar.SECOND);
			btc.send(ids);
			btc.send((char)hour);
			btc.send((char)minute);
			btc.send((char)second);
			btc.send((char)day);
			btc.send((char)date);
			btc.send((char)month);
			btc.send((char)year);
			btc.send(endOfLine);
		}
		else displayFailed();
	}
	private void settingDateRTC(char ids)
	{
		if(btc.getStatusConnect() && flagReceive)
		{
			final Calendar c = Calendar.getInstance();
			day = c.get(Calendar.WEEK_OF_MONTH);
			date = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH)+1;
			year = c.get(Calendar.YEAR) - 2000;
			btc.send(ids);
			btc.send((char)day);
			btc.send((char)date);
			btc.send((char)month);
			btc.send((char)year);
			btc.send(endOfLine);
		}
		else displayFailed();
	}
	
	/*@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) 
		{
			case ALARM_DIALOG_ID:
				getTime();
				return new TimePickerDialog(this,timePickerListener, hour, minute,formatTime);
			case TIMER_DIALOG_ID:
				getTime();
				return new TimePickerDialog(this,timePickerListener, hour, minute,formatTime);
		}
		return null;
	}*/
 
	private void getTime()
	{
		if(mode.equalsIgnoreCase(getResources().getString(R.string.alarm1)))
		{
			final Calendar c = Calendar.getInstance();
			int hourNow = c.get(Calendar.HOUR_OF_DAY);
			int minuteNow = c.get(Calendar.MINUTE);
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			hour = sharedPref.getInt(getString(R.string.hour1), hourNow);
			minute = sharedPref.getInt(getString(R.string.minute1), minuteNow);
			formatTime = false;
		}
		else if(mode.equalsIgnoreCase(getResources().getString(R.string.alarm2)))
		{
			final Calendar c = Calendar.getInstance();
			int hourNow = c.get(Calendar.HOUR_OF_DAY);
			int minuteNow = c.get(Calendar.MINUTE);
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			hour = sharedPref.getInt(getString(R.string.hour2), hourNow);
			minute = sharedPref.getInt(getString(R.string.minute2), minuteNow);
			formatTime = false;
		}
		else 
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			hour = sharedPref.getInt(getString(R.string.hTimer), 0);
			minute = sharedPref.getInt(getString(R.string.mTimer), 0);
			formatTime = true;
		}
	}
	
	private void writeTime()
	{
		if(mode.equalsIgnoreCase(getResources().getString(R.string.alarm1)))
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);	
			Editor editor = sharedPref.edit();
			editor.putInt(getString(R.string.hour1), hour);
			editor.putInt(getString(R.string.minute1), minute);
			editor.commit();
		}
		else if(mode.equalsIgnoreCase(getResources().getString(R.string.alarm2)))
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);	
			Editor editor = sharedPref.edit();
			editor.putInt(getString(R.string.hour2), hour);
			editor.putInt(getString(R.string.minute2), minute);
			editor.commit();
		}
		else
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);	
			Editor editor = sharedPref.edit();
			editor.putInt(getString(R.string.hTimer), hour);
			editor.putInt(getString(R.string.mTimer), minute);
			editor.commit();
		}
	}
	
	/*private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) 
		{
			hour = selectedHour;
			minute = selectedMinute;
			if(btc.getStatusConnect() && flagReceive)
			{				
				if(mode.equalsIgnoreCase(getResources().getString(R.string.alarm1)))
				{
					btc.send(setAlarmClock);
					btc.send(sendAlarm1);
					btc.send((char)1);
					btc.send((char)hour);
					btc.send((char)minute);
					for(int i=0;i<numOfEquipment;i++) btc.send(alarmValue[0][i]);
				}
				else if(mode.equalsIgnoreCase(getResources().getString(R.string.alarm2)))
				{
					btc.send(setAlarmClock);
					btc.send(sendAlarm2);
					btc.send((char)1);
					btc.send((char)hour);
					btc.send((char)minute);
					for(int i=0;i<numOfEquipment;i++) btc.send(alarmValue[1][i]);
				}
				else
				{
					btc.send(setTimerClock);
					btc.send((char)hour);
					btc.send((char)minute);
					for(int i=0;i<numOfEquipment;i++) //btc.send(timerValue[i]);
						if(setTimerValue[i]) btc.send((char)1);
						else btc.send((char)0);
				}
				btc.send(endOfLine);
				writeTime();
			}
			else displayFailed();
		}

		@Override
		public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
	};*/
	
	private void checkHandshake()
	{
		flagOnDisconnect=false;
		sendHandshakeCode();
		new Thread(new Runnable() 
		{									
			@Override
			public void run() 
			{
				int timeNow = 0;
				while(timeNow<=(timeOut*1000) && !flagReceive)
				{
					try{Thread.sleep(1);} 
					catch (InterruptedException e) {}
					timeNow++;										
				}
				if(timeNow>=timeOut && !flagReceive) 
				{
					closeDialog();
					Message msg = new Message();
					msg.obj = "gagal";
					hardwareHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	public void sendExitCode() 
    {
       btc.send(exitCode);
    }
	
	public void sendHandshakeCode() 
    {
       btc.send(sendConnect);
       btc.send(endOfLine);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		 if (event.values[0] == 0) 
		 {
			 //Method.toastShort(this, "dekat gan!!!");
			 if(!flagOff)
			 {
				 
				 /*WindowManager.LayoutParams params = getWindow().getAttributes();
				 params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
				 params.screenBrightness=0;
				 getWindow().setAttributes(params);*/
				 /*defScreenTimeOut = Settings.System.getInt(getContentResolver(), 
						 			Settings.System.SCREEN_OFF_TIMEOUT,0);*/
				 Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,1);
				 flagOff=true;
			 }
			 else
			 {
				 /*WindowManager.LayoutParams params = getWindow().getAttributes();
				 params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
				 params.screenBrightness=1;
				 getWindow().setAttributes(params);*/
				 unlockScreen();
			 }			 
		 }
	}
	 private void unlockScreen()
	 {
		/* Window window = this.getWindow();
		 window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		 window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		 window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);	 */
		/* KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		 final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		 w = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				             | PowerManager.ACQUIRE_CAUSES_WAKEUP
				             | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");*/
		 kl.disableKeyguard();
		 w.acquire();
		 w.release();
		 flagOff=false;
		 Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defScreenTimeOut);
	 }
	
	/*@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		closeDialog();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeDialog();
	}*/
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(btc.getStatusConnect()) getMenuInflater().inflate(R.menu.dashboard_2, menu);
		else getMenuInflater().inflate(R.menu.dashboard, menu);
		invalidateOptionsMenu();
		return true;
	}
	}
	*/
}
