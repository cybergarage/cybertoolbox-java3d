/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : Background.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import java.util.Date;
import cv97.*;
import cv97.field.*;

public class BackgroundNode extends BindableNode {
	
	private String	groundColorFieldName	= "groundColor";
	private String	skyColorFieldName		= "skyColor";
	private String	groundAngleFieldName	= "groundAngle";
	private String	skyAngleFieldName		= "skyAngle";
	private String	frontURLFieldName		= "frontUrl";
	private String	backURLFieldName		= "backUrl";
	private String	leftURLFieldName		= "leftUrl";
	private String	rightURLFieldName		= "rightUrl";
	private String	topURLFieldName			= "topUrl";
	private String	bottomURLFieldName		= "bottomUrl";

	public BackgroundNode() {
		setHeaderFlag(false);
		setType(backgroundTypeName);

		// groundColor exposed field
		MFColor groundColor = new MFColor();
		addExposedField(groundColorFieldName, groundColor);

		// skyColor exposed field
		MFColor skyColor = new MFColor();
		addExposedField(skyColorFieldName, skyColor);

		// groundAngle exposed field
		MFFloat groundAngle = new MFFloat();
		addExposedField(groundAngleFieldName, groundAngle);

		// skyAngle exposed field
		MFFloat skyAngle = new MFFloat();
		addExposedField(skyAngleFieldName, skyAngle);

		// url exposed field
		MFString frontURL = new MFString();
		addExposedField(frontURLFieldName, frontURL);

		// url exposed field
		MFString backURL = new MFString();
		addExposedField(backURLFieldName, backURL);

		// url exposed field
		MFString leftURL = new MFString();
		addExposedField(leftURLFieldName, leftURL);

		// url exposed field
		MFString rightURL = new MFString();
		addExposedField(rightURLFieldName, rightURL);

		// url exposed field
		MFString topURL = new MFString();
		addExposedField(topURLFieldName, topURL);

		// url exposed field
		MFString bottomURL = new MFString();
		addExposedField(bottomURLFieldName, bottomURL);
	}

	public BackgroundNode(BackgroundNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	// groundColor
	////////////////////////////////////////////////

	public void addGroundColor(float value[]) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.addValue(value);
	}
	
