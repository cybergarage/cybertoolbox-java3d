/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ColorInterpolator.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class ColorInterpolatorNode extends InterpolatorNode {

	private String	keyValueFieldName	= "keyValue";

	public ColorInterpolatorNode() {
		setHeaderFlag(false);
		setType(colorInterpolatorTypeName);

		// keyValue exposed field
		MFColor keyValue = new MFColor();
		addExposedField(keyValueFieldName, keyValue);

		// value_changed eventOut field
		ConstSFColor valueChanged = new ConstSFColor(0.0f, 0.0f, 0.0f);
		addEventOut(valueFieldName, valueChanged);
	}

	public ColorInterpolatorNode(ColorInterpolatorNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	keyValue
	////////////////////////////////////////////////
	
	public void addKeyValue(float color[]) {
		getKeyValueField().addValue(color);
	}

	public void addKeyValue(float r, float g, float b) {
		getKeyValueField().addValue(r, g, b);
	}

	public int getNKeyValues() {
		return getKeyValueField().getSize();
	}
	
	public void setKeyValue(int index, float color[]) {
		getKeyValueField().set1Value(index, color);
	}

	public void setKeyValue(int index, float r, float g, float b) {
		getKeyValueField().set1Value(index, r, g, b);
	}

	public void setKeyValues(String color) {
		getKeyValueField().setValues(color);
	}

	public void setKeyValues(String color[]) {
		getKeyValueField().setValues(color);
	}

	public void getKeyValue(int index, float color[]) {
		getKeyValueField().get1Value(index, color);
	}

	public float[] getKeyValue(int index) {
		float value[] = new float[3];
		getKeyValue(index, value);
		return value;
	}

	public void removeKeyValue(int index) {
		getKeyValueField().removeValue(index);
	}

	public MFColor getKeyValueField() {
		return (MFColor)getExposedField(keyValueFieldName);
	}

	////////////////////////////////////////////////
	//	value
	////////////////////////////////////////////////
	
	public void setValue(float color[]) {
		getValueField().setValue(color);
	}

	public void setValue(String color) {
		getValueField().setValue(color);
	}

	public void getValue(float color[]) {
		getValueField().getValue(color);
	}

	public ConstSFColor getValueField() {
		return (ConstSFColor)getEventOut(valueFieldName);
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

		float color1[] = new float[3];
		float color2[] = new float[3];
		float colorOut[] = new float[3];

		getKeyValue(index, color1);
		getKeyValue(index+1, color2);
		for (int n=0; n<3; n++)
			colorOut[n] = color1[n] + (color2[n] - color1[n])*scale;

		setValue(colorOut);
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
	
		float color[] = new float[3];
		printStream.println(indentString + "\tkeyValue [");
		for (int n=0; n<getNKeyValues(); n++) {
			getKeyValue(n, color);
			if (n < getNKeyValues()-1)
				printStream.println(indentString + "\t\t" + color[X] + " " + color[Y] + " " + color[Z] + ",");
			else	
				printStream.println(indentString + "\t\t" + color[X] + " " + color[Y] + " " + color[Z]);
		}
		printStream.println(indentString + "\t]");
	}

	////////////////////////////////////////////////
	//	List
	////////////////////////////////////////////////

/* for Visual C++
	public ColorInterpolator next() {
		return (ColorInterpolator)next(getType());
	}

	public ColorInterpolator nextTraversal() {
		return (ColorInterpolator)nextTraversalByType(getType());
	}
*/

}