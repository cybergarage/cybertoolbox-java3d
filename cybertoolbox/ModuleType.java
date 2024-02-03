/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	Module.java
*
******************************************************************/

import java.io.*;
import java.awt.*;

import cv97.*;
import cv97.field.*;
import cv97.util.*;

////////////////////////////////////////////////////////////
//	Module Type File Format
////////////////////////////////////////////////////////////

/**********************************************************

  =====================================

  #CTB Module. V3.0

  CLASSNAME		className
  MODULENAME	moduleName
  ATTRIBUTE		(SYSTEM | USER)

  EVENTIN0		filedName
  EVENTIN1		filedName
  EVENTIN2		filedName
  EVENTIN3		filedName

  EVENTOUT0		filedName
  EVENTOUTl		filedName
  EVENTOUT2		filedName
  EVENTOUT3		filedName

  EXECUTIONNODE	(TURE | FALSE)

  =====================================

**********************************************************/

public class ModuleType extends LinkedListNode implements ModuleTypeConstants, cv97.Constants {

	public static final String	FILE_CLASSNAME_STRING		= "CLASSNAME";
	public static final String	FILE_TYPENAME_STRING			= "TYPENAME";
	public static final String	FILE_ATTRIBUTE_STRING		= "ATTRIBUTE";
	public static final String	FILE_SCRIPTNAME_STRING		= "SCRIPTNAME";
	public static final String	FILE_ICONNAME_STRING			= "ICONNAME";
	public static final String	FILE_EVENTIN_STRING			= "EVENTIN";
	public static final String	FILE_EVENTOUT_STRING			= "EVENTOUT";
	public static final String	FILE_EXECUTIONNODE_STRING	= "EXECUTIONNODE";

	public static final String	SYSTEM_MODULETYPE_CLASSNAME_STRING	= "SYSTEM";

	private String		mClassName;
	private String		mName;
	private String		mInNodeName[]	= new String[Module.NODE_MAXNUM];
	private String		mOutNodeName[]	= new String[Module.NODE_MAXNUM];
	private boolean	mExecutionNode;
	private int			mAttribure;
	
