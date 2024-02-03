/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	BooleanNot.java
*
----------------------------------------------------------------*/

public class BooleanNot extends Module {

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (inNode[0].getBooleanValue() == true)
			sendOutNodeValue(0, false);
		else 
			sendOutNodeValue(0, true);
	}
}
