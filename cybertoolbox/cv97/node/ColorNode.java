/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : Color.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class ColorNode extends Node {

	private String	colorFieldName = "color";

	public ColorNode() {
		setHeaderFlag(false);
		setType(colorTypeName);

		// color exposed field
		MFColor mfcolor = new MFColor();
		mfcolor.setName(colorFieldName);
		addExposedField(mfcolor);
	}

	public ColorNode(ColorNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	color
	////////////////////////////////////////////////
	
	public void addColor(float color[]) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.addValue(color);
	}
	
	public void addColor(float r, float g, float b) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.addValue(r, g, b);
	}
	
	public int getNColors() {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		return mfcolor.getSize();
	}
	
	public void setColor(int index, float color[]) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.set1Value(index, color);
	}
	
	public void setColor(int index, float r, float g, float b) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.set1Value(index, r, g, b);
	}

	public void setColors(String value) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.setValues(value);
	}

	public void setColors(String value[]) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.setValues(value);
	}
	
	public void getColor(int index, float color[]) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.get1Value(index, color);
	}
	
	public float[] getColor(int index) {
		float value[] = new float[3];
		getColor(index, value);
		return value;
	}
	
	public void removeColor(int index) {
		MFColor mfcolor = (MFColor)getExposedField(colorFieldName);
		mfcolor.removeValue(index);
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
	}

	////////////////////////////////////////////////
	//	Output
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		float color[] = new float[3];
		printStream.println(indentString + "\tcolor [");
		for (int n=0; n<getNColors(); n++) {
			getColor(n, color);
			if (n < getNColors()-1)
				printStream.println(indentString + "\t\t" + color[X] + " " + color[Y] + " " + color[Z] + ",");
			else	
				printStream.println(indentString + "\t\t" + color[X] + " " + color[Y] + " " + color[Z]);
		}
		printStream.println(indentString + "\t]");
	}
}