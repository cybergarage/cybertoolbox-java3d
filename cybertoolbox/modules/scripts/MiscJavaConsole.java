/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscJavaConsole.java
*
----------------------------------------------------------------*/

public class MiscJavaConsole extends Module {

	public void initialize() {
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		System.out.println(inNode[0].getStringValue());
	}

}
