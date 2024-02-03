/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleMiscArrayName.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class DialogModuleMiscArrayName extends Dialog {
	
	private DialogTextFieldComponent mGroupNameTextField = null;
	private DialogTextFieldComponent mDataNameTextField = null;
	
	public DialogModuleMiscArrayName(Frame parentFrame, Module module) {
		super(parentFrame, "Module Misc::" + module.getTypeName());
		
		mGroupNameTextField = new DialogTextFieldComponent("Group Name");
		mDataNameTextField = new DialogTextFieldComponent("Data Name");
		
		JComponent dialogComponent[] = new JComponent[2];
		dialogComponent[0] = mGroupNameTextField;
		dialogComponent[1] = mDataNameTextField;
		
		String name[] = module.getStringValues();
		if (name != null && name.length == 2) {
			mGroupNameTextField.setText(name[0]);
			mDataNameTextField.setText(name[1]);
		}
		
		setComponents(dialogComponent);
	}

	public String getGroupName() {
		return mGroupNameTextField.getText();
	}
	
	public String getDataName() {
		return mDataNameTextField.getText();
	}
}
		
