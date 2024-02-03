/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleTransformNode.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleTransformNode extends DialogModuleNode {
	
	private DialogTransformFieldComponent mTransformComboBoxComponent = null;
	
	public DialogModuleTransformNode(Frame parentFrame, World world, TransformNode node) {
		super(parentFrame, "Module Transform");
		setSize(300, 200);
		
		mTransformComboBoxComponent = new DialogTransformFieldComponent(world);
		mTransformComboBoxComponent.setNode(node);
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mTransformComboBoxComponent;
		setComponents(dialogComponent);
	}
	
	public Node getNode() {
		return mTransformComboBoxComponent.getNode();
	}
}
		
