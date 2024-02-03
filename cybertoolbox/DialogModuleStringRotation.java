/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleStringOrientation.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleStringRotation extends Dialog {
	private DialogValueFieldComponent mTextFieldComponent[] = new DialogValueFieldComponent[4];
	
	public DialogModuleStringRotation(Frame parentFrame, Module module) {
		super(parentFrame, "Module String::Rotation");
		
		mTextFieldComponent[0] = new DialogValueFieldComponent("X");
		mTextFieldComponent[1] = new DialogValueFieldComponent("Y");
		mTextFieldComponent[2] = new DialogValueFieldComponent("Z");
		mTextFieldComponent[3] = new DialogValueFieldComponent("Angle");
		
		double rotation[] = module.getDoubleValues();
		if (rotation != null && rotation.length == 4) {
			mTextFieldComponent[0].setText(Double.toString(rotation[0]));
			mTextFieldComponent[1].setText(Double.toString(rotation[1]));
			mTextFieldComponent[2].setText(Double.toString(rotation[2]));
			mTextFieldComponent[3].setText(Double.toString(rotation[3]));
		}
		else {
			mTextFieldComponent[0].setText("0");
			mTextFieldComponent[1].setText("0");
			mTextFieldComponent[2].setText("1");
			mTextFieldComponent[3].setText("0");
		}
												
		JComponent dialogComponent[] = new JComponent[4];
		dialogComponent[0] = mTextFieldComponent[0];
		dialogComponent[1] = mTextFieldComponent[1];
		dialogComponent[2] = mTextFieldComponent[2];
		dialogComponent[3] = mTextFieldComponent[3];
		
		setComponents(dialogComponent);
	}
	
	public float []getRotationValue() {
		float rotation[] = {0.0f, 0.0f, 1.0f, 0.0f};
		try {
			for (int n=0; n<4; n++) {
				Double value = new Double(mTextFieldComponent[n].getText());
				rotation[n] = value.floatValue();
			}
		}
		catch (NumberFormatException nfe ) {}
		return rotation;
	}
}
		
