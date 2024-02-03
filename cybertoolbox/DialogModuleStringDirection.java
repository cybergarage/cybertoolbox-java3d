/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleStringVector.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleStringDirection extends Dialog {
	private DialogValueFieldComponent mTextFieldComponent[] = new DialogValueFieldComponent[3];
	
	public DialogModuleStringDirection(Frame parentFrame, Module module) {
		super(parentFrame, "Module String::Direction");
		
		mTextFieldComponent[0] = new DialogValueFieldComponent("X");
		mTextFieldComponent[1] = new DialogValueFieldComponent("Y");
		mTextFieldComponent[2] = new DialogValueFieldComponent("Z");
		
		double dir[] = module.getDoubleValues();
		if (dir != null && dir.length == 3) {
			mTextFieldComponent[0].setText(Double.toString(dir[0]));
			mTextFieldComponent[1].setText(Double.toString(dir[1]));
			mTextFieldComponent[2].setText(Double.toString(dir[2]));
		}
		else {
			mTextFieldComponent[0].setText("0");
			mTextFieldComponent[1].setText("0");
			mTextFieldComponent[2].setText("1");
		}
												
		JComponent dialogComponent[] = new JComponent[3];
		dialogComponent[0] = mTextFieldComponent[0];
		dialogComponent[1] = mTextFieldComponent[1];
		dialogComponent[2] = mTextFieldComponent[2];
		
		setComponents(dialogComponent);
	}
	
	public float []getVectorValue() {
		float vector[] = {0.0f, 0.0f, 0.0f};
		try {
			for (int n=0; n<3; n++) {
				Double value = new Double(mTextFieldComponent[n].getText());
				vector[n] = value.floatValue();
			}
		}
		catch (NumberFormatException nfe ) {}
		return vector;
	}
}
		
