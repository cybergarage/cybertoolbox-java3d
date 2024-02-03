/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : XMLElement.java
*
******************************************************************/

package cv97.xml;

import cv97.*;
import cv97.field.*;

public class XMLElement extends Field {

	private StringValue mValue = new StringValue(null); 

	public XMLElement() 
	{
		setType(fieldTypeXMLElement);
	}

	public XMLElement(XMLElement string) 
	{
		setType(fieldTypeXMLElement);
		setValue(string);
	}

	public XMLElement(String value) 
	{
		setType(fieldTypeXMLElement);
		setValue(value);
	}

	public void setValue(String value) 
	{
		synchronized (mValue) {
			mValue.setValue(value);
		}
	}

	public void setValue(XMLElement ele) 
	{
		setValue(ele.getValue());
	}

	public String getValue() 
	{
		String value;
		synchronized (mValue) {
			value = mValue.getValue();
		}
		return value;
	}

	////////////////////////////////////////////////
	//	abstract methods
	////////////////////////////////////////////////

	public void setValue(Field field)
	{
		if (field instanceof XMLElement)
			setValue((XMLElement)field);
	}

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
}