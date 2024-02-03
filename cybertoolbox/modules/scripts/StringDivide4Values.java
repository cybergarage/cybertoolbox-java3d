/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	StringDivide4Values.java
*
----------------------------------------------------------------*/

public class StringDivide4Values extends Module {
	
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		String value[] = inNode[0].getStringValues();
		if (value != null && 4 <= value.length) {
			sendOutNodeValue(0, value[0]);
			sendOutNodeValue(1, value[1]);
			sendOutNodeValue(2, value[2]);
			sendOutNodeValue(3, value[3]);
		} 
	}
}
