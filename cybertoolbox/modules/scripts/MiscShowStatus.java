/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscShowStatus.java
*
----------------------------------------------------------------*/

import java.applet.*;
import java.net.*;

public class MiscShowStatus extends Module {

	Applet			applet = null;
	AppletContext	appletContext;
	String			docStatus;
	
	public void initialize() {
		Debug.message("MiscShowStatus.initialize()");
		
		applet = null;
		appletContext = null;

		World world = getWorld();
		if (world != null) {
			if (0 < world.getNApplets())
				applet = world.getApplet(0);
		}
		Debug.message("  applet = " + applet);
		
		if (applet != null) {
			try {
				appletContext = applet.getAppletContext();		
			}
			catch (Exception e) {};
		}
		Debug.message("  appletContext = " + appletContext);
		
		String docStatus = getStringValue();
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		Debug.message("MiscShowStatus.processData()");
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (inNode[0].isConnected() == true)
			docStatus = inNode[0].getStringValue();

		Debug.message("  appletContext = " + appletContext);
		Debug.message("  docStatus     = " + docStatus);
		
		if (appletContext == null)
			return;

		if (docStatus == null)
			return;
			
		appletContext.showStatus(docStatus);
	}
	
}
