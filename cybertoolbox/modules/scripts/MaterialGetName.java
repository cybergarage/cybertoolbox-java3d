/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MaterialGetName.java
*
----------------------------------------------------------------*/

public class MaterialGetName extends Module {

	public void initialize() {
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		sendOutNodeValue(0, getValue());
	}

}
