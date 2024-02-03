/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	LightGetDirection.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class LightGetDirection extends Module {

	private SceneGraph		sg;
	private LightNode			lightNode;
	private float				direction[] = new float[3];
			
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
			if (lightNode.isDirectionalLightNode() == true) {
				((DirectionalLightNode)lightNode).getDirection(direction);
				sendOutNodeValue(0, direction);
			}
			else if (lightNode.isSpotLightNode() == true) {
				((SpotLightNode)lightNode).getDirection(direction);
				sendOutNodeValue(0, direction);
			}
			else
				sendOutNodeValue(0, "No Light");
		}
		else
			sendOutNodeValue(0, "No Light");
	}

}
