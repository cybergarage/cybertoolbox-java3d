/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ConstSFImage.java
*
******************************************************************/

package cv97.field;

import java.io.PrintWriter;
import cv97.*;

public class ConstSFImage extends ConstMField {

	public ConstSFImage() {
		setType(fieldTypeConstSFImage);
	}

	public void addValue(int value) {
		SFInt32 sfvalue = new SFInt32(value);
		add(sfvalue);
	}

	public void addValue(SFInt32 sfvalue) {
		add(sfvalue);
	}

	public void insertValue(int index, int value) {
		SFInt32 sfvalue = new SFInt32(value);
		insert(index, sfvalue);
	}

	public void insertValue(int index, String value) {
		SFInt32 sfvalue = new SFInt32(value);
		insert(index, sfvalue);
	}
	
	public int get1Value(int index) {
		SFInt32 sfvalue = (SFInt32)getField(index);
		return sfvalue.getValue();
	}

	public void set1Value(int index, int value) {
		SFInt32 sfvalue = (SFInt32)getField(index);
		sfvalue.setValue(value);
	}

	////////////////////////////////////////////////
	//	Output
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		float	value[] = new float[3];
		int		nOutput = 0;
		for (int n=0; n<getSize(); n++) {
			printStream.print(indentString);
			if (n < getSize()) { 
				printStream.print(get1Value(n) + " ");
				nOutput++;
				if (32 < nOutput) {
					printStream.println("");
					printStream.print(indentString);
					nOutput = 0;
				}
			}
			printStream.println("");
		}
	}

	////////////////////////////////////////////////
	//	toString
	////////////////////////////////////////////////

	public String toString() {
		return null;
	}

	////////////////////////////////////////////////
	//	Referrence Field Object 
	////////////////////////////////////////////////

	public Field createReferenceFieldObject() {
		SFImage field = new SFImage();
		field.setName(getName());
		field.setObject(getObject());
		return field;
	}
}