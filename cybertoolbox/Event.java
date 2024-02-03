/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1998
*
*	File:	Event.java
*
******************************************************************/

import java.io.*;
import java.util.*;

import cv97.SceneGraph;

public class Event extends LinkedListNode {
	
	public Event() {
		setHeaderFlag(false);
		setWorld(null);
		setEventType(null);
		setName(null);
		setAttribute(EventType.ATTRIBUTE_NONE);
		setOptionString(null); 
		setData(null);
	}
	
	public Event(EventType eventType, String optionString) {
		this();
		
		Debug.message("Event.<init>");
		Debug.message("\teventType    " + eventType); 
		Debug.message("\toptionString " + optionString); 
		
		setEventType(eventType);
		setOptionString(optionString); 
	}

	////////////////////////////////////////
	//	World / SceneGraph
	////////////////////////////////////////

	private World mWorld;
			
	public void setWorld(World world) {
		mWorld = world;
	}
	
	public World getWorld() {
		return mWorld;
	}
	
	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	//////////////////////////////////////////////////
	// Event		
	//////////////////////////////////////////////////
	
	private EventType mEventType = null;
	
	public void setEventType(EventType eventType) {
		mEventType = eventType;
	}
	
	public EventType getEventType() {
		return mEventType;
	}

	//////////////////////////////////////////////////
	// EventType Number
	//////////////////////////////////////////////////

	public int getEventTypeNumber() {
		return getWorld().getEventTypeNumber(getEventType());
	}
	
	//////////////////////////////////////////////////
	// Name
	//////////////////////////////////////////////////
	
	private String mName;
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		EventType eventType = getEventType();
		if (eventType != null)
			return eventType.getName();
		return mName;
	}

	//////////////////////////////////////////////////
	// Attribute
	//////////////////////////////////////////////////
	
	private int mAttribure;

	public void setAttribute(int attr) {
		mAttribure = attr;
	}

	public void setAttribute(String attributeString) {
		setAttribute((attributeString.equalsIgnoreCase(EventType.ATTRIBUTE_SYSTEM_STRING) == true) ? EventType.ATTRIBUTE_SYSTEM : EventType.ATTRIBUTE_USER);
	}
	
	public int getAttribute() {
		EventType eventType = getEventType();
		if (eventType != null)
			return getEventType().getAttribute();
		return mAttribure;
	}

	public String getAttributeString() {
		EventType eventType = getEventType();
		if (eventType != null)
			return getEventType().getAttributeString();
		return null;
	}
	
	//////////////////////////////////////////////////
	// OptionString
	//////////////////////////////////////////////////

	private String mOptionString = null;
	
	public void setOptionString(String name) {
		mOptionString = name;
	}

	public String getOptionString() {
		return mOptionString;
	}

	public double getOptionValue() throws NumberFormatException {
		return Double.parseDouble(mOptionString);
	}

	public String []getOptionStrings() {
		String optionString = getOptionString();
		if (optionString == null)
			return null;
		if (optionString.length() <= 0)
			return null;
		StringValueTokenizer tokenizer = new StringValueTokenizer(optionString);
		return tokenizer.getValues();
	}
	
	public double []getOptionValues() {
		String optionString = getOptionString();
		if (optionString == null)
			return null;
		if (optionString.length() <= 0)
			return null;
		DoubleValueTokenizer tokenizer = new DoubleValueTokenizer(optionString);
		return tokenizer.getValues();
	}

	//////////////////////////////////////////////////
	//  System Module
	//////////////////////////////////////////////////
	
	public boolean hasSystemtModule() {
		Debug.message("Event.hasSystemtModule");
		EventType eventType = getEventType();
		String moduleClassName = eventType.getModuleClassName();
		String moduleName = eventType.getModuleName();
		Debug.message("\tmoduleClassName = " + moduleClassName);
		Debug.message("\tmoduleName      = " + moduleName);
		if (moduleClassName == null)
			return false;
		if (moduleClassName.length() <= 0)
			return false; 
		if (moduleName == null)
			return false;
		if (moduleName.length() <= 0)
			return false; 
		return true;
	}

	public ModuleType getSystemModuleType() {
		if (getWorld() == null)
			return null;
		EventType eventType = getEventType();
		String moduleClassName = eventType.getModuleClassName();
		String moduleName = eventType.getModuleName();
		return getWorld().getModuleType(moduleClassName, moduleName);
	}
		
	//////////////////////////////////////////////////
	// Diagram
	//////////////////////////////////////////////////

	private LinkedList mDiagramList = new LinkedList();

	public void	addDiagram(Diagram dgm) {
		dgm.setEvent(this);
		mDiagramList.addNode(dgm);
	}
	
	public int getNDiagrams() {
		return mDiagramList.getNNodes();
	}
	
	public Diagram getDiagrams() {
		return (Diagram)mDiagramList.getNodes();
	}
	
	public Diagram getDiagram(int n) {
		return (Diagram)mDiagramList.getNode(n);
	}

	public Diagram getDiagram(String name) {
		int nDiagrams = getNDiagrams();
		for (int n=0; n<nDiagrams; n++) {
			Diagram dgm = getDiagram(n);
			if (name.equals(dgm.getName()) == true)
				return dgm;
		}
		return null;
	}

	public void removeDiagram(Diagram dgm) {
		dgm.remove();
	}

	public void removeDiagram(int n) {
		removeDiagram(getDiagram(n));
	}

	public void removeAllDiagrams() {
		Diagram dgm = getDiagrams();
		while (dgm != null) {
			Diagram nextDiagram = dgm.next();
			removeDiagram(dgm);
			dgm = nextDiagram;
		}
	}

	//////////////////////////////////////////////////
	// Execute Behavior
	//////////////////////////////////////////////////

	public void run() {
		int nDiagrams = getNDiagrams();
		for (int n=0; n<nDiagrams; n++)
			getDiagram(n).run();
	}
		
	public void run(String sysModuleParams[]) {
		int nDiagrams = getNDiagrams();
		for (int n=0; n<nDiagrams; n++)
			getDiagram(n).run(sysModuleParams);
	}
	
	public void run(String sysModuleParam) {
		int nDiagrams = getNDiagrams();
		for (int n=0; n<nDiagrams; n++)
			getDiagram(n).run(sysModuleParam);
	}
	
	////////////////////////////////////////////////
	//	next node list
	////////////////////////////////////////////////

	public Event next () {
		return (Event)getNextNode();
	}

	//////////////////////////////////////////////////
	// Data
	//////////////////////////////////////////////////

	private Object mData = null;
	
	public void setData(Object data) {
		mData = data;
	}

	public Object getData() {
		return mData;
	}

	//////////////////////////////////////////////////
	// initialize / shutdown
	//////////////////////////////////////////////////
	
	public void initialize() {
		for (Diagram dgm = getDiagrams(); dgm != null; dgm = dgm.next())
			dgm.initialize();
	}

	public void shutdown() {
		for (Diagram dgm = getDiagrams(); dgm != null; dgm = dgm.next())
			dgm.shutdown();
	}

	//////////////////////////////////////////////////
	// toString
	//////////////////////////////////////////////////
	
	public String toString() {
		if (getOptionString() == null)
			return getName();
		return getName() + " (" + getOptionString() + ")";
	}

}
