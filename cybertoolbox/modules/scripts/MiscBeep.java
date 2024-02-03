/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscBeep.java
*
----------------------------------------------------------------*/
import java.awt.Toolkit;

public class MiscBeep extends Module {

	public void initialize() {
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == true) 
				Toolkit.getDefaultToolkit().beep();
		}
	}

}
