/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventCollisionPanel.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogEventCollisionPanel extends DialogEventPanel {
	
	private ShapeComboBoxComponent mShapeComboBoxComponent[] = new ShapeComboBoxComponent[2];
	private DialogValueFieldComponent mIntervalTimeComponent = null;
	
	public DialogEventCollisionPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mShapeComboBoxComponent[0] = new ShapeComboBoxComponent(world);
		mShapeComboBoxComponent[1] = new ShapeComboBoxComponent(world);
		mIntervalTimeComponent = new DialogValueFieldComponent("Collision Detection Interval (sec)");
		mIntervalTimeComponent.setText("1");
		add(mShapeComboBoxComponent[0]);
		add(mShapeComboBoxComponent[1]);
		add(mIntervalTimeComponent);
	}

	public DialogEventCollisionPanel(World world, Event event) {
		this(world);
		String option[] = event.getOptionStrings();
		if (option.length == 3) {
			setNode(0, option[0]);
			setNode(1, option[1]);
			setIntervalTime(option[2]);
		}
	}

	public void setNode(int nNode, String nodeName) {
		if (nodeName == null)
			return;
		JComboBox combo = mShapeComboBoxComponent[nNode];
		for (int n=0; n<combo.getItemCount(); n++) {
			String itemName = (String)combo.getItemAt(n);
			if (itemName.equals(nodeName) == true) {
				combo.setSelectedIndex(n);
				return;
			}
		}
	}

	public void setIntervalTime(double time) {
		mIntervalTimeComponent.setValue(time);
	}

	public void setIntervalTime(String time) {
		try {
			setIntervalTime(Double.parseDouble(time));
		}
		catch (NumberFormatException e) {}
	}
		
	public double getIntervalTime() {
		try {
			return Double.parseDouble(mIntervalTimeComponent.getText());
		}
		catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public String getOptionString() {
			String nodeName1 = (String)mShapeComboBoxComponent[0].getSelectedItem();
			String nodeName2 = (String)mShapeComboBoxComponent[1].getSelectedItem();
			return nodeName1 +  ":" + nodeName2 + ":" + getIntervalTime();
	}

	private class ShapeComboBoxComponent extends JComboBox {
		public ShapeComboBoxComponent(World world) {
			for (ShapeNode shape = world.getSceneGraph().findShapeNode(); shape != null; shape=(ShapeNode)shape.nextTraversalSameType()) {
				String nodeName = shape.getName();
				if (nodeName != null) {
					if (0 < nodeName.length())
						addItem(nodeName);
				}
			}
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Shape")));
		}
	}
}
		
