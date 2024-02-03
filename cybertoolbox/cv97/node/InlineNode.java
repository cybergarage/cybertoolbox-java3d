/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : Inline.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import java.net.*;

import cv97.*;
import cv97.field.*;
import cv97.util.*;

public class InlineNode extends GroupingNode {

	private String	urlFieldName				= "url";
	
	public InlineNode() {
		super(false);
		setHeaderFlag(false);
		setType(inlineTypeName);

		// url exposed field
		MFString url = new MFString();
		addExposedField(urlFieldName, url);
	}

	public InlineNode(InlineNode node) {
		this();
		setFieldValues(node);
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
	//	abstract functions
	////////////////////////////////////////////////

	public boolean loadURL() {
	
		Debug.message("InlineNode::loadURL");
		
		removeChildNodes();
		
		if (getNURLs() <= 0)
			return false;
			
		String urlName = getURL(0);
		
		if (urlName == null)
			return true;
			
		SceneGraph sgLoad = new SceneGraph();
		
		if (sgLoad.load(urlName) == false) {
			SceneGraph sg = getSceneGraph();
			if (sg == null)
				return false;
			URL baseURL = sg.getBaseURL();
			if (baseURL != null) {
				try {
					if (sgLoad.load(new URL(baseURL.toString() + urlName)) == false) {
						Debug.message("\tLoading is Failed !!");
						return false;
					}
				} catch (MalformedURLException mue) {
					return false;
				}
			}
		}
		
		Debug.message("\tLoading is OK !!");
		
		sgLoad.initialize();
		
		setBoundingBoxCenter(sgLoad.getBoundingBoxCenter());
		setBoundingBoxSize(sgLoad.getBoundingBoxSize());
		
		Node sgNode = sgLoad.getNodes();
		while (sgNode != null) {
			sgNode.remove();
			addChildNode(sgNode);			
			sgNode = sgLoad.getNodes();
		}
		
		return true;
	}

	////////////////////////////////////////////////
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		MFString url = (MFString)getExposedField(urlFieldName);
		printStream.println(indentString + "\t" + "url [");
		url.outputContext(printStream, indentString + "\t\t");
		printStream.println(indentString + "\t" + "]");
	}
}
