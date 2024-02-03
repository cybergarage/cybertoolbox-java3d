/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SystemMouse.java
*
----------------------------------------------------------------*/

import java.applet.*;

public class SystemMouse extends Module {

	private 	int 			coordFormat;
	private	CtbApplet	ctbApplet;
	private	int			width, height;
	private	int			smx, smy;
	
	public void initialize() {
		ctbApplet = null;
		World world = getWorld();
		
		if (world != null) {
			if (0 < world.getNApplets())
				ctbApplet = world.getApplet(0);
		}

		coordFormat = World.MOUSE_FRAME_COORDINATE;
		String value = getStringValue();
		if (value == null)
			return;
		if (value.equalsIgnoreCase(World.MOUSE_FRAME_COORDINATE_STRING) == true)
			coordFormat = World.MOUSE_FRAME_COORDINATE;
		if (value.equalsIgnoreCase(World.MOUSE_NORMALIZED_COORDINATE_STRING) == true)
			coordFormat = World.MOUSE_NORMALIZED_COORDINATE;
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {	
	}

	public double []parseMousePosition(String value) {
		DoubleValueTokenizer tokenizer = new DoubleValueTokenizer(value);
		return tokenizer.getValues();
	}

	public void sendOutNodeValue(int n, String value) {

		if (n==1) {
			if (coordFormat == World.MOUSE_NORMALIZED_COORDINATE) {
				if (ctbApplet != null) {
					width		= ctbApplet.getWidth();
					height	= ctbApplet.getHeight();
					smx		= ctbApplet.getStartMouseX();
					smy		= ctbApplet.getStartMouseY();
					double mpos[] = parseMousePosition(value);
					value = ((float)mpos[0] / (float)width)+ "," + ((float)mpos[1] / (float)height);
				}
			}
		}
		
		super.setOutNodeValue(n, value);
		super.sendOutNodeValue(n);
	}

}
