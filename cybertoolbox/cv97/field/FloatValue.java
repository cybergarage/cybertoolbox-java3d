/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : FloatValue.java
*
******************************************************************/

package cv97.field;

import cv97.*;

public class FloatValue extends Object {
	
	private float mValue; 

	public FloatValue(float value) {
		setValue(value);
	}
	
	public void setValue(float value) {
		mValue = value;
	}
	
	public float getValue() {
		return mValue;
	}	
}
