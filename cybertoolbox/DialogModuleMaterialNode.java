/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleMaterialNode.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleMaterialNode extends DialogModuleNode {
	
	public DialogModuleMaterialNode(Frame parentFrame, World world, MaterialNode node) {
		super(parentFrame, "Module Material");
		
		setSize(300, 200);
		setWorld(world);
		
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = createNodeComboBox("Material", world.getSceneGraph().findMaterialNode());
		setComponents(dialogComponent);
		
		setNode(node);
	}
	
	public Node getNode() {
		String nodeName = (String)getComboBox().getSelectedItem();
		return getWorld().getSceneGraph().findMaterialNode(nodeName);
	}
}
		
