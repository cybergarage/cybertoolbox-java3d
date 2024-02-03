/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : TouchSensor.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import java.util.Date;
import cv97.*;
import cv97.field.*;

public class TouchSensorNode extends Node {
	
	private String	enabledFieldName			= "enabled";

	private String	hitNormalEventOutName	= "hitNormal";
	private String	hitPointEventOutName		= "hitPoint";
	private String	hitTexCoordEventOutName	= "hitTexCoord";
	private String	isOverEventOutName		= "isOver";
	private String	touchTimeEventOutName	= "touchTime";

	public TouchSensorNode() {
		setHeaderFlag(false);
		setType(touchSensorTypeName);

		// enabled exposed field
		SFBool enabled = new SFBool(true);
		addExposedField(enabledFieldName, enabled);

	
		// isActive eventOut field
		ConstSFBool isActive = new ConstSFBool(false);
		addEventOut(isActiveFieldName, isActive);

		// hitNormal eventOut field
		ConstSFVec3f hitNormal = new ConstSFVec3f(0.0f, 0.0f, 0.0f);
		addEventOut(hitNormalEventOutName, hitNormal);

		// hitTexCoord eventOut field
		ConstSFVec2f hitTexCoord = new ConstSFVec2f(0.0f, 0.0f);
		addEventOut(hitTexCoordEventOutName, hitTexCoord);

		// hitPoint eventOut field
		ConstSFVec3f hitPoint = new ConstSFVec3f(0.0f, 0.0f, 0.0f);
		addEventOut(hitPointEventOutName, hitPoint);

		// isOver eventOut field
		ConstSFBool isOver = new ConstSFBool(false);
		addEventOut(isOverEventOutName, isOver);

		// exitTime eventOut field
		ConstSFTime time = new ConstSFTime(0.0f);
		addEventOut(touchTimeEventOutName, time);
	}

