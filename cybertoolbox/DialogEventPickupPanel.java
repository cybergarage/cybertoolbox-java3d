/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventPickupPanel.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogEventPickupPanel extends DialogEventPanel {
	
	private ShapeComboBoxComponent mShapeComboBoxComponent = null;
	
	public DialogEventPickupPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mShapeComboBoxComponent = new ShapeComboBoxComponent(world);
		add(mShapeComboBoxComponent);
	}

	public DialogEventPickupPanel(World world, Event event) {
		this(world);
		setNode(event.getOptionString());
	}

	public void setNode(String nodeName) {
		if (nodeName == null)
			return;
		JComboBox combo = mShapeComboBoxComponent;
		for (int n=0; n<combo.getItemCount(); n++) {
			String itemName = (String)combo.getItemAt(n);
			if (itemName.equals(nodeName) == true) {
				combo.setSelectedIndex(n);
				return;
			}
		}
	}
	
	public String getOptionString() {
			return (String)mShapeComboBoxComponent.getSelectedItem();
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
		
