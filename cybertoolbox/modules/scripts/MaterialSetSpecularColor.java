/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MaterialSetSpecularColor.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class MaterialSetSpecularColor extends Module {

	private SceneGraph		sg;
	private MaterialNode	matNode;
	private float				color[];
			
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

		float value[] = inNode[1].getFloatValues();
		if (value != null && value.length == 3)
			color = value;
		else
			color = null;			

		if (matNode != null && color != null)
			matNode.setSpecularColor(color);
	}

}
