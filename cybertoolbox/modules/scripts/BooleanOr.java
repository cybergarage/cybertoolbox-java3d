/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	BooleanOr.java
*
----------------------------------------------------------------*/

public class BooleanOr extends Module {

	boolean	value1;
	boolean	value2;
	
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		value1 = inNode[0].getBooleanValue();
		value2 = inNode[1].getBooleanValue();
		sendOutNodeValue(0, (value1 || value2));
	}
}
