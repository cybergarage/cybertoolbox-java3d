/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	Module.java
*
******************************************************************/

import java.util.Vector;
import java.awt.Image;

import cv97.SceneGraph;

public abstract class Module extends LinkedListNode {

	public static final String	FILE_EXTENTION					= "mod";
	public static final String	ICON_FILE_EXTENTION			= "ico";

	public static final int ATTRIBUTE_SYSTEM					= 0x00;
	public static final int ATTRIBUTE_DEFAULT					= 0x01;
	public static final int ATTRIBUTE_USER						= 0x02;

	public static final String	ATTRIBUTE_SYSTEM_STRING	= "SYSTEM";
	public static final String	ATTRIBUTE_DEFAULT_STRING	= "DEFAULT";
	public static final String	ATTRIBUTE_USER_STRING		= "USER";

	public static final String	SYSTEM_INSIDEDIAGRAM_CLASSNAME	= "MISC";
	public static final String	SYSTEM_INSIDEDIAGRAM_TYPENAME		= "INSIDEDIAGRAM";
	
	public static final String	INPUTMODULE_CLASSNAME		= "SYSTEM";
	public static final String	OUTPUTMODULE_CLASSNAME		= "SYSTEM";
	public static final String	INPUTMODULE_TYPENAME		= "INPUT";
	public static final String	OUTPUTMODULE_TYPENAME		= "OUTPUT";

	public static final int NODE_MAXNUM				= 4;
	
	public static final int SIZE						= 32;
	public static final int NODE_SIZE				= 5;
	public static final int NODE_SPACING			= ((SIZE - NODE_SIZE * NODE_MAXNUM) / (NODE_MAXNUM+1));

	public static final int EXECUTIONNODE_NUMBER	= NODE_MAXNUM;

	public static final int OUTSIDE					= 0x000;
	public static final int INSIDE					= 0x100;
	public static final int INSIDE_INNODE			= 0x200;	
	public static final int INSIDE_OUTNODE			= 0x400;
	public static final int INSIDE_EXECUTIONNODE	= 0x800;

	public static final String NONE_VALUE			= "NONE";
	public static final int NODE_NUMBER_MASK		= 0xff;

	////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////
	
	public Module() {
		setHeaderFlag(false);
		setDiagram(null);
		setClassName(null);
		setTypeName(null);
		setName(null);
		setPosition(0, 0);
		setValue((String)null);
		setExecutionNode(null);
		setData(null);
		setInsideDiagram(null);
	}

	public Module(ModuleType moduleType) {
		this();
		setModuleType(moduleType);
	}

	////////////////////////////////////////
	//	ModuleType
	////////////////////////////////////////
	
	public void setModuleType(ModuleType moduleType) {
		setClassName(moduleType.getClassName());
		setTypeName(moduleType.getName());
		setAttribute(moduleType.getAttribute());
		setIcon(moduleType.getIcon());

		clearInNodes();		
		for (int n=0; n<moduleType.getNInNodes(); n++)
			addInNode(moduleType.getInNodeName(n));
		
		clearOutNodes();		
		for (int n=0; n<moduleType.getNOutNodes(); n++)
			addOutNode(moduleType.getOutNodeName(n));
		
		if (moduleType.hasExecutionNode() == true) 
			enableExecutionNode();
		else
			disableExecutionNode();
	}

	public ModuleType getModuleType() {
		return getWorld().getModuleType(getClassName(), getTypeName());
	}

	public boolean isSystemModule() {
		return getModuleType().isSystemModuleType();
	}
	
	////////////////////////////////////////
	//	World / SceneGraph
	////////////////////////////////////////

	public World getWorld() {
		return getDiagram().getWorld();
	}
	
	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	////////////////////////////////////////
	//	Diagram
	////////////////////////////////////////

	private Diagram mDiagram;
			
	public void setDiagram(Diagram dgm) {
		mDiagram = dgm;
	}
	
	public Diagram getDiagram() {
		return mDiagram;
	}

	////////////////////////////////////////
	//	Inside Diagram
	////////////////////////////////////////

