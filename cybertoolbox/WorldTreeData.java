/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	WorldTreeData.java
*
******************************************************************/

public class WorldTreeData extends Object
{
	private String	mString;
	private Object	mObject;

	public WorldTreeData(String string, Object obj) {
		setText(string);
		setObject(obj);
	}

	public void setObject(Object obj) {
		mObject = obj;
	}

	public Object getObject() {
		return mObject;
	}
	
	public void setText(String string) {
		mString = string;
	}

	public String getText() {
		return mString;
	}

	public String toString() {
		return mString;
	}
}
