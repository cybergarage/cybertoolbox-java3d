/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscGetData.java
*
----------------------------------------------------------------*/

import java.util.*;

public class MiscGetData extends Module {
	
	private String dataName = null;
	
	public void initialize() {
		dataName = getStringValue();
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (inNode[0].isConnected() == true) {
			dataName = inNode[0].getStringValue();
			if (dataName != null) 
				setValue(dataName);
			else
				setValue("");
		}
			
		if (dataName != null) {
			GlobalData gdata = getWorld().getGlobalData(null, dataName);
			if (gdata != null)
				sendOutNodeValue(0, gdata.getValue());
		}
	}
}
