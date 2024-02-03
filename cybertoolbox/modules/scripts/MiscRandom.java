/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscRandom.java
*
----------------------------------------------------------------*/

import java.util.*;

public class MiscRandom extends Module {
	
	public void initialize() {
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		sendOutNodeValue(0, Math.random());
	}

}
