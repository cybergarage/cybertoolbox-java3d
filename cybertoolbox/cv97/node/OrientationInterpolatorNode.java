/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : OrientationInterpolator.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class OrientationInterpolatorNode extends InterpolatorNode {

	private String	keyValueFieldName	= "keyValue";

	public OrientationInterpolatorNode() {
		setHeaderFlag(false);
		setType(orientationInterpolatorTypeName);

		// keyValue exposed field
		MFRotation keyValue = new MFRotation();
		addExposedField(keyValueFieldName, keyValue);

		// value_changed eventOut field
		ConstSFRotation valueChanged = new ConstSFRotation(0.0f, 0.0f, 1.0f, 0.0f);
		addEventOut(valueFieldName, valueChanged);
	}

	public OrientationInterpolatorNode(OrientationInterpolatorNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	keyValue
	////////////////////////////////////////////////
	
	public void addKeyValue(float rotation[]) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.addValue(rotation);
	}

	public void addKeyValue(float x, float y, float z, float angle) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.addValue(x, y, z, angle);
	}

	public int getNKeyValues() {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		return keyValue.getSize();
	}
	
	public void setKeyValue(int index, float rotation[]) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.set1Value(index, rotation);
	}

	public void setKeyValue(int index, float x, float y, float z, float angle) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.set1Value(index, x, y, z, angle);
	}

	public void setKeyValues(String value) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.setValues(value);
	}

	public void setKeyValues(String value[]) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.setValues(value);
	}

	public void getKeyValue(int index, float rotation[]) {
		MFRotation keyValue = (MFRotation)getExposedField(keyValueFieldName);
		keyValue.get1Value(index, rotation);
	}

	public float[] getKeyValue(int index) {
		float value[] = new float[4];
		getKeyValue(index, value);
		return value;
	}


	public void removeKeyValue(int index) {
		MFVec3f keyValue = (MFVec3f)getExposedField(keyValueFieldName);
		keyValue.removeValue(index);
	}

	public Field getKeyValueField() {
		return getExposedField(keyValueFieldName);
	}

	////////////////////////////////////////////////
	//	value
	////////////////////////////////////////////////
	
	public void setValue(float rotation[]) {
		ConstSFRotation value = (ConstSFRotation)getEventOut(valueFieldName);
		value.setValue(rotation);
	}

	public void setValue(String rotation) {
		ConstSFRotation value = (ConstSFRotation)getEventOut(valueFieldName);
		value.setValue(rotation);
	}

	public void getValue(float rotation[]) {
		ConstSFRotation value = (ConstSFRotation)getEventOut(valueFieldName);
		value.getValue(rotation);
	}

	public Field getValueField() {
		return getEventOut(valueFieldName);
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		return false;
	}

	public void initialize() {
		super.initialize();
	}

	public void uninitialize() {
	}

	public void update() {

		float fraction = getFraction();
		int index = -1;
		for (int n=0; n<(getNKeys()-1); n++) {
			if (getKey(n) <= fraction && fraction <= getKey(n+1)) {
				index = n;
				break;
			}
		}
		if (index == -1)
			return;

		float scale = (fraction - getKey(index)) / (getKey(index+1) - getKey(index));

		float rotation1[] = new float[4];
		float rotation2[] = new float[4];
		float rotationOut[] = new float[4];

		getKeyValue(index, rotation1);
		getKeyValue(index+1, rotation2);
		for (int n=0; n<4; n++)
			rotationOut[n] = rotation1[n] + (rotation2[n] - rotation1[n])*scale;

		setValue(rotationOut);
		sendEvent(getValueField());
	}

	////////////////////////////////////////////////
	//	Output
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {

		printStream.println(indentString + "\tkey [");
		for (int n=0; n<getNKeys(); n++) {
			if (n < getNKeys()-1)
				printStream.println(indentString + "\t\t" + getKey(n));
			else	
				printStream.println(indentString + "\t\t" + getKey(n));
		}
		printStream.println(indentString + "\t]");
	
		float rotation[] = new float[4];
		printStream.println(indentString + "\tkeyValue [");
		for (int n=0; n<getNKeyValues(); n++) {
			getKeyValue(n, rotation);
			if (n < getNKeyValues()-1)
				printStream.println(indentString + "\t\t" + rotation[X] + " " + rotation[Y] + " " + rotation[Z] + " " + rotation[3] + ",");
			else	
				printStream.println(indentString + "\t\t" + rotation[X] + " " + rotation[Y] + " " + rotation[Z] + " " + rotation[3]);
		}
		printStream.println(indentString + "\t]");
	}
}