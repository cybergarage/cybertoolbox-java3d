/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscSetText.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class MiscSetText extends Module {

	private SceneGraph		sg;
	private TextNode			textNode;
			
	public void initialize() {
		sg = getSceneGraph();
		
		String nodeName = getStringValue();
		if (nodeName != null)
			textNode = sg.findTextNode(nodeName);
		else
			textNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		String textString = inNode[0].getStringValue();
		if (textString == null)
			textString = new String();

		if (textNode != null && textString != null) {
			if (0 < textNode.getNStrings()) 
				textNode.setString(0, textString);
			else
				textNode.addString(textString);
		}
	}

}
