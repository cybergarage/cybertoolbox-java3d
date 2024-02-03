/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	TransformSetTranslation.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class TransformSetTranslation extends Module {

	private SceneGraph		sg;
	private TransformNode	objNode;
	private float				translation[];
			
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
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

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

		float value[] = inNode[1].getFloatValues();
		if (value != null && value.length == 3)
			translation = value;
		else
			translation = null;			

		if (objNode != null && translation != null)
			objNode.setTranslation(translation);
	}
}
