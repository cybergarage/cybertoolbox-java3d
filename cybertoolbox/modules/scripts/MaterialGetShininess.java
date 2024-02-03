/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MaterialGetShininess.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;

public class MaterialGetShininess extends Module {

	private SceneGraph		sg;
	private MaterialNode	matNode;
			
	public void initialize() {
		sg = getSceneGraph();
		
		String matName = getStringValue();
		if (matName != null)
			matNode = sg.findMaterialNode(matName);
		else
			matNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (inNode[0].isConnected() == true) {
			String matName = inNode[0].getStringValue();
			if (matName != null) {
				matNode = sg.findMaterialNode(matName);
				if (matNode != null) 
					setValue(matName);
				else
					setValue("");
			}
			else {
				matNode = null;
				setValue("");
			}
		}

		if (matNode != null) 
			sendOutNodeValue(0, matNode.getShininess());
		else
			sendOutNodeValue(0, "No Material");
	}
}
