/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1999
*
*	File : InterpolatorNode.java
*
******************************************************************/

package cv97.node;

import cv97.*;
import cv97.field.*;

public abstract class InterpolatorNode extends Node {

	private String	keyFieldName		= "key";

	public InterpolatorNode() {
		setHeaderFlag(false);

		// key exposed field
		MFFloat key = new MFFloat();
		addExposedField(keyFieldName, key);

		// set_fraction eventIn field
		SFFloat setFraction = new SFFloat(0.0f);
		addEventIn(fractionFieldName, setFraction);
	}

	////////////////////////////////////////////////
	//	key
	////////////////////////////////////////////////
	
	public void addKey(float value) {
		getKeyField().addValue(value);
	}
	
	public int getNKeys() {
		return getKeyField().getSize();
	}
	
	public void setKey(int index, float value) {
		getKeyField().set1Value(index, value);
	}

	public void setKeys(String value) {
		getKeyField().setValues(value);
	}

	public void setKeys(String value[]) {
		getKeyField().setValues(value);
	}
	
	public float getKey(int index) {
		return getKeyField().get1Value(index);
	}
	
	public void removeKey(int index) {
		getKeyField().removeValue(index);
	}
	
	public MFFloat getKeyField() {
		return (MFFloat)getExposedField(keyFieldName);
	}

	////////////////////////////////////////////////
	//	fraction
	////////////////////////////////////////////////
	
	public void setFraction(float value) {
		getFractionField().setValue(value);
	}

	public void setFraction(String value) {
		getFractionField().setValue(value);
	}

	public float getFraction() {
		return getFractionField().getValue();
	}

	public SFFloat getFractionField() {
		return (SFFloat)getEventIn(fractionFieldName);
	}
}