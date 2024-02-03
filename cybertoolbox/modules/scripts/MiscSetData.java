/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscSetData.java
*
----------------------------------------------------------------*/

import java.util.*;

public class MiscSetData extends Module {
	
	private String dataName = null;
	private String dataValue = null;

	public void initialize() {
		dataName = getStringValue();
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (inNode[0].isConnected() == true) {
			dataName = inNode[0].getStringValue();
			if (dataName != null) 
				setValue(dataName);
			else
				setValue("");
		}

		dataValue = inNode[1].getStringValue();			

		if (dataName != null) 
			getWorld().setGlobalData(null, dataName, dataValue);
	}
}
