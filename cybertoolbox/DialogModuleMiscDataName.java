/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleMiscDataName.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class DialogModuleMiscDataName extends Dialog {
	
	private DialogTextFieldComponent mDataNameTextField = null;
	
	public DialogModuleMiscDataName(Frame parentFrame, Module module) {
		super(parentFrame, "Module Misc::" + module.getTypeName());
		
		mDataNameTextField = new DialogTextFieldComponent("Data Name");
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mDataNameTextField;
		
		String dataName = module.getStringValue();
		if (dataName != null)
			mDataNameTextField.setText(dataName);
		
		setComponents(dialogComponent);
	}

	public String getDataName() {
		return mDataNameTextField.getText();
	}
}
		
