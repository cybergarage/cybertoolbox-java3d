/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventUserPanel.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class DialogEventUserPanel extends DialogEventPanel {
	
	private DialogTextFieldComponent mTextFieldComponent = null;
	
	public DialogEventUserPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mTextFieldComponent = new DialogTextFieldComponent("Event Name");
		//mTextFieldComponent.setText("");
		add(mTextFieldComponent);
	}

	public DialogEventUserPanel(World world, Event event) {
		this(world);
		setEventName(event.getOptionString());
	}
	
	public void setEventName(String name) {
		mTextFieldComponent.setText(name);
	}
		
	public String getEventName() {
		return mTextFieldComponent.getText();
	}

	public String getOptionString() {
		return getEventName();
	}
}
		
