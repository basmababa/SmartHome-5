package com.asep.embedded;

public class DummyClass {
	
	private String name;
	private Boolean status;
	
	/*public DummyClass(String name, Boolean status)
	{
		this.setName(name);
		this.setStatus(status);
	}*/
	
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
}
