/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SensorMouse.java
*
----------------------------------------------------------------*/

import java.awt.event.*;

public class SensorMouse extends Module {

	CtbApplet		ctbApplet = null;
	int				button;
	int				x, y;
	String			buttonString[]	= {"NONE", "BUTTON1", "BUTTON2", "BUTTON3"};
	String			posString		= null;
	 	
	public void initialize() {
		ctbApplet = null;
		World world = getWorld();
		if (world != null) {
			if (0 < world.getNApplets())
				ctbApplet = world.getApplet(0);
		}
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		button = ctbApplet.getMouseButton();
		if (button == MouseEvent.BUTTON1_MASK)
			sendOutNodeValue(0, buttonString[1]);
		else if (button == MouseEvent.BUTTON2_MASK)
			sendOutNodeValue(0, buttonString[2]);
		else if (button == MouseEvent.BUTTON3_MASK)
			sendOutNodeValue(0, buttonString[3]);
		else
			sendOutNodeValue(0, buttonString[0]);

		x = ctbApplet.getMouseX();
		y = ctbApplet.getMouseY();
		posString = x + "," + y;
		sendOutNodeValue(1, posString);
	}

}