	public ModuleType() {
		setHeaderFlag(false);
		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			setInNodeName(n, null);
			setOutNodeName(n, null);
		}
		setExecutionNode(false);
		setWorld(null);
	}

	public ModuleType(String className, String name, boolean hasExecutionNode, int attribute) {
		this();
		setClassName(className);
		setName(name);
		setExecutionNode(hasExecutionNode);
		setAttribute(attribute);
	}

	public ModuleType(String className, String name, int inNodeCount, int outNodeCount, boolean hasExecutionNode, int attribute) {
		this(className, name, hasExecutionNode, attribute);
		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			if (n < inNodeCount)
				setInNodeName(n, "InNode" + n);
			else
				setInNodeName(n, null);
			
			if (n < outNodeCount)
				setOutNodeName(n, "OutNode" + n);
			else
				setOutNodeName(n, null);
		}
	}

	public ModuleType(String className, String name, String inNodeName[], String outNodeName[], boolean hasExecutionNode, int attribute) {
		this(className, name, hasExecutionNode, attribute);
		for (int n=0; n<inNodeName.length; n++) 
			setInNodeName(n, inNodeName[n]);
		for (int n=0; n<outNodeName.length; n++) 
			setOutNodeName(n, outNodeName[n]);
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
	
	public boolean isAppletMode() {
		return mWorld.isAppletMode();
	}
	
	////////////////////////////////////////
	//	Class Name
	////////////////////////////////////////

	public void setClassName(String className) {
		mClassName = className;
	}

	public String getClassName() {
		return mClassName;
	}

	public boolean isSystemModuleType() {
		return mClassName.equalsIgnoreCase(SYSTEM_MODULETYPE_CLASSNAME_STRING);
	}
	
	////////////////////////////////////////
	//	 Name
	////////////////////////////////////////

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	////////////////////////////////////////
	//	 Attribure
	////////////////////////////////////////

	public void setAttribute(String attrString) {
		if (attrString.equalsIgnoreCase(Module.ATTRIBUTE_SYSTEM_STRING) == true)
			setAttribute(Module.ATTRIBUTE_SYSTEM);
		else if (attrString.equalsIgnoreCase(Module.ATTRIBUTE_DEFAULT_STRING) == true)
			setAttribute(Module.ATTRIBUTE_DEFAULT);
		else if (attrString.equalsIgnoreCase(Module.ATTRIBUTE_USER_STRING) == true)
			setAttribute(Module.ATTRIBUTE_USER);
		else {
			Debug.message("ModuleType.setAttribute");
			Debug.message("\tInvalidate attrString = " + attrString);
			setAttribute(Module.ATTRIBUTE_DEFAULT);
		}
	}
	
	public void setAttribute(int attr) {
		mAttribure = attr;
	}

	public String getAttributeString() {
		if (getAttribute() == Module.ATTRIBUTE_SYSTEM)
			return Module.ATTRIBUTE_SYSTEM_STRING;
		else if (getAttribute() == Module.ATTRIBUTE_DEFAULT)
			return Module.ATTRIBUTE_DEFAULT_STRING;
		if (getAttribute() == Module.ATTRIBUTE_USER)
			return Module.ATTRIBUTE_USER_STRING;
		return Module.ATTRIBUTE_DEFAULT_STRING;
	}
	
	public int getAttribute() {
		return mAttribure;
	}

	////////////////////////////////////////
	//	Icon Name
	////////////////////////////////////////

	private Image		mIconImage = null;
	
	public String getIconFileName() {
		return getClassName() + getName() + ".gif";
	}

	public Image getIcon() {
		if (isAppletMode() == true)
			return null;
		if (mIconImage == null) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			String fileSep = System.getProperty("file.separator");
			String imageFilename = World.getModuleTypeIconDirectory() + getIconFileName();
			mIconImage = toolkit.getImage(imageFilename);
		}
		return mIconImage;
	}

	////////////////////////////////////////
	//	Class Name
	////////////////////////////////////////

	public String getModuleClassName() {
		return getClassName() + getName();
	}

	////////////////////////////////////////
	//	InNode Name
	////////////////////////////////////////

	public void setInNodeName(int n, String name) {
		mInNodeName[n] = name;
	}
	public String getInNodeName(int n) {
		return mInNodeName[n];
	}

	public int getNInNodes() {
		int nInNode = 0;
		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			if (getInNodeName(n) != null)
				nInNode++;
		}
		return nInNode;
	}
	
	public boolean hasInNodes() {
		return (0 < getNInNodes()) ? true : false;
	}

	////////////////////////////////////////
	//	OutNode Name
	////////////////////////////////////////

	public void setOutNodeName(int n, String name) {
		mOutNodeName[n] = name;
	}
	public String getOutNodeName(int n) {
		return mOutNodeName[n];
	}

	public int getNOutNodes() {
		int nOutNode = 0;
		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			if (getOutNodeName(n) != null)
				nOutNode++;
		}
		return nOutNode;
	}
	
	public boolean hasOutNodes() {
		return (0 < getNOutNodes()) ? true : false;
	}
	
	////////////////////////////////////////
	//	Execution Node
	////////////////////////////////////////

	public void setExecutionNode(boolean bvalue) {
		mExecutionNode = bvalue;
	}

	public boolean hasExecutionNode() {
		return mExecutionNode;
	}

	////////////////////////////////////////
	//	File
	////////////////////////////////////////

	public boolean load(String filename) {

		try {
			ModuleTypeFile moduleFile = new ModuleTypeFile(filename);

			moduleFile.findString(FILE_CLASSNAME_STRING);		String className	= moduleFile.nextString();
			moduleFile.findString(FILE_TYPENAME_STRING);			String moduleName	= moduleFile.nextString();
			moduleFile.findString(FILE_ATTRIBUTE_STRING);		String attribute	= moduleFile.nextString();

			setClassName(className);
			setName(moduleName);
			setAttribute(attribute);

			for (int n=0; n<Module.NODE_MAXNUM; n++) {
				moduleFile.findString(new String(FILE_EVENTIN_STRING + n));
				String fieldName = moduleFile.nextString();
				if (fieldName.equals("NULL") == false) 
					setInNodeName(n, fieldName);
				else
					setInNodeName(n, null);
			}

			for (int n=0; n<Module.NODE_MAXNUM; n++) {
				moduleFile.findString(new String(FILE_EVENTOUT_STRING + n));
				String fieldName = moduleFile.nextString();
				if (fieldName.equals("NULL") == false) 
					setOutNodeName(n, fieldName);
				else
					setOutNodeName(n, null);
			}

			moduleFile.findString(FILE_EXECUTIONNODE_STRING); String executionNode = moduleFile.nextString();
			setExecutionNode(executionNode.equals("TRUE"));
		}
		catch (Exception e) {
			Debug.warning(e.toString() + " in ModuleType::load");
			Debug.warning("    filaname = " + filename);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean save(String filename) {

		try {
			FileOutputStream out = new FileOutputStream(filename);
			
			PrintWriter ps = new PrintWriter(out);

			ps.println("#CTB MODULETYPE V3.0");
			
			ps.print(toString());
			
			ps.close();
			out.close();
		}
		catch (Exception e) {
			Debug.warning(e.toString() + " in ModuleType::save");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}


	////////////////////////////////////////
	//	set
	////////////////////////////////////////

	public void set(String className, String moduleName, String attribute, String eventIn[], String eventOut[], boolean executionNode) {
		Debug.message("ModuleType.set");
		Debug.message("\t" + className +", " + moduleName  +", " + attribute);
		setClassName(className);
		setName(moduleName);
		setAttribute(attribute);

		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			String fieldName = eventIn[n];
			if (fieldName.equals("NULL") == false) 
				setInNodeName(n, fieldName);
			else
				setInNodeName(n, null);
		}

		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			String fieldName = eventOut[n];
			if (fieldName.equals("NULL") == false) 
				setOutNodeName(n, fieldName);
			else
				setOutNodeName(n, null);
		}

		setExecutionNode(executionNode);
	}

	////////////////////////////////////////
	//	List
	////////////////////////////////////////

	public ModuleType next(String className) {
		for (ModuleType cmType=next(); cmType != null; cmType=cmType.next()) {
			if (className.equalsIgnoreCase(cmType.getClassName()) == true) 
				return cmType;
		}
		return null;
	}
	
	public ModuleType next()	{
		return (ModuleType)getNextNode();
	}

	////////////////////////////////////////
	// createModule
	////////////////////////////////////////

	public Module createModule() {
		String className = getClassName() + getName();

		Module module = null;
		
		if (isAppletMode() == true) {
			try {
				module = ModuleClassCreator.createModule(this);
			}
			catch (NoClassDefFoundError ncdfe) {
				Debug.warning("java.lang.NoClassDefFoundError: " + getClassName() + getName());
				return null;
			}
		}
		else {
			ModuleClassLoader classLoader =  new ModuleClassLoader();
				
			Class moduleClass = null;
			
			try {
				moduleClass = classLoader.loadClass(className);
			}
			catch (ClassNotFoundException cnfe1) {
				Debug.warning("Module.createModule : ClassNotFoundException " + className);
				return null;
			}

			if (moduleClass == null) 
				return null;
		
			try {			 
				module = (Module)moduleClass.newInstance();
			}
			catch (InstantiationException e) {
				Debug.warning("ModuleType.createModule : InstantiationException " + className);
				return null;
			}
			catch (IllegalAccessException e) {
				Debug.warning("ModuleType.createModule : IllegalAccessException " + className);
				return null;
			}
		}
		
		if (module != null)
			module.setModuleType(this);
		
		if (module == null)
			Debug.warning("ModuleType.createModule : Couldn't create module (" + className + ")");
			
		return module;
	}
	
	////////////////////////////////////////
	//	toString
	////////////////////////////////////////
	
	public String endl() {
		return "\n";
	}
	
	public String toString() {
		StringBuffer info = new StringBuffer();
		info.append(FILE_CLASSNAME_STRING	+ " " + getClassName() + endl());		
		info.append(FILE_TYPENAME_STRING		+ " " + getName() + endl());	
	
		info.append(FILE_ATTRIBUTE_STRING + " " + getAttributeString() + endl());
					
		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			String fieldName		= getInNodeName(n);
			info.append(FILE_EVENTIN_STRING + n + " ");
			info.append((fieldName != null ? fieldName : "NULL") + endl());
		}

		for (int n=0; n<Module.NODE_MAXNUM; n++) {
			String fieldName		= getOutNodeName(n);
			info.append(FILE_EVENTOUT_STRING + n + " ");
			info.append((fieldName != null ? fieldName : "NULL") + endl());
		}

		info.append(FILE_EXECUTIONNODE_STRING + " " + (hasExecutionNode() ? "TRUE" : "FALSE") + endl()); 

		return info.toString();		
	}
};

