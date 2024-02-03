/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	StringSelector.java
*
----------------------------------------------------------------*/

public class StringSelector extends Module {

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == true)
				sendOutNodeValue(0, inNode[0].getStringValue());
			else
				sendOutNodeValue(0, inNode[1].getStringValue());
			return;
		}

		sendOutNodeValue(0, inNode[0].getStringValue());
	}

}
