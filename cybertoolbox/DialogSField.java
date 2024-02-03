/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogSField.java
*
******************************************************************/

import java.awt.Frame;

import cv97.Field;

public abstract class DialogSField extends Dialog {

	public DialogSField(Frame parentFrame, Field field) {
		super(parentFrame, field.getTypeName() + " - " + field.getName());
	}
	
	abstract public String getStringValue();
}
		