	private Diagram mInsideDiagram;
			
	public void setInsideDiagram(Diagram dgm) {
		mInsideDiagram = dgm;
	}
	
	private ModuleType getSystemInputModuleType() {
		return getWorld().getModuleType(OUTPUTMODULE_CLASSNAME, INPUTMODULE_TYPENAME);
	}
	
	private ModuleType getSystemOutputModuleType() {
		return getWorld().getModuleType(INPUTMODULE_CLASSNAME, OUTPUTMODULE_TYPENAME);
	}

	private Diagram createInsideDiagram() {
		Diagram dgm = new Diagram();

		dgm.setName("InsideDiagram");
		dgm.setParentModule(this);

		Module inModule = getSystemInputModuleType().createModule();
		inModule.setPosition(0, 0);
		dgm.addModule(inModule);
		
		Module outModule = getSystemOutputModuleType().createModule();
		outModule.setPosition(320 - outModule.getSize(), 0);
		dgm.addModule(outModule);
		
		return dgm;
	}
	
	public Diagram getInsideDiagram() {
		if (isInsideDiagram() == false)
			return null;
		
		if (mInsideDiagram == null)
			mInsideDiagram = createInsideDiagram();
		
		return mInsideDiagram;
	}
	
	public boolean hasInsideDiagram() {
		return (getInsideDiagram() != null ? true : false);
	}
	
	public boolean isInsideDiagram() {
		String className = getClassName(); 
		if (className.equalsIgnoreCase(SYSTEM_INSIDEDIAGRAM_CLASSNAME) == false)
			return false;
		String typeName = getTypeName(); 
		if (typeName.equalsIgnoreCase(SYSTEM_INSIDEDIAGRAM_TYPENAME) == false)
			return false;
		return true;
	}
	
	public Module getSystemOutputModule() {
		if (isInsideDiagram() == false)
			return null;
		return getInsideDiagram().getSystemModule(OUTPUTMODULE_TYPENAME);
	}

	public Module getSystemInputModule() {
		if (isInsideDiagram() == false)
			return null;
		return getInsideDiagram().getSystemModule(INPUTMODULE_TYPENAME);
	}
	
	////////////////////////////////////////
	//	Class Name
	////////////////////////////////////////

	private String mClassName = null;
	
	public void setClassName(String className) {
		mClassName = className;
	}

	public String getClassName() {
		return mClassName;
	}

	////////////////////////////////////////
	//	 Type Name
	////////////////////////////////////////

	private String mTypeName = null;
	
	public void setTypeName(String typeName) {
		mTypeName = typeName;
	}

	public String getTypeName() {
		return mTypeName;
	}

	////////////////////////////////////////
	//	 Attribure
	////////////////////////////////////////

	private int	mAttribure;

	public void setAttribute(int attr) {
		mAttribure = attr;
	}

	public int getAttribute() {
		return mAttribure;
	}
	
	////////////////////////////////////////////////
	//	X position
	////////////////////////////////////////////////
	
	private int mXPosition = 0;
	
	public void setXPosition(int x) {
		mXPosition = x;
	}
	public int getXPosition() {
		return mXPosition;
	}

	////////////////////////////////////////////////
	//	Y position
	////////////////////////////////////////////////
	
	private int mYPosition = 0;
	
	public void setYPosition(int y) {
		mYPosition = y;
	}
	public int getYPosition() {
		return mYPosition;
	}

	////////////////////////////////////////////////
	//	Position
	////////////////////////////////////////////////

	public void setPosition(int x, int y) {
		setXPosition(x);
		setYPosition(y);
	}

	////////////////////////////////////////////////
	//	Name
	////////////////////////////////////////////////

	private String mName = null;
	
	public void setName(String value) {
		mName = value;
	}
	public String getName() {
		return mName;
	}
	
	public void setNumber(int n) {
		setName("module" + Integer.toString(n));
	}
	
