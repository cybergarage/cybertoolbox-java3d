/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleFilterDiv.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class DialogModuleFilterDiv extends Dialog {
	private DialogValueFieldComponent mTextFieldComponent = null;
	
	public DialogModuleFilterDiv(Frame parentFrame, Module module) {
		super(parentFrame, "Module Filter::Div");
		mTextFieldComponent = new DialogValueFieldComponent("Div value");
		mTextFieldComponent.setText(module.getValue());
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mTextFieldComponent;
		setComponents(dialogComponent);
	}
	
	public String getDivValue() {
		return mTextFieldComponent.getText();
	}
}
		
