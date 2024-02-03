/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	InterpolatorGetName.java
*
----------------------------------------------------------------*/

public class InterpolatorGetName extends Module {

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		sendOutNodeValue(0, getValue());
	}

}
