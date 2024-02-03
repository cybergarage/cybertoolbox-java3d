/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	ViewpointGetPosition.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class ViewpointGetPosition extends Module {

	private SceneGraph		sg;
	private ViewpointNode	viewNode;
	private float				pos[] = new float[3];

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

		if (viewNode != null) {
			viewNode.getPosition(pos);
			sendOutNodeValue(0, pos);
		}
		else
			sendOutNodeValue(0, "No Viewpoint");
	}
}
