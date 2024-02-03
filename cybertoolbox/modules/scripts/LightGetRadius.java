/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	LightGetRadius.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class LightGetRadius extends Module {

	private SceneGraph		sg;
	private LightNode			lightNode;
			
	public void initialize() {
		sg = getSceneGraph();
		
		String lightName = getStringValue();
		if (lightName != null)
			lightNode = sg.findLightNode(lightName);
		else
			lightNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (inNode[0].isConnected() == true) {
			String lightName = inNode[0].getStringValue();
			if (lightName != null) {
				lightNode = sg.findLightNode(lightName);
				if (lightNode != null) 
					setValue(lightName);
				else
					setValue("");
			}
			else {
				lightNode = null;
				setValue("");
			}
		}

		if (lightNode != null) {
			if (lightNode.isPointLightNode() == true) 
				sendOutNodeValue(0, ((PointLightNode)lightNode).getRadius());
			else if (lightNode.isSpotLightNode() == true) 
				sendOutNodeValue(0, ((SpotLightNode)lightNode).getRadius());
			else
				sendOutNodeValue(0, "No Light");
		}
		else
			sendOutNodeValue(0, "No Light");
	}

}
