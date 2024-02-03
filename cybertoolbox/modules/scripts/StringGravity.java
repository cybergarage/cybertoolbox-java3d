/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	StringValue.java
*
----------------------------------------------------------------*/

public class StringGravity extends Module {

	public void initialize() {
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		sendOutNodeValue(0, 9.8);
	}

}
