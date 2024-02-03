/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	ViewpointGetOrientation.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class ViewpointGetOrientation extends Module {

	private SceneGraph		sg;
	private ViewpointNode	viewNode;
	private float				ori[] = new float[4];

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
			viewNode.getOrientation(ori);
			sendOutNodeValue(0, ori);
		}
		else
			sendOutNodeValue(0, "No Viewpoint");
	}
}
