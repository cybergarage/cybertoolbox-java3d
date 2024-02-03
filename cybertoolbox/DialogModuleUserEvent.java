/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleUserEvent.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleUserEvent extends Dialog {

	private UserEventComboBoxComponent mUserEventComboBoxComponent = null;
	
	public DialogModuleUserEvent(Frame parentFrame, Module module) {
		super(parentFrame, "Module Misc::SendMessage");
		
		mUserEventComboBoxComponent = new UserEventComboBoxComponent(module.getWorld());
		setUserEvent(module.getStringValue());
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mUserEventComboBoxComponent;
		setComponents(dialogComponent);
	}
	
	public void setUserEvent(String eventName) {
		if (eventName == null)
			return;
		for (int n=0; n<mUserEventComboBoxComponent.getItemCount(); n++) {
			String itemName = (String)mUserEventComboBoxComponent.getItemAt(n);
			if (itemName.equals(eventName) == true) {
				mUserEventComboBoxComponent.setSelectedIndex(n);
				return;
			}
		}
	}
	
	public String getEventName() {
		return (String)mUserEventComboBoxComponent.getSelectedItem();
	}

	private class UserEventComboBoxComponent extends JComboBox {
		public UserEventComboBoxComponent(World world) {
			setMaximumRowCount(2);		
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "User Event")));
			for (int n=0; n<world.getNEvents(); n++) {
				Event event = world.getEvent(n);
				if (event.getAttribute() != EventType.ATTRIBUTE_USER)
					continue;
				String eventName = event.getName();
				if (eventName.equalsIgnoreCase("User") == true) {
					String optionString = event.getOptionString();
					if (optionString != null)
						addItem(optionString);
				}
			}
		}
	}
}
		
