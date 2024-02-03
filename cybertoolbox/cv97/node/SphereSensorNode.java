/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : SphereSensor.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import java.util.Date;
import cv97.*;
import cv97.field.*;

public class SphereSensorNode extends Node {
	
	private String	enabledFieldName		= "enabled";
	private String	autoOffsetFieldName		= "autoOffset";
	private String	offsetFieldName			= "offset";
	private String	rotationEventOutName	= "rotation";
	private String	trackPointEventOutName	= "trackPoint";

	public SphereSensorNode() {
		setHeaderFlag(false);
		setType(sphereSensorTypeName);

		// enabled exposed field
		SFBool enabled = new SFBool(true);
		addExposedField(enabledFieldName, enabled);

		// autoOffset exposed field
		SFBool autoOffset = new SFBool(true);
		addExposedField(autoOffsetFieldName, autoOffset);

		// offset exposed field
		SFRotation offset = new SFRotation(0.0f, 0.0f, 1.0f, 0.0f);
		addExposedField(offsetFieldName, offset);

		// isActive eventOut field
		ConstSFBool isActive = new ConstSFBool(false);
		addEventOut(isActiveFieldName, isActive);

		// rotation eventOut field
		ConstSFRotation rotation = new ConstSFRotation(0.0f, 0.0f, 1.0f, 0.0f);
		addEventOut(rotationEventOutName, rotation);

		// trackPoint eventOut field
		ConstSFVec3f trackPoint = new ConstSFVec3f(0.0f, 0.0f, 0.0f);
		addEventOut(trackPointEventOutName, trackPoint);
	}

	public SphereSensorNode(SphereSensorNode node) {
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
	//	AutoOffset
	////////////////////////////////////////////////
	
	public void setAutoOffset(boolean value) {
		SFBool sfbool = (SFBool)getExposedField(autoOffsetFieldName);
		sfbool.setValue(value);
	}

	public void setAutoOffset(String value) {
		SFBool sfbool = (SFBool)getExposedField(autoOffsetFieldName);
		sfbool.setValue(value);
	}

	public boolean getAutoOffset() {
		SFBool sfbool = (SFBool)getExposedField(autoOffsetFieldName);
		return sfbool.getValue();
	}
	
	public boolean isAutoOffset() {
		return getAutoOffset();
	}

	////////////////////////////////////////////////
	//	Offset
	////////////////////////////////////////////////
	
	public void setOffset(float value[]) {
		SFRotation sfrotation = (SFRotation)getExposedField(offsetFieldName);
		sfrotation.setValue(value);
	}

	public void setOffset(String value) {
		SFRotation sfrotation = (SFRotation)getExposedField(offsetFieldName);
		sfrotation.setValue(value);
	}

	public void getOffset(float value[]) {
		SFRotation sfrotation = (SFRotation)getExposedField(offsetFieldName);
		sfrotation.getValue(value);
	}

	////////////////////////////////////////////////
	//	isActive
	////////////////////////////////////////////////
	
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
		return getAutoOffset();
	}

	////////////////////////////////////////////////
	//	Rotation
	////////////////////////////////////////////////
	
	public void setRotationChanged(float value[]) {
		ConstSFRotation time = (ConstSFRotation)getEventOut(rotationEventOutName);
		time.setValue(value);
	}
	
	public void setRotationChanged(float x, float y, float z, float rot) {
		ConstSFRotation time = (ConstSFRotation)getEventOut(rotationEventOutName);
		time.setValue(x, y, z, rot);
	}

	public void setRotationChanged(String value) {
		ConstSFRotation time = (ConstSFRotation)getEventOut(rotationEventOutName);
		time.setValue(value);
	}
	
	public void getRotationChanged(float value[]) {
		ConstSFRotation time = (ConstSFRotation)getEventOut(rotationEventOutName);
		time.getValue(value);
	}

	public void setRotation(float value[]) {
		setRotationChanged(value);
	}
	
	public void setRotation(float x, float y, float z, float rot) {
		setRotationChanged(x, y, z, rot);
	}

	public void setRotation(String value) {
		setRotationChanged(value);
	}
	
	public void getRotation(float value[]) {
		getRotationChanged(value);
	}

	////////////////////////////////////////////////
	//	TrackPoint
	////////////////////////////////////////////////
	
	public void setTrackPointChanged(float value[]) {
		ConstSFVec3f sfvec3f = (ConstSFVec3f)getEventOut(trackPointEventOutName);
		sfvec3f.setValue(value);
	}
	public void setTrackPointChanged(float x, float y, float z) {
		ConstSFVec3f sfvec3f = (ConstSFVec3f)getEventOut(trackPointEventOutName);
		sfvec3f.setValue(x, y, z);
	}

	public void setTrackPointChanged(String value) {
		ConstSFVec3f sfvec3f = (ConstSFVec3f)getEventOut(trackPointEventOutName);
		sfvec3f.setValue(value);
	}
	
	public void getTrackPointChanged(float value[]) {
		ConstSFVec3f sfvec3f = (ConstSFVec3f)getEventOut(trackPointEventOutName);
		sfvec3f.getValue(value);
	}

	public void setTrackPoint(float value[]) {
		setTrackPointChanged(value);
	}
	
	public void setTrackPoint(float x, float y, float z) {
		setTrackPointChanged(x, y, z);
	}

	public void setTrackPoint(String value) {
		setTrackPointChanged(value);
	}
	
	public void getTrackPoint(float value[]) {
		getTrackPointChanged(value);
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		return false;
	}

	public void initialize() {
		super.initialize();
		setIsActive(false);
	}

	public void uninitialize() {
	}

	public void update() {
	}

	////////////////////////////////////////////////
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFBool autoOffset = (SFBool)getExposedField(autoOffsetFieldName);
		SFBool enabled = (SFBool)getExposedField(enabledFieldName);
		SFRotation offset = (SFRotation)getExposedField(offsetFieldName);

		printStream.println(indentString + "\t" + "autoOffset " + autoOffset );
		printStream.println(indentString + "\t" + "enabled " + enabled );
		printStream.println(indentString + "\t" + "offset " + offset );
	}
}
