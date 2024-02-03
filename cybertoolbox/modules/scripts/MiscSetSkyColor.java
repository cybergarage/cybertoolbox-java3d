/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscSetSkyColor.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class MiscSetSkyColor extends Module {

	private SceneGraph			sg;
	private BackgroundNode		bgNode;
	private float					color[];
			
	public void initialize() {
		sg = getSceneGraph();
		
		String nodeName = getStringValue();
		if (nodeName != null)
			bgNode = sg.findBackgroundNode(nodeName);
		else
			bgNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		float value[] = inNode[0].getFloatValues();
		if (value != null && value.length == 3)
			color = value;
			
		if (bgNode != null && color != null) {
			if (0 < bgNode.getNSkyColors()) 
				bgNode.setSkyColor(0, color);
			else
				bgNode.addSkyColor(color);
		}
	}
}

