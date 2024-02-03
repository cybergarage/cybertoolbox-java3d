/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleInterpolatorNodeComboBox.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleInterpolatorNodeComboBox extends JComboBox {

	public DialogModuleInterpolatorNodeComboBox(SceneGraph sg) {
		setMaximumRowCount(2);		
		setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Interpolator")));
		
		for (Node node = sg.getNodes(); node != null; node=node.nextTraversal()) {
			if (node.isInterpolatorNode() == true) {
				String nodeName = node.getName();
				if (nodeName != null) {
					if (0 < nodeName.length())
						addItem(nodeName);
				}
			}
		}
		
		
	}
		
	public void setNode(String nodeName) {
		if (nodeName == null)
			return;
		for (int n=0; n<getItemCount(); n++) {
			String itemName = (String)getItemAt(n);
			if (itemName.equals(nodeName) == true) {
				setSelectedIndex(n);
				return;
			}
		}
	}
	
	public void setNode(Node node) {
		if (node == null)
			return;
		setNode(node.getName());
	}
	
	public String getNode() {
		return (String)getSelectedItem();
	}
}
		
