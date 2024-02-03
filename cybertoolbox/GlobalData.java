/******************************************************************
*
*	CyberToolbox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File : GlobalValue.java
*
******************************************************************/

public class GlobalData extends LinkedListNode {
	
	private String mGroupName	= null;
	private String mName			= null;
	private String mValue		= null;
	
	public GlobalData(String groupName, String name) {
		setHeaderFlag(false);
		setGroupName(groupName);
		setName(name);
		setValue(null);
	}

	public void setGroupName(String groupName) {
		mGroupName = groupName;
	}
	
	public String getGroupName() {
		return mGroupName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setValue(String value) {
		mValue = value;
	}
	
	public String getValue() {
		return mValue;
	}	
	
	////////////////////////////////////////////////
	//	next node list
	////////////////////////////////////////////////

	public GlobalData next () {
		return (GlobalData)getNextNode();
	}

	////////////////////////////////////////////////
	//	toString
	////////////////////////////////////////////////

	public String toString() {
		return getName() + " = " + getValue();
	}

};
