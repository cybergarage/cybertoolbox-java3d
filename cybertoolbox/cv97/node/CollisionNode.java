/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : Collision.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class CollisionNode extends GroupingNode {

	private String	collideFieldName		= "collide";
	private String	collideTimeEventOut		= "collideTime";
	
	public CollisionNode() {
		super();

		setHeaderFlag(false);
		setType(collisionTypeName);

		// collide exposed field
		SFBool collide = new SFBool(true);
		addExposedField(collideFieldName, collide);

		// collide event out
		ConstSFTime collideTime = new ConstSFTime(-1.0);
		addEventOut(collideTimeEventOut, collideTime);
	}

	public CollisionNode(CollisionNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	collide
	////////////////////////////////////////////////

	public void setCollide(boolean value) {
		SFBool collide = (SFBool)getExposedField(collideFieldName);
		collide.setValue(value);
	}

	public void setCollide(String value) {
		SFBool collide = (SFBool)getExposedField(collideFieldName);
		collide.setValue(value);
	}

	public boolean getCollide() {
		SFBool collide = (SFBool)getExposedField(collideFieldName);
		return collide.getValue();
	}

	////////////////////////////////////////////////
	//	collideTime
	////////////////////////////////////////////////

	public void setCollideTime(double value) {
		ConstSFTime collideTime = (ConstSFTime)getEventOut(collideTimeEventOut);
		collideTime.setValue(value);
	}

	public void setCollideTime(String value) {
		ConstSFTime collideTime = (ConstSFTime)getEventOut(collideTimeEventOut);
		collideTime.setValue(value);
	}

	public double getCollideTime() {
		ConstSFTime collideTime = (ConstSFTime)getEventOut(collideTimeEventOut);
		return collideTime.getValue();
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
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFBool collide = (SFBool)getExposedField(collideFieldName);
		printStream.println(indentString + "\t" + "collide " + collide);
	}
}
