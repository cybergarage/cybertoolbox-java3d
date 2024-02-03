/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ScalarInterpolator.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class ScalarInterpolatorNode extends InterpolatorNode {

	private String	keyValueFieldName	= "keyValue";

	public ScalarInterpolatorNode() {
		setHeaderFlag(false);
		setType(scalarInterpolatorTypeName);

		// keyValue exposed field
		MFFloat keyValue = new MFFloat();
		addExposedField(keyValueFieldName, keyValue);

		// value_changed eventOut field
		ConstSFFloat valueChanged = new ConstSFFloat(0.0f);
		addEventOut(valueFieldName, valueChanged);
	}

	public ScalarInterpolatorNode(ScalarInterpolatorNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	keyValue
	////////////////////////////////////////////////
	
	public void addKeyValue(float value) {
		getKeyValueField().addValue(value);
	}

	public int getNKeyValues() {
		return getKeyValueField().getSize();
	}
	
	public void setKeyValue(int index, float value) {
		getKeyValueField().set1Value(index, value);
	}

	public void setKeyValues(String value) {
		getKeyValueField().setValues(value);
	}

	public void setKeyValues(String value[]) {
		getKeyValueField().setValues(value);
	}

	public float getKeyValue(int index) {
		return getKeyValueField().get1Value(index);
	}

	public void removeKeyValue(int index) {
		getKeyValueField().removeValue(index);
	}

	public MFFloat getKeyValueField() {
		return (MFFloat)getExposedField(keyValueFieldName);
	}

	////////////////////////////////////////////////
	//	value
	////////////////////////////////////////////////
	
	public void setValue(float vector) {
		getValueField().setValue(vector);
	}

	public void setValue(String vector) {
		getValueField().setValue(vector);
	}

	public float getValue() {
		return getValueField().getValue();
	}

	public ConstSFFloat getValueField() {
		return (ConstSFFloat)getEventOut(valueFieldName);
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


		float value1 = getKeyValue(index);
		float value2 = getKeyValue(index+1);
		float valueOut = value1 + (value2 - value1)*scale;

		setValue(valueOut);
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
	
		printStream.println(indentString + "\tkeyValue [");
		for (int n=0; n<getNKeyValues(); n++) {
			if (n < getNKeyValues()-1)
				printStream.println(indentString + "\t\t" + getKeyValue(n));
			else	
				printStream.println(indentString + "\t\t" + getKeyValue(n));
		}
		printStream.println(indentString + "\t]");
	}
}