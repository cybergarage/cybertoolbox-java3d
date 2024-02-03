/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File: Cylinder.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import cv97.*;
import cv97.util.*;
import cv97.field.*;

public class CylinderNode extends GeometryNode {

	private String	radiusFieldName		= "radius";
	private String	heightFieldName		= "height";
	private String	topFieldName		= "top";
	private String	sideFieldName		= "side";
	private String	bottomFieldName		= "bottom";
	
	public CylinderNode() {

		setHeaderFlag(false);
		setType(cylinderTypeName);

		// radius field
		SFFloat radius = new SFFloat(1.0f);
		addExposedField(radiusFieldName, radius);

		// height field
		SFFloat height = new SFFloat(2.0f);
		addExposedField(heightFieldName, height);

		// top field
		SFBool top = new SFBool(true);
		addExposedField(topFieldName, top);

		// side field
		SFBool side = new SFBool(true);
		addExposedField(sideFieldName, side);

		// bottom field
		SFBool bottom = new SFBool(true);
		addExposedField(bottomFieldName, bottom);
	}

	public CylinderNode(CylinderNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	radius
	////////////////////////////////////////////////

	public void setRadius(float value) {
		SFFloat radius = (SFFloat)getExposedField(radiusFieldName);
		radius.setValue(value);
	}

	public void setRadius(String value) {
		SFFloat radius = (SFFloat)getExposedField(radiusFieldName);
		radius.setValue(value);
	}
	
	public float getRadius() {
		SFFloat radius = (SFFloat)getExposedField(radiusFieldName);
		return radius.getValue();
	}

	////////////////////////////////////////////////
	//	height
	////////////////////////////////////////////////

	public void setHeight(float value) {
		SFFloat height = (SFFloat)getExposedField(heightFieldName);
		height.setValue(value);
	}

	public void setHeight(String value) {
		SFFloat height = (SFFloat)getExposedField(heightFieldName);
		height.setValue(value);
	}
	
	public float getHeight() {
		SFFloat height = (SFFloat)getExposedField(heightFieldName);
		return height.getValue();
	}

	////////////////////////////////////////////////
	//	top
	////////////////////////////////////////////////

	public void setTop(boolean value) {
		SFBool top = (SFBool)getExposedField(topFieldName);
		top.setValue(value);
	}

	public void setTop(String value) {
		SFBool top = (SFBool)getExposedField(topFieldName);
		top.setValue(value);
	}
	
	public boolean getTop() {
		SFBool top = (SFBool)getExposedField(topFieldName);
		return top.getValue();
	}

	////////////////////////////////////////////////
	//	side
	////////////////////////////////////////////////

	public void setSide(boolean value) {
		SFBool side = (SFBool)getExposedField(sideFieldName);
		side.setValue(value);
	}

	public void setSide(String value) {
		SFBool side = (SFBool)getExposedField(sideFieldName);
		side.setValue(value);
	}

	public boolean getSide() {
		SFBool side = (SFBool)getExposedField(sideFieldName);
		return side.getValue();
	}

	////////////////////////////////////////////////
	//	bottom
	////////////////////////////////////////////////

	public void setBottom(boolean value) {
		SFBool bottom = (SFBool)getExposedField(bottomFieldName);
		bottom.setValue(value);
	}

	public void setBottom(String value) {
		SFBool bottom = (SFBool)getExposedField(bottomFieldName);
		bottom.setValue(value);
	}
	
	public boolean getBottom() {
		SFBool bottom = (SFBool)getExposedField(bottomFieldName);
		return bottom.getValue();
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		return false;
	}

	public void initialize() {
		super.initialize();
		calculateBoundingBox();
	}

	public void uninitialize() {
	}

	public void update() {
	}

	////////////////////////////////////////////////
	//	BoundingBox
	////////////////////////////////////////////////
	
	public void calculateBoundingBox() {
		setBoundingBoxCenter(0.0f, 0.0f, 0.0f);
		setBoundingBoxSize(getRadius(), getHeight()/2.0f, getRadius());
	}

	////////////////////////////////////////////////
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFBool top = (SFBool)getExposedField(topFieldName);
		SFBool side = (SFBool)getExposedField(sideFieldName);
		SFBool bottom = (SFBool)getExposedField(bottomFieldName);

		printStream.println(indentString + "\t" + "radius " + getRadius());
		printStream.println(indentString + "\t" + "height " + getHeight());
		printStream.println(indentString + "\t" + "side " + side);
		printStream.println(indentString + "\t" + "top " + top);
		printStream.println(indentString + "\t" + "bottom " + bottom);
	}
}
