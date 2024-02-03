/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleSensorBeeBox.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class DialogModuleSensorBeeBox extends Dialog {

	private SerialComboBoxComponent		mSerialComboBoxComponent	= null;
	
	public DialogModuleSensorBeeBox(Frame parentFrame, Module module) {
		super(parentFrame, "Module Sensor::BeeBox");
		
		mSerialComboBoxComponent	= new SerialComboBoxComponent();
		
		String value = module.getStringValue();
		setSerialPort(value);
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mSerialComboBoxComponent;
		setComponents(dialogComponent);
	}
	
	public void setSerialPort(String portName) {
		if (portName == null)
			return;
		for (int n=0; n<mSerialComboBoxComponent.getItemCount(); n++) {
			String itemName = (String)mSerialComboBoxComponent.getItemAt(n);
			if (itemName.equals(portName) == true) {
				mSerialComboBoxComponent.setSelectedIndex(n);
				return;
			}
		}
	}

	public String getSerialPort() {
		return (String)mSerialComboBoxComponent.getSelectedItem();
	}

	private class SerialComboBoxComponent extends JComboBox {
		public SerialComboBoxComponent() {
			setMaximumRowCount(2);		
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Serial Port")));
			addItem("SERIAL1");
			addItem("SERIAL2");
			addItem("SERIAL3");
			addItem("SERIAL4");
		}
	}
}
		
