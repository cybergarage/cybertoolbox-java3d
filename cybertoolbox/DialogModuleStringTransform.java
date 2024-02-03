/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleStringTransform.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleStringTransform extends Dialog {
	
	private DialogTransformFieldComponent mTransformComboBoxComponent = null;
	
	public DialogModuleStringTransform(Frame parentFrame, World world, Module module) {
		super(parentFrame, "Module String::Transform");
		setSize(300, 200);
		
		SceneGraph sg = world.getSceneGraph();
			
		mTransformComboBoxComponent = new DialogTransformFieldComponent(world);
		Node node = sg.findNodeByName(module.getValue());
		if (node != null)
			mTransformComboBoxComponent.setNode(node);
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mTransformComboBoxComponent;
		setComponents(dialogComponent);
	}
	
	public String getNodeValue() {
		Node node = mTransformComboBoxComponent.getNode();
		if (node == null)
			return null;
		return node.getName();
	}
}
		
