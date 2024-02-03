/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleStringColor.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleStringColor extends JColorChooser {
	
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = -1;

	private Component	mParentComponent	= null;
	private Color		mInitialColor 		= new Color(1, 1, 1);
	private Color		mSelectedColor		= new Color(1, 1, 1);
	
	public DialogModuleStringColor(Frame parentFrame, Module module) {
		mParentComponent = parentFrame;
		
		float color[] = module.getFloatValues();
		if (color != null && color.length == 3) 
			mInitialColor = new Color(color[0], color[1], color[2]);
	}
	
	public int doModal() {
		Color color = showDialog(mParentComponent,"Module String::Color", mInitialColor);
		if (mSelectedColor == null)
			return CANCEL_OPTION;
		mSelectedColor = color;
		return OK_OPTION;
	}

	public float[] getColorValue() {
		float color[] = new float[3];
		mSelectedColor.getColorComponents(color);
		return color;
	}
}
		
