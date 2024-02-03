/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleFilterRange.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleFilterRange extends Dialog {
	private DialogValueFieldComponent mHighTextFieldComponent = null;
	private DialogValueFieldComponent mLowTextFieldComponent = null;
	
	public DialogModuleFilterRange(Frame parentFrame, Module module) {
		super(parentFrame, "Module Filter::Range");
		
		mHighTextFieldComponent = new DialogValueFieldComponent("High value");
		mLowTextFieldComponent = new DialogValueFieldComponent("Low value");
		
		double moduleValue[] = module.getDoubleValues();
		if (moduleValue != null) {
			if (moduleValue.length == 2) {
				mHighTextFieldComponent.setValue(moduleValue[0]);
				mLowTextFieldComponent.setValue(moduleValue[1]);
			}
		}
				
		JComponent dialogComponent[] = new JComponent[2];
		dialogComponent[0] = mHighTextFieldComponent;
		dialogComponent[1] = mLowTextFieldComponent;
		
		setComponents(dialogComponent);
	}
	
	public String getHighValue() {
		return mHighTextFieldComponent.getText();
	}

	public String getLowValue() {
		return mLowTextFieldComponent.getText();
	}
}
		