	public void addGroundColor(float r, float g, float b) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.addValue(r, g, b);
	}
	
	public int getNGroundColors() {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		return groundColor.getSize();
	}
	
	public void setGroundColor(int index, float value[]) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.set1Value(index, value);
	}
	
	public void setGroundColor(int index, float r, float g, float b) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.set1Value(index, r, g, b);
	}
	
	public void setGroundColors(String value) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.setValues(value);
	}
	
	public void setGroundColors(String value[]) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.setValues(value);
	}
	
	public void getGroundColor(int index, float value[]) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.get1Value(index, value);
	}
	
	public float[] getGroundColor(int index) {
		float value[] = new float[3];
		getGroundColor(index, value);
		return value;
	}
	
	public void removeGroundColor(int index) {
		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		groundColor.removeValue(index);
	}

	////////////////////////////////////////////////
	// skyColor
	////////////////////////////////////////////////

	public void addSkyColor(float value[]) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.addValue(value);
	}
	
	public void addSkyColor(float r, float g, float b) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.addValue(r, g, b);
	}
	
	public int getNSkyColors() {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		return skyColor.getSize();
	}
	
	public void setSkyColor(int index, float value[]) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.set1Value(index, value);
	}
	
	public void setSkyColor(int index, float r, float g, float b) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.set1Value(index, r, g, b);
	}
	
	public void setSkyColors(String value) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.setValues(value);
	}

	public void setSkyColors(String value[]) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.setValues(value);
	}
	
	public void getSkyColor(int index, float value[]) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.get1Value(index, value);
	}
	
	public float[] getSkyColor(int index) {
		float value[] = new float[3];
		getSkyColor(index, value);
		return value;
	}
	
	public void removeSkyColor(int index) {
		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		skyColor.removeValue(index);
	}

	////////////////////////////////////////////////
	// groundAngle
	////////////////////////////////////////////////

	public void addGroundAngle(float value) {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		groundAngle.addValue(value);
	}
	
	public int getNGroundAngles() {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		return groundAngle.getSize();
	}
	
	public void setGroundAngle(int index, float value) {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		groundAngle.set1Value(index, value);
	}

	public void setGroundAngles(String value) {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		groundAngle.setValues(value);
	}
	
	public void setGroundAngles(String value[]) {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		groundAngle.setValues(value);
	}
	
	public float getGroundAngle(int index) {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		return groundAngle.get1Value(index);
	}
	
	public void removeGroundAngle(int index) {
		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		groundAngle.removeValue(index);
	}

	////////////////////////////////////////////////
	// skyAngle
	////////////////////////////////////////////////

	public void addSkyAngle(float value) {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		skyAngle.addValue(value);
	}
	
	public int getNSkyAngles() {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		return skyAngle.getSize();
	}
	
	public void setSkyAngle(int index, float value) {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		skyAngle.set1Value(index, value);
	}
	
	public void setSkyAngles(String value) {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		skyAngle.setValues(value);
	}
	
	public void setSkyAngles(String value[]) {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		skyAngle.setValues(value);
	}
	
	public float getSkyAngle(int index) {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		return skyAngle.get1Value(index);
	}
	
	public void removeSkyAngle(int index) {
		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		skyAngle.removeValue(index);
	}

	////////////////////////////////////////////////
	// frontURL
	////////////////////////////////////////////////

	public void addFrontURL(String value) {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		frontURL.addValue(value);
	}
	
	public int getNFrontURLs() {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		return frontURL.getSize();
	}
	
	public void setFrontURL(int index, String value) {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		frontURL.setValues(value);
	}
	
	public void setFrontURLs(String value) {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		frontURL.setValues(value);
	}
	
	public void setFrontURLs(String value[]) {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		frontURL.setValues(value);
	}
	
	public String getFrontURL(int index) {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		return frontURL.get1Value(index);
	}
	
	public void removeFrontURL(int index) {
		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		frontURL.removeValue(index);
	}

	////////////////////////////////////////////////
	// backURL
	////////////////////////////////////////////////

	public void addBackURL(String value) {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		backURL.addValue(value);
	}
	
	public int getNBackURLs() {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		return backURL.getSize();
	}
	
	public void setBackURL(int index, String value) {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		backURL.set1Value(index, value);
	}
	
	public void setBackURLs(String value) {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		backURL.setValues(value);
	}
	
	public void setBackURLs(String value[]) {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		backURL.setValues(value);
	}
	
	public String getBackURL(int index) {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		return backURL.get1Value(index);
	}
	
	public void removeBackURL(int index) {
		MFString backURL = (MFString)getExposedField(backURLFieldName);
		backURL.removeValue(index);
	}

	////////////////////////////////////////////////
	// leftURL
	////////////////////////////////////////////////

	public void addLeftURL(String value) {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		leftURL.addValue(value);
	}
	
	public int getNLeftURLs() {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		return leftURL.getSize();
	}
	
	public void setLeftURL(int index, String value) {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		leftURL.set1Value(index, value);
	}
	
	public void setLeftURLs(String value) {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		leftURL.setValues(value);
	}

	public void setLeftURLs(String value[]) {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		leftURL.setValues(value);
	}
	
	public String getLeftURL(int index) {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		return leftURL.get1Value(index);
	}
	
	public void removeLeftURL(int index) {
		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		leftURL.removeValue(index);
	}

	////////////////////////////////////////////////
	// rightURL
	////////////////////////////////////////////////

	public void addRightURL(String value) {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		rightURL.addValue(value);
	}
	
	public int getNRightURLs() {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		return rightURL.getSize();
	}
	
	public void setRightURL(int index, String value) {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		rightURL.set1Value(index, value);
	}

	public void setRightURLs(String value) {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		rightURL.setValues(value);
	}
	
	public void setRightURLs(String value[]) {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		rightURL.setValues(value);
	}
	
	public String getRightURL(int index) {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		return rightURL.get1Value(index);
	}
	
	public void removeRightURL(int index) {
		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		rightURL.removeValue(index);
	}

	////////////////////////////////////////////////
	// topURL
	////////////////////////////////////////////////

	public void addTopURL(String value) {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		topURL.addValue(value);
	}
	
	public int getNTopURLs() {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		return topURL.getSize();
	}
	
	public void setTopURL(int index, String value) {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		topURL.set1Value(index, value);
	}

	public void setTopURLs(String value) {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		topURL.setValues(value);
	}
	
	public void setTopURLs(String value[]) {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		topURL.setValues(value);
	}
	
	public String getTopURL(int index) {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		return topURL.get1Value(index);
	}
	
	public void removeTopURL(int index) {
		MFString topURL = (MFString)getExposedField(topURLFieldName);
		topURL.removeValue(index);
	}

	////////////////////////////////////////////////
	// bottomURL
	////////////////////////////////////////////////

	public void addBottomURL(String value) {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		bottomURL.addValue(value);
	}
	
	public int getNBottomURLs() {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		return bottomURL.getSize();
	}
	
	public void setBottomURL(int index, String value) {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		bottomURL.set1Value(index, value);
	}

	public void setBottomURLs(String value) {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		bottomURL.setValues(value);
	}
	
	public void setBottomURLs(String value[]) {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		bottomURL.setValues(value);
	}
	
	public String getBottomURL(int index) {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		return bottomURL.get1Value(index);
	}
	
	public void removeBottomURL(int index) {
		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		bottomURL.removeValue(index);
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

		MFColor groundColor = (MFColor)getExposedField(groundColorFieldName);
		printStream.println(indentString + "\t" + "groundColor [");
		groundColor.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFColor skyColor = (MFColor)getExposedField(skyColorFieldName);
		printStream.println(indentString + "\t" + "skyColor [");
		skyColor.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");


		MFFloat groundAngle = (MFFloat)getExposedField(groundAngleFieldName);
		printStream.println(indentString + "\t" + "groundAngle [");
		groundAngle.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFFloat skyAngle = (MFFloat)getExposedField(skyAngleFieldName);
		printStream.println(indentString + "\t" + "skyAngle [");
		skyAngle.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");


		MFString frontURL = (MFString)getExposedField(frontURLFieldName);
		printStream.println(indentString + "\t" + "frontURL [");
		frontURL.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFString backURL = (MFString)getExposedField(backURLFieldName);
		printStream.println(indentString + "\t" + "backURL [");
		backURL.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFString leftURL = (MFString)getExposedField(leftURLFieldName);
		printStream.println(indentString + "\t" + "leftURL [");
		leftURL.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFString rightURL = (MFString)getExposedField(rightURLFieldName);
		printStream.println(indentString + "\t" + "rightURL [");
		rightURL.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFString topURL = (MFString)getExposedField(topURLFieldName);
		printStream.println(indentString + "\t" + "topURL [");
		topURL.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");

		MFString bottomURL = (MFString)getExposedField(bottomURLFieldName);
		printStream.println(indentString + "\t" + "bottomURL [");
		bottomURL.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");
	}
}
