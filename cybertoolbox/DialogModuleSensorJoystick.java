/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleSensorJoystick.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;


public class DialogModuleSensorJoystick extends Dialog {

	private PortComboBoxComponent	mPortComboBoxComponent = null;
	
	public DialogModuleSensorJoystick(Frame parentFrame, Module module) {
		super(parentFrame, "Module Sensor::Joystick");
		
		mPortComboBoxComponent	= new PortComboBoxComponent();
		setGamePort(module.getStringValue());
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mPortComboBoxComponent;
		setComponents(dialogComponent);
	}
	
	public void setGamePort(String portName) {
		if (portName == null)
			return;
		for (int n=0; n<mPortComboBoxComponent.getItemCount(); n++) {
			String itemName = (String)mPortComboBoxComponent.getItemAt(n);
			if (itemName.equals(portName) == true) {
				mPortComboBoxComponent.setSelectedIndex(n);
				return;
			}
		}
	}

	public String getGamePort() {
		return (String)mPortComboBoxComponent.getSelectedItem();
	}

	private class PortComboBoxComponent extends JComboBox {
		public PortComboBoxComponent() {
			setMaximumRowCount(2);		
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Game Port")));
			addItem("PORT1");
			addItem("PORT2");
		}
	}
}
		
