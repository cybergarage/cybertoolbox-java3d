/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	LightSetDirection.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class LightSetDirection extends Module {

	private SceneGraph		sg;
	private LightNode	lightNode;
	private float				direction[];
			
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

		float value[] = inNode[1].getFloatValues();
		if (value != null && value.length == 3)
			direction = value;
		else
			direction = null;			

		if (lightNode != null && direction != null) {
			if (lightNode.isDirectionalLightNode() == true) 
				((DirectionalLightNode)lightNode).setDirection(direction);
			else if (lightNode.isSpotLightNode() == true) 
				((SpotLightNode)lightNode).setDirection(direction);
		}
	}

}
