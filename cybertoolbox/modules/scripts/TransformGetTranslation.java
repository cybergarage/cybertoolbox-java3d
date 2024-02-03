/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	TransformGetTranslation.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class TransformGetTranslation extends Module {

	private SceneGraph		sg;
	private TransformNode	objNode;
	private float				translation[] = new float[3];
			
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
			objNode.getTranslation(translation);
			sendOutNodeValue(0, translation);
		}
		else
			sendOutNodeValue(0, "No Transform");
	}
}
