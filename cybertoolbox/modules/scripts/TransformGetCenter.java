/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	TransformGetCenter.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class TransformGetCenter extends Module {

	private SceneGraph		sg;
	private TransformNode	objNode;
	private float				center[] = new float[3];
			
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
			objNode.getCenter(center);
			sendOutNodeValue(0, center);
		}
		else
			sendOutNodeValue(0, "No Transform");
	}

}
