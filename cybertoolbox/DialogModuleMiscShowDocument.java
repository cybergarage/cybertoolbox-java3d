/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleMiscShowDocument.java
*
******************************************************************/

import java.awt.*;
import javax.swing.*;

public class DialogModuleMiscShowDocument extends Dialog {
	
	private DialogTextFieldComponent	mURLStringTextField;
	private DialogTextFieldComponent	mTargetStringTextField;
	
	public DialogModuleMiscShowDocument(Frame parentFrame, Module module) {
		super(parentFrame, "Module Misc::" + module.getTypeName());
		
		mURLStringTextField		= new DialogTextFieldComponent("URL");
		mTargetStringTextField	= new DialogTextFieldComponent("Target");
		
		JComponent dialogComponent[] = new JComponent[2];
		dialogComponent[0] = mURLStringTextField;
		dialogComponent[1] = mTargetStringTextField;
		
		setURLString(null);
		setTargetString(null);
		
		String moduleValue[] = module.getStringValues();
		if (moduleValue != null) {
			if (0 < moduleValue.length)
				setURLString(moduleValue[0]);
			if (1 < moduleValue.length)
				setTargetString(moduleValue[1]);
		}
		
		setComponents(dialogComponent);
	}

	public void setURLString(String url) {
		mURLStringTextField.setText(url);
	}

	public void setTargetString(String target) {
		mTargetStringTextField.setText(target);
	}

	public String getURLString() {
		return mURLStringTextField.getText();
	}

	public String getTargetString() {
		String targetString = mTargetStringTextField.getText();
		if (targetString != null) {
			if (0 < targetString.length())
				return targetString;
		}
		return null;
	}
}
		
