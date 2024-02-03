/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SystemOutput.java
*
----------------------------------------------------------------*/

public class SystemOutput extends Module {

	Diagram	dgm;
	Module	insideModule;
		
	public void initialize() {
		dgm				= getDiagram();
		Debug.message("Inside Diagram = " + dgm);
		insideModule	= dgm.getParentModule();
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		for (int n=0; n<inNode.length; n++)
			insideModule.sendOutNodeValue(n, inNode[n].getStringValue());
	}

}
