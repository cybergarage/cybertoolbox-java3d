/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : SFBool.java
*
******************************************************************/

package cv97.field;

import cv97.*;

public class SFBool extends Field {

	private BoolValue mValue = new BoolValue(true); 

	public SFBool() {
		setType(fieldTypeSFBool);
		setValue(true);
	}

	public SFBool(SFBool value) {
		setType(fieldTypeSFBool);
		setValue(value);
	}

	public SFBool(ConstSFBool value) {
		setType(fieldTypeSFBool);
		setValue(value);
	}

	public SFBool(boolean value) {
		setType(fieldTypeSFBool);
		setValue(value);
	}

	public SFBool(String value) {
		setType(fieldTypeSFBool);
		setValue(value);
	}

	public void setValue(boolean value) {
		synchronized (mValue) {
			mValue.setValue(value);
		}
	}

	public void setValue(SFBool value) {
		setValue(value.getValue());
	}

	public void setValue(ConstSFBool value) {
		setValue(value.getValue());
	}

	public void setValue(String value) {
		if (value.equalsIgnoreCase("TRUE") == true)
			setValue(true);
		else
			setValue(false);
	}

	public void setValue(Field field) {
		if (field instanceof SFBool)
			setValue((SFBool)field);
		if (field instanceof ConstSFBool)
			setValue((ConstSFBool)field);
	}

	public boolean getValue() {
		boolean value;
		synchronized (mValue) {
			value = mValue.getValue();
		}
		return value;
	}

	////////////////////////////////////////////////
	//	Object
	////////////////////////////////////////////////

	public void setObject(Object object) {
		synchronized (mValue) {
			mValue = (BoolValue)object;
		}
	}

	public Object getObject() {
		Object object;
		synchronized (mValue) {
			object = mValue;
		}
		return object;
	}
	
	////////////////////////////////////////////////
	//	toString
	////////////////////////////////////////////////

	public String toString() {
		if (getValue() == true)
			return "TRUE";
		else
			return "FALSE";
	}

	public String toXMLString()
	{
		if (getValue() == true)
			return "true";
		else
			return "false";
	}
}