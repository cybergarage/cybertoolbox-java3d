/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : MovieTexture.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.lang.String;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class MovieTextureNode extends TextureNode {
	
	//// Exposed Field ////////////////
	private String	urlFieldName			= "url";
	private String	loopFieldName			= "loop";
	private String	startTimeFieldName	= "startTime";
	private String	stopTimeFieldName		= "stopTime";
	private String	speedFieldName			= "speedTime";


	//// Field ////////////////
	private String	repeatSFieldName		= "repeatS";
	private String	repeatTFieldName		= "repeatT";

	public MovieTextureNode() {
		setHeaderFlag(false);
		setType(movieTextureTypeName);

		///////////////////////////
		// Exposed Field 
		///////////////////////////

		// url field
		MFString url = new MFString();
		addExposedField(urlFieldName, url);

		// loop exposed field
		SFBool loop = new SFBool(false);
		addExposedField(loopFieldName, loop);

		// startTime exposed field
		SFTime startTime = new SFTime(0.0f);
		addExposedField(startTimeFieldName, startTime);

		// stopTime exposed field
		SFTime stopTime = new SFTime(0.0f);
		addExposedField(stopTimeFieldName, stopTime);

		// speed exposed field
		SFFloat speed = new SFFloat(1);
		addExposedField(speedFieldName, speed);

		///////////////////////////
		// Field 
		///////////////////////////

		// repeatS field
		SFBool repeatS = new SFBool(true);
		addField(repeatSFieldName, repeatS);

		// repeatT field
		SFBool repeatT = new SFBool(true);
		addField(repeatTFieldName, repeatT);

		///////////////////////////
		// EventOut
		///////////////////////////

		// isActive eventOut field
		ConstSFBool isActive = new ConstSFBool(false);
		addEventOut(isActiveFieldName, isActive);

		// time eventOut field
		ConstSFTime durationChanged = new ConstSFTime(-1.0f);
		addEventOut(durationFieldName, durationChanged);
	}

	public MovieTextureNode(MovieTextureNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	RepeatS
	////////////////////////////////////////////////
	
	public void setRepeatS(boolean value) {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		repeatS.setValue(value);
	}

	public void setRepeatS(String value) {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		repeatS.setValue(value);
	}
	
	public boolean getRepeatS() {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		return repeatS.getValue();
	}

	////////////////////////////////////////////////
	//	RepeatT
	////////////////////////////////////////////////
	
	public void setRepeatT(boolean value) {
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		repeatT.setValue(value);
	}

	public void setRepeatT(String value) {
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		repeatT.setValue(value);
	}
	
	public boolean getRepeatT() {
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		return repeatT.getValue();
	}

	////////////////////////////////////////////////
	// URL
	////////////////////////////////////////////////

	public void addURL(String value) {
		MFString url = (MFString)getExposedField(urlFieldName);
		url.addValue(value);
	}
	
	public int getNURLs() {
		MFString url = (MFString)getExposedField(urlFieldName);
		return url.getSize();
	}
	
	public void setURL(int index, String value) {
		MFString url = (MFString)getExposedField(urlFieldName);
		url.set1Value(index, value);
	}

	public void setURLs(String value) {
		MFString url = (MFString)getExposedField(urlFieldName);
		url.setValues(value);
	}

	public void setURLs(String value[]) {
		MFString url = (MFString)getExposedField(urlFieldName);
		url.setValues(value);
	}
	
	public String getURL(int index) {
		MFString url = (MFString)getExposedField(urlFieldName);
		return url.get1Value(index);
	}
	
	public void removeURL(int index) {
		MFString url = (MFString)getExposedField(urlFieldName);
		url.removeValue(index);
	}

	////////////////////////////////////////////////
	//	Loop
	////////////////////////////////////////////////
	
	public void setLoop(boolean value) {
		SFBool loop = (SFBool)getExposedField(loopFieldName);
		loop.setValue(value);
	}

	public void setLoop(String value) {
		SFBool loop = (SFBool)getExposedField(loopFieldName);
		loop.setValue(value);
	}
	
	public boolean getLoop() {
		SFBool loop = (SFBool)getExposedField(loopFieldName);
		return loop.getValue();
	}
	
	public boolean IsLoop() {
		return getLoop();
	}

	////////////////////////////////////////////////
	//	Speed
	////////////////////////////////////////////////
	
	public void setSpeed(float value) {
		SFFloat time = (SFFloat)getExposedField(speedFieldName);
		time.setValue(value);
	}

	public void setSpeed(String value) {
		SFFloat time = (SFFloat)getExposedField(speedFieldName);
		time.setValue(value);
	}
	
	public float getSpeed() {
		SFFloat time = (SFFloat)getExposedField(speedFieldName);
		return time.getValue();
	}

	////////////////////////////////////////////////
	//	Start time
	////////////////////////////////////////////////
	
	public void setStartTime(double value) {
		SFTime time = (SFTime)getExposedField(startTimeFieldName);
		time.setValue(value);
	}

	public void setStartTime(String value) {
		SFTime time = (SFTime)getExposedField(startTimeFieldName);
		time.setValue(value);
	}
	
	public double getStartTime() {
		SFTime time = (SFTime)getExposedField(startTimeFieldName);
		return time.getValue();
	}

	////////////////////////////////////////////////
	//	Stop time
	////////////////////////////////////////////////
	
	public void setStopTime(double value) {
		SFTime time = (SFTime)getExposedField(stopTimeFieldName);
		time.setValue(value);
	}

	public void setStopTime(String value) {
		SFTime time = (SFTime)getExposedField(stopTimeFieldName);
		time.setValue(value);
	}
	
	public double getStopTime() {
		SFTime time = (SFTime)getExposedField(stopTimeFieldName);
		return time.getValue();
	}

	////////////////////////////////////////////////
	//	isActive
	////////////////////////////////////////////////
	
	public void setIsActive(boolean value) {
		ConstSFBool field = (ConstSFBool)getEventOut(isActiveFieldName);
		field.setValue(value);
	}

	public void setIsActive(String value) {
		ConstSFBool field = (ConstSFBool)getEventOut(isActiveFieldName);
		field.setValue(value);
	}
	
	public boolean getIsActive() {
		ConstSFBool field = (ConstSFBool)getEventOut(isActiveFieldName);
		return field.getValue();
	}
	
	public boolean isActive() {
		ConstSFBool field = (ConstSFBool)getEventOut(isActiveFieldName);
		return field.getValue();
	}

	////////////////////////////////////////////////
	//	duration_changed
	////////////////////////////////////////////////
	
	public void setDurationChanged(double value) {
		ConstSFTime time = (ConstSFTime)getEventOut(durationFieldName);
		time.setValue(value);
	}

	public void setDurationChanged(String value) {
		ConstSFTime time = (ConstSFTime)getEventOut(durationFieldName);
		time.setValue(value);
	}
	
	public double getDurationChanged() {
		ConstSFTime time = (ConstSFTime)getEventOut(durationFieldName);
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
	//	infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFBool loop = (SFBool)getExposedField(loopFieldName);
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		SFBool repeatT = (SFBool)getField(repeatTFieldName);

		printStream.println(indentString + "\t" + "loop " + loop );
		printStream.println(indentString + "\t" + "speed " + getSpeed() );
		printStream.println(indentString + "\t" + "startTime " + getStartTime() );
		printStream.println(indentString + "\t" + "stopTime " + getStopTime() );
		printStream.println(indentString + "\t" + "repeatS " + repeatS );
		printStream.println(indentString + "\t" + "repeatT " + repeatT );

		MFString url = (MFString)getExposedField(urlFieldName);
		printStream.println(indentString + "\t" + "url [");
		url.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");
	}
}
