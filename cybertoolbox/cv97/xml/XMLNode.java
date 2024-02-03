/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-2000
*
*	File : XMLNode.java
*
******************************************************************/

package cv97.xml;

import java.io.PrintWriter;

import cv97.*;
import cv97.node.*;

public class XMLNode extends Node {

	public XMLNode() 
	{
		super();

		setHeaderFlag(false);
		setType(genericXMLNodeTypeName);
	}

	////////////////////////////////////////////////
	//	Field
	////////////////////////////////////////////////

	public final XMLElement getElement(String eleName) 
	{
		return (XMLElement)getField(eleName);
	}
	
	public final int getNElements() 
	{
		return getNFields();
	}

	public final void addElement(XMLElement ele) 
	{
		addField(ele);
	}

	public final void addElement(String name, XMLElement ele) 
	{
		addField(name, ele);
	}

	public final void addElement(String name, String value) 
	{
		XMLElement ele = new XMLElement(value);
		addField(name, ele);
	}

	public final XMLElement getElement(int index) 
	{
		return (XMLElement)getField(index);
	}

	public final boolean removeElement(XMLElement ele) 
	{
		return removeField(ele);
	}

	public final boolean removeElement(String eleName) 
	{
		return removeField(eleName);
	}

	public int getElementNumber(XMLElement ele) 
	{
		return getFieldNumber(ele);
	}
	
	////////////////////////////////////////////////
	//	Output
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) 
	{
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node)
	{
		return false;
	}

	public void initialize() 
	{
	}

	public void uninitialize() 
	{
	}

	public void update() 
	{
	}
}
