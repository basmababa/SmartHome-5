package com.asep.embedded;

import java.util.LinkedList;
import java.util.List;

public class Alarm 
{
	private int id;
	private String name;
	private Boolean status;
	private String hour,minute;
	private List<Boolean> deviceStatus = new LinkedList<Boolean>();
	
	/*public Alarm(int id, String name, Boolean status, String hour, String minute, List<Boolean> deviceStatus)
	{
		this.setId(id);
		this.setName(name);
		this.setStatus(status);
		this.setHour(hour);
		this.setMinute(minute);
		this.setDeviceStatus(deviceStatus);
	}*/
	
	public void setId(int ids)
	{
		this.id = ids;
	}
	public int getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Boolean getStatus()
	{
		return this.status;
	}
	
	public void setStatus(Boolean status)
	{
		this.status = status;
	}
	
	public String getHour()
	{
		return this.hour;
	}
	
	public void setHour(String hour)
	{
		this.hour = hour;
	}
	public String getMinute()
	{
		return this.minute;
	}
	
	public void setMinute(String minute)
	{
		this.minute = minute;
	}
	public List<Boolean> getDeviceStatus()
	{
		return this.deviceStatus;
	}
	public Boolean getDeviceStatus(int position)
	{
		return this.deviceStatus.get(position);
	}
	public void setDeviceStatus(List<Boolean> deviceStatus)
	{
		this.deviceStatus = deviceStatus;
	}
	public void setDeviceStatus(int position,Boolean status)
	{
		this.deviceStatus.set(position, status);
	}
	public void clearDeviceStatus()
	{
		this.deviceStatus = new LinkedList<Boolean>();
	}
	public void addDeviceStatus(Boolean status)
	{
		deviceStatus.add(status);
	}
}
