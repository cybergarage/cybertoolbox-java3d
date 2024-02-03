/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MaterialSetTransparency.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class MaterialSetTransparency extends Module {

	private SceneGraph		sg;
	private MaterialNode	matNode;
	private float				transp;
			
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

		try {
			transp = inNode[1].getFloatValue();
		}
		catch (NumberFormatException nfe) {
			transp = -1.0f;
		};

		if (matNode != null && 0.0 <= transp) 
			matNode.setTransparency(transp);
	}
}
