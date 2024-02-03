/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	StringDivide2Values.java
*
----------------------------------------------------------------*/

public class StringDivide2Values extends Module {
	
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		String value[] = inNode[0].getStringValues();
		if (value != null && 2 <= value.length) {
			sendOutNodeValue(0, value[0]);
			sendOutNodeValue(1, value[1]);
		} 
	}
}
