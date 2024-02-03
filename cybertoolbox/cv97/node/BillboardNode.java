/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : Billboard.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class BillboardNode extends GroupingNode {

	private String	axisOfRotationFieldName		= "axisOfRotation";
	
	public BillboardNode() {
		super();

		setHeaderFlag(false);
		setType(billboardTypeName);

		// axisOfRotation exposed field
		SFVec3f axisOfRotation = new SFVec3f(0.0f, 1.0f, 0.0f);
		addExposedField(axisOfRotationFieldName, axisOfRotation);
	}

	public BillboardNode(BillboardNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	axisOfRotation
	////////////////////////////////////////////////

	public void setAxisOfRotation(float value[]) {
		SFVec3f axisOfRotation = (SFVec3f)getExposedField(axisOfRotationFieldName);
		axisOfRotation.setValue(value);
	}

	public void setAxisOfRotation(float x, float y, float z) {
		SFVec3f axisOfRotation = (SFVec3f)getExposedField(axisOfRotationFieldName);
		axisOfRotation.setValue(x, y, z);
	}

	public void setAxisOfRotation(String value) {
		SFVec3f axisOfRotation = (SFVec3f)getExposedField(axisOfRotationFieldName);
		axisOfRotation.setValue(value);
	}

	public void getAxisOfRotation(float value[]) {
		SFVec3f axisOfRotation = (SFVec3f)getExposedField(axisOfRotationFieldName);
		axisOfRotation.getValue(value);
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		if (node.isCommonNode() || node.isBindableNode() ||node.isInterpolatorNode() || node.isSensorNode() || node.isGroupingNode() || node.isSpecialGroupNode())
			return true;
		else
			return false;
	}

	public void initialize() {
		super.initialize();
		updateChildrenField();
		calculateBoundingBox();
	}

	public void uninitialize() {
	}

	public void update() {
		//updateChildrenField();
		//calculateBoundingBox();
	}

	////////////////////////////////////////////////
	//	Matrix
	////////////////////////////////////////////////

	public void getSFMatrix(SFMatrix mOut)
	{
		mOut.init();
	}

	public SFMatrix getSFMatrix() {
		SFMatrix mx = new SFMatrix();
		getSFMatrix(mx);
		return mx;
	}

	////////////////////////////////////////////////
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFVec3f axisOfRotation = (SFVec3f)getExposedField(axisOfRotationFieldName);
		printStream.println(indentString + "\t" + "axisOfRotation " + axisOfRotation);
	}
}
