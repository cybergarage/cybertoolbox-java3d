/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleSystemMouse.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.field.*;

public class DialogModuleSystemMouse extends Dialog {
	
	private CoordinateComboBoxComponent mCoordinateComboBoxComponent = null;
	
	public DialogModuleSystemMouse(Frame parentFrame, Module module) {
		super(parentFrame, "Module System::Mouse");
		mCoordinateComboBoxComponent = new CoordinateComboBoxComponent();
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mCoordinateComboBoxComponent;
		setComponents(dialogComponent);
		
		setCoordinate(module.getStringValue());
	}
	
	public void setCoordinate(String coord) {
		if (coord == null)
			return;
		for (int n=0; n<mCoordinateComboBoxComponent.getItemCount(); n++) {
			String itemName = (String)mCoordinateComboBoxComponent.getItemAt(n);
			if (itemName.equals(coord) == true) {
				mCoordinateComboBoxComponent.setSelectedIndex(n);
				return;
			}
		}
	}

	public String getCoordinate() {
		return (String)mCoordinateComboBoxComponent.getSelectedItem();
	}

	private class CoordinateComboBoxComponent extends JComboBox {
		public CoordinateComboBoxComponent() {
			setMaximumRowCount(2);		
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Coordinate Format")));
			addItem(World.MOUSE_FRAME_COORDINATE_STRING);
			addItem(World.MOUSE_NORMALIZED_COORDINATE_STRING);
		}
	}

}
		
