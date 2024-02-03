/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscGetArray.java
*
----------------------------------------------------------------*/

import java.util.*;

public class MiscGetArray extends Module {
	
	private String name[] = new String[2];
	
	public void initialize() {
		name[0] = null;
		name[1] = null;
		
		String value[] = getStringValues();
		if (value != null && value.length == 2) {
			name[0] = value[0];
			name[1] = value[1];
		}
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (inNode[0].isConnected() == true) {
			name[0] = inNode[0].getStringValue();
			setValue(name);
		}

		if (inNode[1].isConnected() == true) {
			name[1] = inNode[1].getStringValue();
			setValue(name);
		}
			
		if (name[0] == null || name[1] == null)
			return;
			
		GlobalData gdata = getWorld().getGlobalData(name[0], name[1]);
		
		if (gdata != null)
			sendOutNodeValue(0, gdata.getValue());
	}
}
