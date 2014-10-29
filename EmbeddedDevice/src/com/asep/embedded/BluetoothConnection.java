package com.asep.embedded;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BluetoothConnection
{
	private byte[] buffer; 
	private int bytes; 
	private String data="";
	private static Boolean status_connect=false;
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private final int RECIEVE_MESSAGE = 1;
	private char handshakeCode='a', exitCode='b';
	private BluetoothSocket btSocket = null;
	private Connection mConnectedThread;
	//private String moduleAddress = "78:E4:00:99:78:45", 
	//private String moduleAddress="20:13:10:22:02:41",
	//private String moduleAddress="20:13:09:11:05:45"; //Punya di Kos Saya
	//private String moduleAddress="98:D3:31:40:02:80"; //"20:13:09:11:05:45", Adul Punya
	//private String moduleAddress="98:D3:31:30:02:7C"; // DiptyaSmartRoom
	private String moduleAddress="98:D3:31:30:02:75"; //Yang saya utak-atik di kos
	private Handler h=null;
	private Context context;
	private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
	
	public void turnOnBluetooth()
	{
		//if(!btAdapter.isEnabled()) btAdapter.enable();
		btAdapter.enable();
		//Toast.makeText(context, "Bluetooth sudah dinyalakan", Toast.LENGTH_SHORT).show();
	}
	
	public void turnOffBluetooth()
	{
		btAdapter.disable();
		//Toast.makeText(context, "Bluetooth sudah dimatikan", Toast.LENGTH_SHORT).show();
	}
	
	public void setBTAddress(String address)
	{
		moduleAddress = address;
	}
	
	public BluetoothAdapter getAdapter()
	{
		return btAdapter;
	}
	
	public boolean getStatusConnect()
    {
		return status_connect;
    }
	
	public boolean getBTStatus()
    {
    	return btAdapter.isEnabled();
    }
	
	public void setHandler(Handler handler)
	{
		h = handler;
	}
	
	public void setBTAdapter(BluetoothAdapter bt)
	{
		btAdapter = bt;
	}
	
	public void setContext(Context ctx)
	{
		context = ctx;
	}
	
	public void setHandshakeCode(char data)
	{
		handshakeCode = data;
	}
	
	public void setExitCode(char data)
	{
		exitCode = data;
	}
	
	public void send(char message) 
    {
       mConnectedThread.send(message);
    }
	
	public void sendExitCode() 
    {
       mConnectedThread.send(exitCode);
    }
	
	public void sendConfirmationCode() 
    {
       mConnectedThread.send(handshakeCode);
    }
	
	public void sendString(String message) 
    {
       mConnectedThread.sendString(message);
    }
	
	public boolean createConnection()
	{
		/*new Thread(new Runnable() {					
			@Override
			public void run() 
			{
				int i = 0;
				while(!status_connect && !flagFinishConnect)
				{
					try 
					{
						Thread.sleep(1000);
						i++;
						if(i>=(timeOut/1000))
						{
							try {
								if(!status_connect){btSocket.close();status_connect=false;}
								pDialog.dismiss();
								return;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}	
					} 
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		flagFinishConnect=false;*/
		if(getBTStatus() && !status_connect)
	    {
					try
					{
						BluetoothDevice device=null;
						try
						{
							device = btAdapter.getRemoteDevice(moduleAddress);
						}
						catch(Exception e)
						{
							Log.d("test", e.toString());
						}
						try 
						{
							btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
							//btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 	    
						btAdapter.cancelDiscovery();
						try 
						{
							btSocket.connect();
							mConnectedThread = new Connection();
							mConnectedThread.start();
							status_connect=true;
						} 
						catch (IOException e) 
						{
							try 
							{
								btSocket.close();
								status_connect=false;
							} 
							catch (Exception e2){}
						}
					} catch (Exception e) {}
	    }
		//flagFinishConnect=true;
		return status_connect;
	}
	
	public boolean closeSocket()
	{
		if(getBTStatus() && status_connect)
		{
			try     
		    {
				btSocket.close(); status_connect=false;
		    } 
		    catch (IOException e2){}
		}
		return status_connect;
	}
	
	/*private String messageAction(android.os.Message msg)
    {
    	String sbprint="";
    	switch (msg.what) 
    	{
        	case RECIEVE_MESSAGE:                                                  
                    byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf, 0, msg.arg1);                 
                    sb.append(strIncom);                                               
                    int endOfLineIndex = sb.indexOf(endString);                            
                    if (endOfLineIndex > 0) 
                    {                                            
                        sbprint = sb.substring(0, endOfLineIndex);
                        sb.delete(0, sb.length());
                    }
                    break;
         }
    	return sbprint;
    }*/
	
	public class Connection extends Thread 
	{
		private InputStream mmInStream;
	    private OutputStream mmOutStream;
	    private BluetoothSocket socket = btSocket;
	    
	    public Connection() 
	    {
	    	try 
		    {
		       	mmInStream = socket.getInputStream();
		       	mmOutStream = socket.getOutputStream();
		        //status_connect=true;
	        } 
	        catch (IOException e) 
	        { 
	          	try 
	      		{
	       			mmInStream.close();
	       			mmOutStream.close();
	       			socket.close();	        			
	       			btSocket.close();        			
	       		} 
	           	catch (Exception e3) {e.printStackTrace();}
	          	status_connect=false;
	        };
	    }
	    
	    public void send(char message) 
	    {
	        try 
	        {
	            mmOutStream.write(message);
	        } 
	        catch (IOException e) 
	        {
	        	try 
	            {
					mmInStream.close();
					mmOutStream.close();
	            	btSocket.close();
	            	socket.close();
				} 
	            catch (IOException e1) 
	            {
					e1.printStackTrace();
				}
	        	status_connect=false;
	        }
	    }
	    
	    public void sendString(String message) 
	    {
	    	byte[] msgBuffer = message.getBytes();
	        try 
	        {
	            mmOutStream.write(msgBuffer);
	        } 
	        catch (IOException e) 
	        {
	        	try 
	            {
					mmInStream.close();
					mmOutStream.close();
	            	btSocket.close();
	            	socket.close();
				} 
	            catch (IOException e1) 
	            {
					e1.printStackTrace();
				}
	        	status_connect=false;
	        }
	    }
	    
	    public void run() 
	    {
	        buffer = new byte[256];
	        //Message msg = new Message();
	        while (true) 
	        {
	         	//if(!status_connect) return;
	           	try 
	            {
	                bytes = mmInStream.read(buffer);                
	                data = new String(buffer, 0, bytes);
	                data = Method.filterMessage(data);  
	                //h.obtainMessage(RECIEVE_MESSAGE,data).sendToTarget();
	                //msg.obj = data;
	                //msg.what = 0;
	                //MainActivity.hardwareHandler.sendMessage(msg);
	                if(Method.getActivityName().equalsIgnoreCase(Method.main))
	                	MainActivity.hardwareHandler.obtainMessage(RECIEVE_MESSAGE,data).sendToTarget();
	                else ListAlarm.alarmHandler.obtainMessage(RECIEVE_MESSAGE,data).sendToTarget();
	            } 
	            catch (IOException e) 
	            {
	              	try 
		      		{
	        			mmInStream.close();
		       			mmOutStream.close();
		        		btSocket.close();
		        		socket.close();
		        		status_connect=false; break; //return;	
		        	} 
	                catch (IOException e3) 
		        	{
		        		e.printStackTrace(); 
		        		status_connect=false; break;
		        	}
	            }
	        }
	    }
    }
}
