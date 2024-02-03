/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ConstSFBool.java
*
******************************************************************/

package cv97.field;

import cv97.*;

public class ConstSFBool extends ConstField {

	private BoolValue mValue = new BoolValue(true); 

	public ConstSFBool() {
		setType(fieldTypeConstSFBool);
		setValue(true);
	}

	public ConstSFBool(SFBool value) {
		setType(fieldTypeConstSFBool);
		setValue(value);
	}

	public ConstSFBool(ConstSFBool value) {
		setType(fieldTypeConstSFBool);
		setValue(value);
	}

	public ConstSFBool(boolean value) {
		setType(fieldTypeConstSFBool);
		setValue(value);
	}

	public ConstSFBool(String value) {
		setType(fieldTypeConstSFBool);
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
		if (value.compareTo("TRUE") == 0)
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

	////////////////////////////////////////////////
	//	Referrence Field Object 
	////////////////////////////////////////////////

	public Field createReferenceFieldObject() {
		SFBool field = new SFBool();
		field.setName(getName());
		field.setObject(getObject());
		return field;
	}

	public String toXMLString()
	{
		if (getValue() == true)
			return "true";
		else
			return "false";
	}
}