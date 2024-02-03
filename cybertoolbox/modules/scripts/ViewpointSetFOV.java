/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	ViewpointSetFOV.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class ViewpointSetFOV extends Module {

	private SceneGraph		sg;
	private ViewpointNode	viewNode;
	private float				fov;

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

		try {
			fov = inNode[1].getFloatValue();
		}
		catch (NumberFormatException nfe) {
			fov = 0.0f;
		}
					
		if (viewNode != null && 0.0 < fov) 
			viewNode.setFieldOfView(fov);
	}

}
