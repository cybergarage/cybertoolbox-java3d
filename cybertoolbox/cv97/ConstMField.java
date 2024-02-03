/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ConstMField.java
*
******************************************************************/

package cv97;

import java.io.PrintWriter;
import java.util.Vector;

import cv97.field.*;

public abstract class ConstMField extends ConstField {

	private Vector	mFieldVector = new Vector();

	public ConstMField() {
	}

	public ConstMField(MField field) {
		setObject(field.getObject());
	}
	
	public ConstMField(ConstMField constMField) {
		setObject(constMField.getObject());
	}

	public void setObject(Object object) {
		mFieldVector = (Vector)object;
	}

	public Object getObject() {
		return mFieldVector;
	}

	public int getSize() {
		return mFieldVector.size();
	}

	public void add(Object object) {
		mFieldVector.addElement(object);
	}
	
	public void add(Field field) {
		add((Object)field);
	}

	public void add(BaseNode node) {
		add((Object)node);
	}

	public void insert(int index, Object object) {
		mFieldVector.insertElementAt(object, index);
	}

	public void insert(int index, Field field) {
		insert(index, (Object)field);
	}

	public void clear() {
		mFieldVector.removeAllElements();
	}

	public void delete(int index) {
		mFieldVector.removeElementAt(index);
	}

	public void remove(int index) {
		mFieldVector.removeElementAt(index);
	}

	public void removeValue(int index) {
		remove(index);
	}

	public void removeAllValues() {
		clear();
	}
	
	public Object get(int index) {
		return mFieldVector.elementAt(index);
	}

	public Field getField(int index) {
		return (Field)get(index);
	}

	public void set(int index, Object object) {
		mFieldVector.setElementAt(object, index);
	}
	
	public void setField(int index, Field field) {
		set(index, (Object)field);
	}

	public void copy(MField srcMField) {
		clear();
		for (int n=0; n<srcMField.getSize(); n++) {
			add(srcMField.get(n));
		}
	}

	public void copy(ConstMField srcMField) {
		clear();
		for (int n=0; n<srcMField.getSize(); n++) {
			add(srcMField.get(n));
		}
	}

	abstract public void outputContext(PrintWriter printStream, String indentString);

	////////////////////////////////////////////////
	//	setValue 
	////////////////////////////////////////////////

	public void setValue(String string) {
		String token[] = null;
		if (isSingleValueMField() == true)
			token = MFieldSingleValueTokenizer.getTokens(string);
		else
			token = MFieldMultiValueTokenizer.getTokens(string);
		setValue(token);
	}

	public void setValue(String value[]) {
		if (value == null)
			return;
		clear();
		int size = value.length;
		for (int n=0; n<size; n++)
			add(value[n]);
	}

	public void setValues(String value)
	{
		setValue(value);
	}
	
	public void setValues(String value[]) 
	{
		setValue(value);
	}

	public void setValue(Field field)
	{
		if (field instanceof MField)
			setValue((MField)field);
		else if (field instanceof ConstMField)
			setValue((ConstMField)field);
	}

	public void setValue(MField field) {
		copy(field);
	}

	public void setValue(ConstMField field) {
		copy(field);
	}

};