	public TouchSensorNode(TouchSensorNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	Enabled
	////////////////////////////////////////////////
	
	public void setEnabled(boolean value) {
		SFBool bEnabled = (SFBool)getExposedField(enabledFieldName);
		bEnabled.setValue(value);
	}

	public void setEnabled(String value) {
		SFBool bEnabled = (SFBool)getExposedField(enabledFieldName);
		bEnabled.setValue(value);
	}
	
	public boolean getEnabled() {
		SFBool bEnabled = (SFBool)getExposedField(enabledFieldName);
		return bEnabled.getValue();
	}
	
	public boolean isEnabled() {
		return getEnabled();
	}

	////////////////////////////////////////////////
	//	isActive
	////////////////////////////////////////////////
	
	public ConstSFBool getIsActiveField() {
		return (ConstSFBool)getEventOut(isActiveFieldName);
	}
	
	public void setIsActive(boolean value) {
		ConstSFBool sfbool = (ConstSFBool)getEventOut(isActiveFieldName);
		sfbool.setValue(value);
	}

	public void setIsActive(String value) {
		ConstSFBool sfbool = (ConstSFBool)getEventOut(isActiveFieldName);
		sfbool.setValue(value);
	}
	
	public boolean getIsActive() {
		ConstSFBool sfbool = (ConstSFBool)getEventOut(isActiveFieldName);
		return sfbool.getValue();
	}
	
	public boolean isActive() {
		return getIsActive();
	}
	
	////////////////////////////////////////////////
	//	isOver
	////////////////////////////////////////////////
	
	public void setIsOver(boolean value) {
		ConstSFBool sfbool = (ConstSFBool)getEventOut(isOverEventOutName);
		sfbool.setValue(value);
	}

	public void setIsOver(String value) {
		ConstSFBool sfbool = (ConstSFBool)getEventOut(isOverEventOutName);
		sfbool.setValue(value);
	}
	
	public boolean getIsOver() {
		ConstSFBool sfbool = (ConstSFBool)getEventOut(isOverEventOutName);
		return sfbool.getValue();
	}
	
	public boolean isOver() {
		return getIsOver();
	}

	////////////////////////////////////////////////
	//	hitNormal
	////////////////////////////////////////////////
	
	public void setHitNormalChanged(float value[]) {
		ConstSFVec3f normal = (ConstSFVec3f)getEventOut(hitNormalEventOutName);
		normal.setValue(value);
	}
	
	public void setHitNormalChanged(float x, float y, float z) {
		ConstSFVec3f normal = (ConstSFVec3f)getEventOut(hitNormalEventOutName);
		normal.setValue(x, y, z);
	}

	public void setHitNormalChanged(String value) {
		ConstSFVec3f normal = (ConstSFVec3f)getEventOut(hitNormalEventOutName);
		normal.setValue(value);
	}
	
	public void getHitNormalChanged(float value[]) {
		ConstSFVec3f normal = (ConstSFVec3f)getEventOut(hitNormalEventOutName);
		normal.getValue(value);
	}

	public void setHitNormal(float value[]) {
		setHitNormalChanged(value);
	}
	
	public void setHitNormal(float x, float y, float z) {
		setHitNormalChanged(x, y, z);
	}

	public void setHitNormal(String value) {
		setHitNormalChanged(value);
	}
	
	public void getHitNormal(float value[]) {
		getHitNormalChanged(value);
	}

	////////////////////////////////////////////////
	//	hitPoint
	////////////////////////////////////////////////
	
	public void setHitPointChanged(float value[]) {
		ConstSFVec3f point = (ConstSFVec3f)getEventOut(hitPointEventOutName);
		point.setValue(value);
	}
	
	public void setHitPointChanged(float x, float y, float z) {
		ConstSFVec3f point = (ConstSFVec3f)getEventOut(hitPointEventOutName);
		point.setValue(x, y, z);
	}

	public void setHitPointChanged(String value) {
		ConstSFVec3f point = (ConstSFVec3f)getEventOut(hitPointEventOutName);
		point.setValue(value);
	}
	
	public void getHitPointChanged(float value[]) {
		ConstSFVec3f point = (ConstSFVec3f)getEventOut(hitPointEventOutName);
		point.getValue(value);
	}

	public void setHitPoint(float value[]) {
		setHitPointChanged(value);
	}
	
	public void setHitPoint(float x, float y, float z) {
		setHitPointChanged(x, y, z);
	}

	public void setHitPoint(String value) {
		setHitPointChanged(value);
	}
	
	public void getHitPoint(float value[]) {
		getHitPointChanged(value);
	}

	////////////////////////////////////////////////
	//	hitTexCoord
	////////////////////////////////////////////////
	
	public void setHitTexCoord(float value[]) {
		ConstSFVec2f point = (ConstSFVec2f)getEventOut(hitTexCoordEventOutName);
		point.setValue(value);
	}
	
	public void setHitTexCoord(float x, float y) {
		ConstSFVec2f point = (ConstSFVec2f)getEventOut(hitTexCoordEventOutName);
		point.setValue(x, y);
	}

	public void setHitTexCoord(String value) {
		ConstSFVec2f point = (ConstSFVec2f)getEventOut(hitTexCoordEventOutName);
		point.setValue(value);
	}
	
	public void getHitTexCoord(float value[]) {
		ConstSFVec2f point = (ConstSFVec2f)getEventOut(hitTexCoordEventOutName);
		point.getValue(value);
	}

	////////////////////////////////////////////////
	//	ExitTime
	////////////////////////////////////////////////
	
	public ConstSFTime getTouchTimeField() {
		return (ConstSFTime)getEventOut(touchTimeEventOutName);
	}
	
	public void setTouchTime(double value) {
		ConstSFTime time = (ConstSFTime)getEventOut(touchTimeEventOutName);
		time.setValue(value);
	}

	public void setTouchTime(String value) {
		ConstSFTime time = (ConstSFTime)getEventOut(touchTimeEventOutName);
		time.setValue(value);
	}
	
	public double getTouchTime() {
		ConstSFTime time = (ConstSFTime)getEventOut(touchTimeEventOutName);
		return time.getValue();
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
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFBool enabled = (SFBool)getExposedField(enabledFieldName);
		printStream.println(indentString + "\t" + "enabled " + enabled );
	}
}
