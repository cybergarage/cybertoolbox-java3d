/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : SFString.java
*
******************************************************************/

package cv97.field;

import cv97.*;

public class SFString extends Field {

	private StringValue mValue = new StringValue(null); 

	public SFString() {
		setType(fieldTypeSFString);
	}

	public SFString(SFString string) {
		setType(fieldTypeSFString);
		setValue(string);
	}

	public SFString(ConstSFString string) {
		setType(fieldTypeSFString);
		setValue(string);
	}

	public SFString(String value) {
		setType(fieldTypeSFString);
		setValue(value);
	}

	public void setValue(String value) {
		synchronized (mValue) {
			mValue.setValue(value);
		}
	}

	public void setValue(SFString value) {
		setValue(value.getValue());
	}

	public void setValue(ConstSFString value) {
		setValue(value.getValue());
	}

	public void setValue(Field field) {
		if (field instanceof SFString)
			setValue((SFString)field);
		if (field instanceof ConstSFString)
			setValue((ConstSFString)field);
	}

	public String getValue() {
		String value;
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
			mValue = (StringValue)object;
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

	public String toString() 
	{
		String value = getValue();
		if (value == null)
			return new String("\"\""); 
		return "\"" + value + "\"";
	}

	public String toXMLString()
	{
		String value = getValue();
		if (value == null)
			return "";
		return value;
	}
}
