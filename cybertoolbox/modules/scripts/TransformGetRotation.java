/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	TransformGetRotation.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class TransformGetRotation extends Module {

	private SceneGraph		sg;
	private TransformNode	objNode;
	private float				rotation[] = new float[4];
			
	public void initialize() {
		sg = getSceneGraph();
		
		String objName = getStringValue();
		if (objName != null)
			objNode = sg.findTransformNode(objName);
		else
			objNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (inNode[0].isConnected() == true) {
			String objName = inNode[0].getStringValue();
			if (objName != null) {
				objNode = sg.findTransformNode(objName);
				if (objNode != null) 
					setValue(objName);
				else
					setValue("");
			}
			else {
				objNode = null;
				setValue("");
			}
		}

		if (objNode != null) {
			objNode.getRotation(rotation);
			sendOutNodeValue(0, rotation);
		}
		else
			sendOutNodeValue(0, "No Transform");
	}
}
