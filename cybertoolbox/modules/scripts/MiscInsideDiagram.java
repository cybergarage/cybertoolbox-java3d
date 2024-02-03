/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscInsideDiagram.java
*
----------------------------------------------------------------*/

public class MiscInsideDiagram extends Module {
	
	Diagram	dgm;
	Module	sysInModule;
	String	param[] = new String[4];
		
	public void initialize() {
		dgm				= getInsideDiagram();
		sysInModule		= getSystemInputModule();
		dgm.initialize();
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) {
				for (int n=0; n<inNode.length; n++)
					sendOutNodeValue(n, inNode[n].getStringValue());
				return;
			}
		}
		
		for (int n=0; n<inNode.length; n++)
			param[n] = inNode[n].getStringValue();
		dgm.run(sysInModule, param);
	}
}
