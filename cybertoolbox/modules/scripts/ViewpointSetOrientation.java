/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	ViewpointSetOrientation.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class ViewpointSetOrientation extends Module {

	private SceneGraph		sg;
	private ViewpointNode	viewNode;
	private float				orientation[];

	public void initialize() {
		sg = getSceneGraph();
		
		String viewName = getStringValue();
		if (viewName != null)
			viewNode = sg.findViewpointNode(viewName);
		else
			viewNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (inNode[0].isConnected() == true) {
			String viewName = inNode[0].getStringValue();
			if (viewName != null) {
				viewNode = sg.findViewpointNode(viewName);
				if (viewNode != null) 
					setValue(viewName);
				else
					setValue("");
			}
			else {
				viewNode = null;
				setValue("");
			}
		}

		float value[] = inNode[1].getFloatValues();
		if (value != null && value.length == 4)
			orientation = value;
		else
			orientation = null;			

		if (viewNode != null && orientation != null)
			viewNode.setOrientation(orientation);
	}

}
