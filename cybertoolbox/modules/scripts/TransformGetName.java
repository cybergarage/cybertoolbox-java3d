/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	TransformGetName.java
*
----------------------------------------------------------------*/

public class TransformGetName extends Module {

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		sendOutNodeValue(0, getValue());
	}

}
