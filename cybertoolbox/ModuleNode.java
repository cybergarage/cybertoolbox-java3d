/******************************************************************
*
*	CyberToolbox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	ModuleEvent.java
*
******************************************************************/

public class ModuleNode extends Object {

	public static final int IN_NODE				= 0;
	public static final int OUT_NODE				= 1;
	public static final int EXECUTION_NODE	= 2;
	
	public static final String EXECUTION_NODE_NAME	= "executionNode";
	
	public ModuleNode(String name, int type) {
		setModule(null);
		setName(name);
		setType(type);
		setNumber(-1);
		setValue((String)null);
	}

	public ModuleNode(String name, int type, int n) {
		setModule(null);
		setName(name);
		setType(type);
		setNumber(n);
		setValue((String)null);
	}

	public ModuleNode(String name, int type, String value) {
		setModule(null);
		setName(name);
		setType(type);
		setNumber(-1);
		setValue(value);
	}
	
	public ModuleNode(String name, int type, int n, String value) {
		setModule(null);
		setName(name);
		setType(type);
		setNumber(n);
		setValue(value);
	}

	////////////////////////////////////////
	// Module
	////////////////////////////////////////
	
	private Module mModule;
	
	public void setModule(Module module) {
		mModule = module;
	}

	public Module getModule() {
		return mModule;
	}

	////////////////////////////////////////
	// Name
	////////////////////////////////////////
	
	private String mName;
	
	private void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	////////////////////////////////////////
	// Name
	////////////////////////////////////////
	
	private int mNumber;
	
	public void setNumber(int n) {
		mNumber = n;
	}

	public int getNumber() {
		return mNumber;
	}
	
	////////////////////////////////////////
	// Value
	////////////////////////////////////////
	
	private String mValue;
	
	////////////////////////////////////////
	// setValue
	////////////////////////////////////////
	
	public void setValue(String value) {
		mValue = value;
	}

	public void setValue(ModuleNode node) {
		setValue(node.getValue());
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

	////////////////////////////////////////
	// getValue
	////////////////////////////////////////
	
	public String getValue() {
		return mValue;
	}
		
	public String getStringValue() {
		return mValue;
	}

	public int getIntegerValue() throws NumberFormatException {
		Integer value = new Integer(mValue);
		return value.intValue();
	}

	public float getFloatValue() throws NumberFormatException {
		Float value = new Float(mValue);
		return value.floatValue();
	}

	public double getDoubleValue() throws NumberFormatException {
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
	
	////////////////////////////////////////
	// Type 
	////////////////////////////////////////

	private int mType;
	
	private void setType(int type) {
		mType = type;
	}
	
	public int getType() {
		return mType;
	}
	
	public boolean isInNode() {
		if (getType() != IN_NODE)
			return false;
		return true;
	}

	public boolean isOutNode() {
		if (getType() != OUT_NODE)
			return false;
		return true;
	}

	public boolean isExecutionNode() {
		if (getType() != EXECUTION_NODE)
			return false;
		return true;
	}

	////////////////////////////////////////
	// DataFlow 
	////////////////////////////////////////
	
	public boolean isConnected() {
		return getModule().hasInputDataflow(this); 
	}
	
	////////////////////////////////////////
	// toString 
	////////////////////////////////////////
	
	public String toString() {
		return getName() + " " + getValue();
	}
}