/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	EventType.java
*
******************************************************************/

import cv97.*;
import cv97.field.*;
import cv97.util.*;

////////////////////////////////////////////////////////////
//	Event Type File Format
////////////////////////////////////////////////////////////

/**********************************************************

  ==========================================================

  #CTB EVENTTYPE V3.0

  EVENTTYPENAME EVENTATTRIBUTE MODULECLASSNAME
  ............. ............. ................

  ==========================================================

  EVENTATTRIBUTE	:	"SYSTEM"
						|	"USER"
						;

**********************************************************/

public class EventType extends LinkedListNode {

	public static final String DATA[][] = {
		{"Start",		"System",	"", ""},
		{"Frame",		"System",	"System", "Frame"},
		{"Mouse",		"System",	"System", "Mouse"},
		{"Keyboard",	"System",	"System", "Keyboard"},
		{"Pickup",		"System",	"System", "Pickup"},

		{"Clock",		"User",		"System", "Clock"},
		{"Timer",		"User",		"", ""},
		{"Keyboard",	"User",		"System", "Keyboard"},
		{"Pickup",		"User",		"System", "Pickup"},
		{"Area",			"User",		"System", "Area"},
		{"Collision",	"User",		"System", "Collision"},
		{"User",			"User",		"System", "Message"},
	};

	public static final int SYSTEM_START		= 0;
	public static final int SYSTEM_FRAME		= 1;
	public static final int SYSTEM_MOUSE		= 2;
	public static final int SYSTEM_KEYBOARD	= 3;
	public static final int SYSTEM_PICKUP		= 4;
	public static final int USER_CLOCK			= 5;
	public static final int USER_TIMER			= 6;
	public static final int USER_KEYBOARD		= 7;
	public static final int USER_PICKUP			= 8;
	public static final int USER_AREA			= 9;
	public static final int USER_COLLISION		= 10;
	public static final int USER_USER			= 11;

	public static final String	ATTRIBUTE_SYSTEM_STRING	= "SYSTEM";
	public static final String	ATTRIBUTE_USER_STRING		= "USER";
	public static final int		ATTRIBUTE_NONE					= 0;
	public static final int		ATTRIBUTE_SYSTEM				= 1;
	public static final int		ATTRIBUTE_USER					= 2;

	private	String		mName;
	private	String		mModuleClassName;
	private	String		mModuleName;
	private	int			mEventAttribute;

	public EventType(String name, String attributeString, String moduleClassName, String moduleName) {
		setHeaderFlag(false);
		setName(name);
		setAttribute(attributeString);
		setModuleClassName(moduleClassName);
		setModuleName(moduleName);
	}

	//////////////////////////////////////////////////
	// Name
	//////////////////////////////////////////////////

	public void	setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}

	//////////////////////////////////////////////////
	// Attribute Type
	//////////////////////////////////////////////////

	public void	setAttribute(int type) {
		mEventAttribute = type;
	}

	public void	setAttribute(String attributeString) {
		setAttribute((attributeString.equalsIgnoreCase(ATTRIBUTE_SYSTEM_STRING) == true) ? ATTRIBUTE_SYSTEM : ATTRIBUTE_USER);
	}

	public int getAttribute() {
		return mEventAttribute;
	}
	
	String getAttributeString() {
		return (mEventAttribute == ATTRIBUTE_SYSTEM) ? 
			ATTRIBUTE_SYSTEM_STRING : ATTRIBUTE_USER_STRING;
	}

	//////////////////////////////////////////////////
	// Node Name
	//////////////////////////////////////////////////

	public void	setModuleClassName(String name) {
		mModuleClassName = name;
	}
	
	public String getModuleClassName() {
		return mModuleClassName;
	}

	//////////////////////////////////////////////////
	// Node Name
	//////////////////////////////////////////////////

	public void	setModuleName(String name) {
		mModuleName = name;
	}
	
	public String getModuleName() {
		return mModuleName;
	}

	//////////////////////////////////////////////////
	// next
	//////////////////////////////////////////////////

	public EventType next() {
		return (EventType)getNextNode();
	}

	//////////////////////////////////////////////////
	// toString
	//////////////////////////////////////////////////
	
	public String toString() {
		return getName() + " " + getAttributeString() + " " + getModuleClassName();
	}
}

