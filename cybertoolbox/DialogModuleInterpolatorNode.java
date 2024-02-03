/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleInterpolatorNode.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleInterpolatorNode extends DialogModuleNode {

	private DialogModuleInterpolatorNodeComboBox	mInterpNodeCombo;
	private SceneGraph									sg;
	
	public DialogModuleInterpolatorNode(Frame parentFrame, Module module) {
		super(parentFrame, "Module Interpolator");
		
		sg = module.getSceneGraph();
		mInterpNodeCombo	= new DialogModuleInterpolatorNodeComboBox(sg);

		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mInterpNodeCombo;
		setComponents(dialogComponent);
		
		mInterpNodeCombo.setNode(module.getValue());
	}

	public Node getNode() {
		String nodeName = (String)mInterpNodeCombo.getSelectedItem();
		return sg.findInterpolatorNode(nodeName);
	}
}
		
