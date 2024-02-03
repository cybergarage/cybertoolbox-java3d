/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscGetScreenInfo.java
*
----------------------------------------------------------------*/

import java.applet.Applet;
import java.awt.Dimension;

public class MiscGetScreenSize extends Module {

	Applet	applet = null;
	
	public void initialize() {
		World world = getWorld();
		if (world != null) {
			if (0 < world.getNApplets())
				applet = world.getApplet(0);
		}
		else
			applet = null;
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (applet != null) {
			Dimension dim = applet.getSize();
			int size[] = new int[2];
			size[0] = dim.width;
			size[1] = dim.height;
			sendOutNodeValue(0, size);
		}
	}

}
