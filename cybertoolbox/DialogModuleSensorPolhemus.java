/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleSensorPolhemus.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class DialogModuleSensorPolhemus extends Dialog {

	private SerialComboBoxComponent		mSerialComboBoxComponent	= null;
	private BaudComboBoxComponent			mBaudComboBoxComponent		= null;
	private ReceiverComboBoxComponent	mReceiverComboBoxComponent	= null;
	
	public DialogModuleSensorPolhemus(Frame parentFrame, Module module, String title) {
		super(parentFrame, title);
		
		mSerialComboBoxComponent	= new SerialComboBoxComponent();
		mBaudComboBoxComponent		= new BaudComboBoxComponent();
		mReceiverComboBoxComponent	= new ReceiverComboBoxComponent();
		
		String value[] = module.getStringValues();
		if (value != null && value.length == 3) {
			setSerialPort(value[0]);
			setBaud(value[1]);
			setReceiver(value[2]);
		}
		
		JComponent dialogComponent[] = new JComponent[3];
		dialogComponent[0] = mSerialComboBoxComponent;
		dialogComponent[1] = mBaudComboBoxComponent;
		dialogComponent[2] = mReceiverComboBoxComponent;
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

	public void setBaud(String baudName) {
		if (baudName == null)
			return;
		int baud = (int)Double.parseDouble(baudName);
		for (int n=0; n<mBaudComboBoxComponent.getItemCount(); n++) {
			String itemName = (String)mBaudComboBoxComponent.getItemAt(n);
			int itemBaud = (int)Double.parseDouble(itemName);
			if (itemBaud == baud) {
				mBaudComboBoxComponent.setSelectedIndex(n);
				return;
			}
		}
	}

	public void setReceiver(String recvName) {
		if (recvName == null)
			return;
		int recv = (int)Double.parseDouble(recvName);
		for (int n=0; n<mReceiverComboBoxComponent.getItemCount(); n++) {
			String itemName = (String)mReceiverComboBoxComponent.getItemAt(n);
			int itemRecv = (int)Double.parseDouble(itemName);
			if (itemRecv == recv) {
				mReceiverComboBoxComponent.setSelectedIndex(n);
				return;
			}
		}
	}
	
	public String getSerialPort() {
		return (String)mSerialComboBoxComponent.getSelectedItem();
	}

	public String getBaud() {
		return (String)mBaudComboBoxComponent.getSelectedItem();
	}

	public String getReciver() {
		return (String)mReceiverComboBoxComponent.getSelectedItem();
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

	private class BaudComboBoxComponent extends JComboBox {
		public BaudComboBoxComponent() {
			setMaximumRowCount(2);		
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Baud Rate")));
			addItem("9600");
			addItem("19200");
			addItem("38400");
			addItem("57600");
			addItem("115200");
		}
	}

	private class ReceiverComboBoxComponent extends JComboBox {
		public ReceiverComboBoxComponent() {
			setMaximumRowCount(2);		
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Receiver")));
			addItem("1");
			addItem("2");
			addItem("3");
			addItem("4");
		}
	}
}
		
