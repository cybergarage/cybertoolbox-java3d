/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleMiscShowStatus.java
*
******************************************************************/

import java.awt.*;
import javax.swing.*;

public class DialogModuleMiscShowStatus extends Dialog {
	
	private DialogTextFieldComponent	mStatusStringTextField;
	
	public DialogModuleMiscShowStatus(Frame parentFrame, Module module) {
		super(parentFrame, "Module Misc::" + module.getTypeName());
		
		mStatusStringTextField	= new DialogTextFieldComponent("Status");
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mStatusStringTextField;
		
		setStatusString(null);
		
		String moduleValue = module.getStringValue();
		if (moduleValue != null) 
			setStatusString(moduleValue);
		
		setComponents(dialogComponent);
	}

	public void setStatusString(String target) {
		mStatusStringTextField.setText(target);
	}

	public String getStatusString() {
		return mStatusStringTextField.getText();
	}
}
		
