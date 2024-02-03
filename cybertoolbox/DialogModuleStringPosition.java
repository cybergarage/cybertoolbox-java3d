/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleStringPosition.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleStringPosition extends Dialog {
	private DialogValueFieldComponent mTextFieldComponent[] = new DialogValueFieldComponent[3];
	
	public DialogModuleStringPosition(Frame parentFrame, Module module) {
		super(parentFrame, "Module String::Position");
		
		mTextFieldComponent[0] = new DialogValueFieldComponent("X");
		mTextFieldComponent[1] = new DialogValueFieldComponent("Y");
		mTextFieldComponent[2] = new DialogValueFieldComponent("Z");
		
		double pos[] = module.getDoubleValues();
		if (pos != null && pos.length == 3) {
			mTextFieldComponent[0].setText(Double.toString(pos[0]));
			mTextFieldComponent[1].setText(Double.toString(pos[1]));
			mTextFieldComponent[2].setText(Double.toString(pos[2]));
		}
		else {
			mTextFieldComponent[0].setText("0");
			mTextFieldComponent[1].setText("0");
			mTextFieldComponent[2].setText("0");
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
		
