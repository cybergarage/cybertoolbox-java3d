/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : PositionInterpolator.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class PositionInterpolatorNode extends InterpolatorNode {

	private String	keyValueFieldName	= "keyValue";

	public PositionInterpolatorNode() {
		setHeaderFlag(false);
		setType(positionInterpolatorTypeName);

		// keyValue exposed field
		MFVec3f keyValue = new MFVec3f();
		addExposedField(keyValueFieldName, keyValue);

		// value_changed eventOut field
		ConstSFVec3f valueChanged = new ConstSFVec3f(0.0f, 0.0f, 0.0f);
		addEventOut(valueFieldName, valueChanged);
	}

	public PositionInterpolatorNode(PositionInterpolatorNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	keyValue
	////////////////////////////////////////////////
	
	public void addKeyValue(float vector[]) {
		getKeyValueField().addValue(vector);
	}

	public void addKeyValue(float x, float y, float z) {
		getKeyValueField().addValue(x, y, z);
	}

	public int getNKeyValues() {
		return getKeyValueField().getSize();
	}

	public void setKeyValue(int index, float vector[]) {
		getKeyValueField().set1Value(index, vector);
	}
	
	public void setKeyValue(int index, float x, float y, float z) {
		getKeyValueField().set1Value(index, x, y, z);
	}

	public void setKeyValues(String value) {
		getKeyValueField().setValues(value);
	}

	public void setKeyValues(String value[]) {
		getKeyValueField().setValues(value);
	}

	public void getKeyValue(int index, float vector[]) {
		getKeyValueField().get1Value(index, vector);
	}

	public float[] getKeyValue(int index) {
		float value[] = new float[3];
		getKeyValue(index, value);
		return value;
	}

	public void removeKeyValue(int index) {
		getKeyValueField().removeValue(index);
	}

	public MFVec3f getKeyValueField() {
		return (MFVec3f)getExposedField(keyValueFieldName);
	}

	////////////////////////////////////////////////
	//	value
	////////////////////////////////////////////////
	
	public void setValue(float vector[]) {
		getValueField().setValue(vector);
	}

	public void setValue(String vector) {
		getValueField().setValue(vector);
	}

	public void getValue(float vector[]) {
		getValueField().getValue(vector);
	}

	public ConstSFVec3f getValueField() {
		return (ConstSFVec3f)getEventOut(valueFieldName);
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

		float vector1[] = new float[3];
		float vector2[] = new float[3];
		float vectorOut[] = new float[3];

		getKeyValue(index, vector1);
		getKeyValue(index+1, vector2);
		for (int n=0; n<3; n++)
			vectorOut[n] = vector1[n] + (vector2[n] - vector1[n])*scale;

		setValue(vectorOut);
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
	
		float vector[] = new float[3];
		printStream.println(indentString + "\tkeyValue [");
		for (int n=0; n<getNKeyValues(); n++) {
			getKeyValue(n, vector);
			if (n < getNKeyValues()-1)
				printStream.println(indentString + "\t\t" + vector[X] + " " + vector[Y] + " " + vector[Z] + ",");
			else	
				printStream.println(indentString + "\t\t" + vector[X] + " " + vector[Y] + " " + vector[Z]);
		}
		printStream.println(indentString + "\t]");
	}
}