	public int getNumber() {
		String name = getName();
		int index = name.lastIndexOf('e'); 
		String valueString = new String(name.toCharArray(), (index + 1), name.length() - (index + 1));
		Integer value = new Integer(valueString);
		return value.intValue();
	}
	
	////////////////////////////////////////////////
	//	Value (set*)
	////////////////////////////////////////////////

	private String mValue = null;
	
	public void setValue(String value) {
		mValue = value;
	}
	
	public void setValue(int value) {
		setValue(Integer.toString(value));
	}

	public void setValue(float value) {
		setValue(Float.toString(value));
	}

	public void setValue(double value) {
		setValue(Double.toString(value));
	}

	public void setValue(boolean value) {
		Boolean bool = new Boolean(value);
		setValue(bool.toString());
	}

	public void setValue(String value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int n=0; n<value.length; n++) {
			valueBuffer.append(value[n]);
			if (n < (value.length-1))
				valueBuffer.append(',');
		}
		setValue(valueBuffer.toString());
	}
	
	public void setValue(int x, int y) {
		setValue(Integer.toString(x) + "," + Integer.toString(y));
	}

	public void setValue(int x, int y, int z) {
		setValue(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
	}

	public void setValue(int x, int y, int z, int angle) {
		setValue(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + "," + Integer.toString(angle));
	}

	public void setValue(int value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int n=0; n<value.length; n++) {
			valueBuffer.append(Integer.toString(value[n]));
			if (n < (value.length-1))
				valueBuffer.append(',');
		}
		setValue(valueBuffer.toString());
	}
	
	public void setValue(float x, float y) {
		setValue(Float.toString(x) + "," + Float.toString(y));
	}

	public void setValue(float x, float y, float z) {
		setValue(Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z));
	}

	public void setValue(float x, float y, float z, float angle) {
		setValue(Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z) + "," + Float.toString(angle));
	}

	public void setValue(float value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int n=0; n<value.length; n++) {
			valueBuffer.append(Float.toString(value[n]));
			if (n < (value.length-1))
				valueBuffer.append(',');
		}
		setValue(valueBuffer.toString());
	}
	
	public void setValue(double x, double y) {
		setValue(Double.toString(x) + "," + Double.toString(y));
	}

	public void setValue(double x, double y, double z) {
		setValue(Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z));
	}

	public void setValue(double x, double y, double z, double angle) {
		setValue(Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z) + "," + Double.toString(angle));
	}

	public void setValue(double value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int n=0; n<value.length; n++) {
			valueBuffer.append(Double.toString(value[n]));
			if (n < (value.length-1))
				valueBuffer.append(',');
		}
		setValue(valueBuffer.toString());
	}
	
	////////////////////////////////////////////////
	//	Value (get*)
	////////////////////////////////////////////////
	
	public String getValue() {
		return mValue;
	}

	public String getStringValue() {
		return mValue;
	}

	public int getIntegerValue() throws NumberFormatException {
		if (mValue == null)
			throw new NumberFormatException();	
		Integer value = new Integer(mValue);
		return value.intValue();
	}

	public float getFloatValue() throws NumberFormatException {
		if (mValue == null)
			throw new NumberFormatException();	
		Float value = new Float(mValue);
		return value.floatValue();
	}

	public double getDoubleValue() throws NumberFormatException {
		if (mValue == null)
			throw new NumberFormatException();	
		Double value = new Double(mValue);
		return value.doubleValue();
	}

	public boolean getBooleanValue() {
		if(mValue == null)
			return false;
		if (mValue.equalsIgnoreCase("true") == true)
			return true;
		return false;
	}

	public String []getStringValues() {
		if (mValue == null)
			return null;
		if (mValue.length() <= 0)
			return null;
		StringValueTokenizer tokenizer = new StringValueTokenizer(mValue);
		return tokenizer.getValues();
	}
	
	public double []getDoubleValues() {
		if (mValue == null)
			return null;
		if (mValue.length() <= 0)
			return null;
		DoubleValueTokenizer tokenizer = new DoubleValueTokenizer(mValue);
		return tokenizer.getValues();
	}

	public float []getFloatValues() {
		double doubleValues[] = getDoubleValues();
		if (doubleValues == null)
			return null;
		if (doubleValues.length <= 0)
			return null;
		float floatValues[] = new float[doubleValues.length];
		for (int n=0; n<doubleValues.length; n++)
			floatValues[n] = (float)doubleValues[n];
		return floatValues;
	}

	public int []getIntegerValues() {
		double doubleValues[] = getDoubleValues();
		if (doubleValues == null)
			return null;
		if (doubleValues.length <= 0)
			return null;
		int intValues[] = new int[doubleValues.length];
		for (int n=0; n<doubleValues.length; n++)
			intValues[n] = (int)doubleValues[n];
		return intValues;
	}

	////////////////////////////////////////////////
	//	Icon
	////////////////////////////////////////////////
	
	private Image mIconImage = null;

	public void setIcon(Image image) {
		mIconImage = image;
	}
	
	public Image getIcon() {
		return mIconImage;
	}

	////////////////////////////////////////////////
	//	Size
	////////////////////////////////////////////////

	public int getSize() { 
		return SIZE; 
	}

	////////////////////////////////////////////////
	//	In Node	
	////////////////////////////////////////////////

	private Vector mInNode = new Vector();
	
	public void clearInNodes() {
		mInNode.clear();
	}
	
	public void addInNode(ModuleNode node) {
		node.setNumber(getNInNodes());
		node.setModule(this);
		mInNode.add(node);
	}
	
	public void addInNode(String name) {
		ModuleNode node = new ModuleNode(name, ModuleNode.IN_NODE);
		addInNode(node);
	}
	
	public void addInNode(String name, String value) {
		ModuleNode node = new ModuleNode(name, ModuleNode.IN_NODE, value);
		addInNode(node);
	}
	
	public int getNInNodes() {
		return mInNode.size();
	}
	
	public ModuleNode getInNode(int n) {
		try {
			return (ModuleNode)mInNode.elementAt(n);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public ModuleNode getInNode(String name) {
		int nInNodes = getNInNodes();
		for (int n=0; n<nInNodes; n++) {
			String nodeName = getInNodeName(n);
			if (nodeName != null) {
				if (nodeName.equals(name) == true)
					return getInNode(n);
			}
		}
		return null;
	}

	public int getInNodeNumber(ModuleNode node) {
		int nInNodes = getNInNodes();
		for (int n=0; n<nInNodes; n++) {
			if (getInNode(n) == node)
				return n;
		}
		return -1;
	}

	public String getInNodeName(int n) {
		return getInNode(n).getName();
	}

	public void setInNodeValue(int n, String value) {
		getInNode(n).setValue(value);
	}

	public void setInNodeValue(int n, int value) {
		getInNode(n).setValue(Integer.toString(value));
	}

	public void setInNodeValue(int n, float value) {
		getInNode(n).setValue(Float.toString(value));
	}

	public void setInNodeValue(int n, double value) {
		getInNode(n).setValue(Double.toString(value));
	}

	public void setInNodeValue(int n, boolean value) {
		Boolean bool = new Boolean(value);
		getInNode(n).setValue(bool.toString());
	}

	public void setInNodeValue(int n, String value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(value[i]);
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setInNodeValue(n, valueBuffer.toString());
	}

	public void setInNodeValue(int n, int x, int y) {
		getInNode(n).setValue(Integer.toString(x) + "," + Integer.toString(y));
	}

	public void setInNodeValue(int n, int x, int y, int z) {
		getInNode(n).setValue(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
	}

	public void setInNodeValue(int n, int x, int y, int z, int angle) {
		getInNode(n).setValue(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + "," + Integer.toString(angle));
	}

	public void setInNodeValue(int n, int value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(Integer.toString(value[i]));
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setInNodeValue(n, valueBuffer.toString());
	}
	
	public void setInNodeValue(int n, float x, float y) {
		getInNode(n).setValue(Float.toString(x) + "," + Float.toString(y));
	}

	public void setInNodeValue(int n, float x, float y, float z) {
		getInNode(n).setValue(Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z));
	}

	public void setInNodeValue(int n, float x, float y, float z, float angle) {
		getInNode(n).setValue(Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z) + "," + Float.toString(angle));
	}

	public void setInNodeValue(int n, float value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(Float.toString(value[i]));
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setInNodeValue(n, valueBuffer.toString());
	}
	
	public void setInNodeValue(int n, double x, double y) {
		getInNode(n).setValue(Double.toString(x) + "," + Double.toString(y));
	}

	public void setInNodeValue(int n, double x, double y, double z) {
		getInNode(n).setValue(Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z));
	}

	public void setInNodeValue(int n, double x, double y, double z, double angle) {
		getInNode(n).setValue(Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z) + "," + Double.toString(angle));
	}
	
	public void setInNodeValue(int n, double value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(Double.toString(value[i]));
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setInNodeValue(n, valueBuffer.toString());
	}
	
	public String getInNodeValue(int n) {
		return getInNode(n).getValue();
	}

	public boolean hasInNode() {
		return (0 < getNInNodes()) ? true : false;
	}
	
	////////////////////////////////////////////////
	//	Out Node	
	////////////////////////////////////////////////

	private Vector mOutNode = new Vector();
	
	public void clearOutNodes() {
		mOutNode.clear();
	}
	
	public void addOutNode(ModuleNode node) {
		node.setNumber(getNOutNodes());
		node.setModule(this);
		mOutNode.add(node);
	}
	
	public void addOutNode(String name) {
		ModuleNode node = new ModuleNode(name, ModuleNode.OUT_NODE);
		addOutNode(node);
	}
	
	public void addOutNode(String name, String value) {
		ModuleNode node = new ModuleNode(name, ModuleNode.OUT_NODE, value);
		addOutNode(node);
	}
	
	public int getNOutNodes() {
		return mOutNode.size();
	}
	
	public ModuleNode getOutNode(int n) {
		try {
			return (ModuleNode)mOutNode.elementAt(n);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public ModuleNode getOutNode(String name) {
		int nOutNodes = getNOutNodes();
		for (int n=0; n<nOutNodes; n++) {
			String nodeName = getOutNodeName(n);
			if (nodeName != null) {
				if (nodeName.equals(name) == true)
					return getOutNode(n);
			}
		}
		return null;
	}
	
	public int getOutNodeNumber(ModuleNode node) {
		int nOutNodes = getNOutNodes();
		for (int n=0; n<nOutNodes; n++) {
			if (getOutNode(n) == node)
				return n;
		}
		return -1;
	}
	
	public String getOutNodeName(int n) {
		return getOutNode(n).getName();
	}

	public String getOutNodeValue(int n) {
		return getOutNode(n).getValue();
	}

	public boolean hasOutNode() {
		return (0 < getNOutNodes()) ? true : false;
	}

	////////////////////////////////////////////////
	//	setOutNodeValue
	////////////////////////////////////////////////

	public void setOutNodeValue(int n, String value) {
		getOutNode(n).setValue(value);
	}

	public void setOutNodeValue(int n, int value) {
		setOutNodeValue(n, Integer.toString(value));
	}

	public void setOutNodeValue(int n, float value) {
		setOutNodeValue(n, Float.toString(value));
	}

	public void setOutNodeValue(int n, double value) {
		setOutNodeValue(n, Double.toString(value));
	}

	public void setOutNodeValue(int n, boolean value) {
		Boolean bool = new Boolean(value);
		setOutNodeValue(n, bool.toString());
	}
	
	public void setOutNodeValue(int n, String value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(value[i]);
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setOutNodeValue(n, valueBuffer.toString());
	}
	
	public void setOutNodeValue(int n, int x, int y) {
		setOutNodeValue(n, Integer.toString(x) + "," + Integer.toString(y));
	}

	public void setOutNodeValue(int n, int x, int y, int z) {
		setOutNodeValue(n, Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
	}

	public void setOutNodeValue(int n, int x, int y, int z, int angle) {
		setOutNodeValue(n, Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z) + "," + Integer.toString(angle));
	}

	public void setOutNodeValue(int n, int value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(Integer.toString(value[i]));
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setOutNodeValue(n, valueBuffer.toString());
	}
	
	public void setOutNodeValue(int n, float x, float y) {
		setOutNodeValue(n, Float.toString(x) + "," + Float.toString(y));
	}

	public void setOutNodeValue(int n, float x, float y, float z) {
		setOutNodeValue(n, Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z));
	}

	public void setOutNodeValue(int n, float x, float y, float z, float angle) {
		setOutNodeValue(n, Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z) + "," + Float.toString(angle));
	}

	public void setOutNodeValue(int n, float value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(Float.toString(value[i]));
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setOutNodeValue(n, valueBuffer.toString());
	}
	
	public void setOutNodeValue(int n, double x, double y) {
		setOutNodeValue(n, Double.toString(x) + "," + Double.toString(y));
	}

	public void setOutNodeValue(int n, double x, double y, double z) {
		setOutNodeValue(n, Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z));
	}

	public void setOutNodeValue(int n, double x, double y, double z, double angle) {
		setOutNodeValue(n, Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z) + "," + Double.toString(angle));
	}
	
	public void setOutNodeValue(int n, double value[]) {
		if (value == null)
			return;
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<value.length; i++) {
			valueBuffer.append(Double.toString(value[i]));
			if (i < (value.length-1))
				valueBuffer.append(',');
		}
		setOutNodeValue(n, valueBuffer.toString());
	}
	
	////////////////////////////////////////////////
	//	ExecutionNode
	////////////////////////////////////////////////
	
	public void sendOutNodeValue(int n) {
		Diagram dgm = getDiagram();
		if (dgm != null) 
			dgm.sendModuleOutNodeValue(this, getOutNode(n));
	}

	public void sendOutNodeValue(int n, String value) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, int value) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, float value) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, double value) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, boolean value) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}
	
	public void sendOutNodeValue(int n, String value[]) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}
	
	public void sendOutNodeValue(int n, int x, int y) {
		setOutNodeValue(n, x, y);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, int x, int y, int z) {
		setOutNodeValue(n, x, y, z);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, int x, int y, int z, int angle) {
		setOutNodeValue(n, x, y, z, angle);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, int value[]) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}
	
	public void sendOutNodeValue(int n, float x, float y) {
		setOutNodeValue(n, x, y);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, float x, float y, float z) {
		setOutNodeValue(n, x, y, z);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, float x, float y, float z, float angle) {
		setOutNodeValue(n, x, y, z, angle);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, float value[]) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}
	
	public void sendOutNodeValue(int n, double x, double y) {
		setOutNodeValue(n, x, y);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, double x, double y, double z) {
		setOutNodeValue(n, x, y, z);
		sendOutNodeValue(n);
	}

	public void sendOutNodeValue(int n, double x, double y, double z, double angle) {
		setOutNodeValue(n, x, y, z, angle);
		sendOutNodeValue(n);
	}
	
	public void sendOutNodeValue(int n, double value[]) {
		setOutNodeValue(n, value);
		sendOutNodeValue(n);
	}

	////////////////////////////////////////////////
	//	ExecutionNode
	////////////////////////////////////////////////

	public ModuleNode mExecutionNode = null;

	public boolean hasExecutionNode() {
		return (getExecutionNode() != null ? true : false);
	}

	public void enableExecutionNode() {
		ModuleNode node = new ModuleNode(ModuleNode.EXECUTION_NODE_NAME, ModuleNode.EXECUTION_NODE);
		node.setModule(this);
		setExecutionNode(node);
	}

	public void disableExecutionNode() {
		setExecutionNode(null);
	}
	
	public void setExecutionNode(ModuleNode node) {
		mExecutionNode = node;
	}
	
	public ModuleNode getExecutionNode() {
		return mExecutionNode;
	}

	public void setExecutionNodeValue(String value) {
		if (mExecutionNode == null)
			return;
		mExecutionNode.setValue(value);
	}

	public String getExecutionNodeValue() {
		if (mExecutionNode == null)
			return null;
		return mExecutionNode.getValue();
	}

	////////////////////////////////////////////////
	//	Node Size/Position
	////////////////////////////////////////////////
		
	public int getNodeSize() {
		return NODE_SIZE;
	}

	public int getNodeSpacing() {
		return NODE_SPACING;
	}
	
	public int getInNodeSpacing() {
		return (SIZE - NODE_SIZE * getNInNodes()) / (getNInNodes()+1);
	}
	
	public int getOutNodeSpacing()	{
		return (SIZE - NODE_SIZE * getNOutNodes()) / (getNOutNodes()+1);
	}

	
	public int getInNodeXPosition(int n) {
		return (getXPosition() - getNodeSize());
	}

	public int getInNodeYPosition(int n) {
		return (getYPosition() + (getInNodeSpacing() * (n+1)) + (getNodeSize() * n));
	}
		
	
	public int getInNodeXPosition(ModuleNode moduleNode) {
		return getInNodeXPosition(moduleNode.getNumber());
	}

	public int getInNodeYPosition(ModuleNode moduleNode) {
		return getInNodeYPosition(moduleNode.getNumber());
	}
	
	
	public int getOutNodeXPosition(int n) {
		return (getXPosition() + getSize());
	}

	public int getOutNodeYPosition(int n) {
		return (getYPosition() + (getOutNodeSpacing() * (n+1)) + (getNodeSize() * n));
	}

	
	public int getOutNodeXPosition(ModuleNode moduleNode) {
		if (moduleNode.isExecutionNode() == true)
			return getExecutionNodeXPosition();
		return getOutNodeXPosition(moduleNode.getNumber());
	}
	
	public int getOutNodeYPosition(ModuleNode moduleNode) {
		if (moduleNode.isExecutionNode() == true)
			return getExecutionNodeYPosition();
		return getOutNodeYPosition(moduleNode.getNumber());
	}
	
	
	public int getExecutionNodeXPosition() {
		return (getXPosition() + getSize()/2 - getNodeSize()/2);
	}

	public int getExecutionNodeYPosition() {
		return (getYPosition() - getNodeSize());
	}
	
	////////////////////////////////////////////////
	//	Inside
	////////////////////////////////////////////////

	private boolean isRect(int x0, int y0, int x1, int y1, int x, int y) {
		if (x0 <= x && x <= x1 && y0 <= y && y <= y1)
			return true;
		else
			return false;
	}

	public int isInside(int x, int y) {
		int mx = getXPosition();
		int my = getYPosition();
		int mSize = getSize();
		if (isRect(mx, my, mx+mSize, my+mSize, x, y))
			return INSIDE;

		int		n;
		int		nodeSize = getNodeSize();
		int		nodex, nodey;

		for (n=0; n<getNInNodes(); n++) {
			nodex = getInNodeXPosition(n);
			nodey = getInNodeYPosition(n);
			if (isRect(nodex, nodey, nodex+nodeSize, nodey+nodeSize, x, y) == true)
				return (INSIDE_INNODE | n);
		}

		for (n=0; n<getNOutNodes(); n++) {
			nodex = getOutNodeXPosition(n);
			nodey = getOutNodeYPosition(n);
			if (isRect(nodex, nodey, nodex+nodeSize, nodey+nodeSize, x, y) == true)
				return (INSIDE_OUTNODE | n);
		}

		if (hasExecutionNode()) {
			nodex = getExecutionNodeXPosition();
			nodey = getExecutionNodeYPosition();
			if (isRect(nodex, nodey, nodex+nodeSize, nodey+nodeSize, x, y) == true)
				return INSIDE_EXECUTIONNODE;
		}

		int		expandSize = getNodeSize();

		// Add expandSize to only x position
		for (n=0; n<getNInNodes(); n++) {
			nodex = getInNodeXPosition(n);
			nodey = getInNodeYPosition(n);
			if (isRect(nodex-expandSize, nodey, nodex+nodeSize, nodey+nodeSize, x, y) == true)
				return (INSIDE_INNODE | n);
		}

		for (n=0; n<getNOutNodes(); n++) {
			nodex = getOutNodeXPosition(n);
			nodey = getOutNodeYPosition(n);
			if (isRect(nodex, nodey, nodex+nodeSize+expandSize, nodey+nodeSize, x, y) == true)
				return (INSIDE_OUTNODE | n);
		}

		// Add expandSize to x/y position
		for (n=0; n<getNInNodes(); n++) {
			nodex = getInNodeXPosition(n);
			nodey = getInNodeYPosition(n);
			if (isRect(nodex-expandSize, nodey-expandSize, nodex+nodeSize, nodey+nodeSize+expandSize, x, y) == true)
				return (INSIDE_INNODE | n);
		}

		for (n=0; n<getNOutNodes(); n++) {
			nodex = getOutNodeXPosition(n);
			nodey = getOutNodeYPosition(n);
			if (isRect(nodex, nodey-expandSize, nodex+nodeSize+expandSize, nodey+nodeSize+expandSize, x, y) == true)
				return (INSIDE_OUTNODE | n);
		}

		if (hasExecutionNode()) {
			nodex = getExecutionNodeXPosition();
			nodey = getExecutionNodeYPosition();
			if (isRect(nodex-expandSize, nodey-expandSize, nodex+nodeSize+expandSize, nodey+nodeSize, x, y) == true)
				return INSIDE_EXECUTIONNODE;
		}

		return OUTSIDE;	
	}

	////////////////////////////////////////////////
	//	next node list
	////////////////////////////////////////////////

	public Module next () {
		return (Module)getNextNode();
	}

	////////////////////////////////////////////////
	//	 Callback methods (Release 1.0)
	////////////////////////////////////////////////

	public final void moduleInitialized() {
	}

	public final void moduleActivated() {
	}
	
	public final void nodeValueChanged(ModuleNode node) {
	}

	////////////////////////////////////////////////
	//	 Callback methods (Release 1.1)
	////////////////////////////////////////////////
	
	abstract public void initialize();
	
	abstract public void processData(ModuleNode inNode[], ModuleNode exeNode);

	abstract public void shutdown();
	
	////////////////////////////////////////////////
	//	 Event methods (Release 1.1)
	////////////////////////////////////////////////
	
	private ModuleNode	mEventInNode[];
	private ModuleNode	mEventExeNode;
 	private int				mNRecievedDataCount;
	private int				mInputDataflowCount;
		
	public void initializeEventData() {
		setInputDataflowCount(getNInputDataflows());
		
		int inNodeCount = getNInNodes();
		mEventInNode = new ModuleNode[inNodeCount];
		for (int n=0; n<inNodeCount; n++)
			mEventInNode[n] = getInNode(n);
			
		mEventExeNode = getExecutionNode();
	}

	public void clearEventData() {
		setRecievedDataCount(0);
	}
	
	
	public void setInputDataflowCount(int n) {
		mInputDataflowCount = n;
	}
	
	public int getInputDataflowCount() {
		return mInputDataflowCount;
	}


	public void setRecievedDataCount(int n) {
		mNRecievedDataCount = n;
	}
	
	public int getRecievedDataCount() {
		return mNRecievedDataCount;
	}

	public void incrementRecievedDataCount() {
		mNRecievedDataCount++;
	}


	public void runProcessDataMethod() {
		processData(mEventInNode, mEventExeNode);
	}
	
	////////////////////////////////////////
	// DataFlow 
	////////////////////////////////////////
	
	public boolean hasInputDataflow(ModuleNode inModuleNode) {
		return getDiagram().hasInputDataflow(this, inModuleNode);
	}

	public int getNDataflows() {
		return getDiagram().getNModuleDataflows(this);
	}
	
	public int getNInputDataflows() {
		return getDiagram().getNModuleInputDataflows(this);
	}

	public int getNOutputDataflows() {
		return getDiagram().getNModuleOutputDataflows(this);
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
}
