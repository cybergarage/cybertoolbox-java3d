/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-2000
*
*	File : Route.java
*
******************************************************************/

package cv97.parser.x3d;

public class RouteInfo {

	private String	mFromNodeName	= null;
	private String	mToNodeName		= null;
	private String	mFromFieldName	= null;
	private String	mToFieldName	= null;

	public RouteInfo() 
	{
		setFromNodeName(null);
		setToNodeName(null);
		setFromFieldName(null);
		setToFieldName(null);
	}

	public RouteInfo(String fromNodeName, String fromFieldName, String toNodeName, String toFieldName) 
	{
		setFromNodeName(fromNodeName);
		setToNodeName(toNodeName);
		setFromFieldName(fromFieldName);
		setToFieldName(toFieldName);
	}

	public void setFromNodeName(String name)
	{ 
		mFromNodeName = name; 
	}
	
	public void setToNodeName(String name)
	{
		mToNodeName = name; 
	}

	public String getFromNodeName() 
	{
		return mFromNodeName; 
	}
	
	public String getToNodeName()
	{
		return mToNodeName; 
	}
	
	public void setFromFieldName(String name)
	{
		mFromFieldName = name; 
	}
	
	public String getFromFieldName()
	{
		return mFromFieldName; 
	}

	public void setToFieldName(String name)
	{
		mToFieldName = name; 
	}
	
	public String getToFieldName()
	{
		return mToFieldName; 
	}

};
