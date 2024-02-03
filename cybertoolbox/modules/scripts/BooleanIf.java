/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	BooleanIf.java
*
----------------------------------------------------------------*/

public class BooleanIf extends Module {

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (inNode[0].getBooleanValue() == true) {
			sendOutNodeValue(0, true);
			sendOutNodeValue(1, false);
		}
		else {
			sendOutNodeValue(0, false);
			sendOutNodeValue(1, true);
		}
	}

}
