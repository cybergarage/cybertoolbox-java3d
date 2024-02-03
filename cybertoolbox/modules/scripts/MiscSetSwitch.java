/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscSetSwitch.java
*
----------------------------------------------------------------*/

import cv97.*;
import cv97.node.*;
import cv97.field.*;

public class MiscSetSwitch extends Module {

	private SceneGraph		sg;
	private SwitchNode		switchNode;
	private int					whichChoice;
			
	public void initialize() {
		sg = getSceneGraph();
		
		String nodeName = getStringValue();
		if (nodeName != null)
			switchNode = sg.findSwitchNode(nodeName);
		else
			switchNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		try {
			whichChoice = inNode[0].getIntegerValue();
		}
		catch (NumberFormatException nfe) {
			whichChoice = -1;
		}

		if (switchNode != null) {
			if (0 < whichChoice)
				switchNode.setWhichChoice(whichChoice);
			else
				switchNode.setWhichChoice(-1);
		}
	}

}
