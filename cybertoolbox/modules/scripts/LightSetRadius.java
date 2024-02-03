/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	LightSetRadius.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class LightSetRadius extends Module {

	private SceneGraph	sg;
	private LightNode		lightNode;
	private float			radius;
			
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

		try {
			radius = inNode[1].getFloatValue();
		}
		catch (NumberFormatException nfe) {
			radius = -1.0f;
		};

		if (lightNode != null && 0.0f <= radius) {
			if (lightNode.isPointLightNode() == true) 
				((PointLightNode)lightNode).setRadius(radius);
			else if (lightNode.isSpotLightNode() == true) 
				((SpotLightNode)lightNode).setRadius(radius);
		}
	}
}
