/******************************************************************
*
*	Simple VRML Viewer for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File:	PerspectiveViewJava3DApplet.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.File;
import java.net.*;

import javax.swing.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.*;
import cv97.node.*;
import cv97.j3d.*;

public class PerspectiveViewJava3DApplet extends CtbApplet {

	private CtbIDE	mCtbIDE;
	
	public void setCtbIDE(CtbIDE ctbIDE) {
		mCtbIDE = ctbIDE;
	}
	
	public CtbIDE getCtbIDE() {
		return mCtbIDE;
	}
	
	public PerspectiveViewJava3DApplet(CtbIDE ctbIDE) {
		setCtbIDE(ctbIDE);
	}

	////////////////////////////////////////////////
	//	 Override 
	////////////////////////////////////////////////

	public void sendMouseEvent(boolean button, int x, int y) {
		if (getWorld().isSimulationRunning() == false)
			return;
			
		super.sendMouseEvent(button, x, y);
		
		Event mouseEvent = getWorld().getSystemEvent("Mouse");
		if (mouseEvent == null)
			return;
		getCtbIDE().updateDiagramFrames(mouseEvent);
	}

	public void sendPickupEvent(boolean button, ShapeNode shapeNode, int offsetx, int offsety) {
		if (getWorld().isSimulationRunning() == false)
			return;
			
		super.sendPickupEvent(button, shapeNode, offsetx, offsety);
			
		String shapeName = shapeNode.getName();
		
		for (Event event=getWorld().getEvents(); event != null; event=event.next()) {
			int eventTypeNumber = event.getEventTypeNumber();
			if (eventTypeNumber != EventType.SYSTEM_PICKUP && eventTypeNumber != EventType.USER_PICKUP)
				continue;
			if (eventTypeNumber == EventType.USER_PICKUP) {
				String optionString = event.getOptionString();
				if (optionString.equals(shapeName) == false)
					continue;
			}
			getCtbIDE().updateDiagramFrames(event);
		}
	}

	public void sendKeyboardEvent(boolean pressed, String keyText) {
		if (getWorld().isSimulationRunning() == false)
			return;

		super.sendKeyboardEvent(pressed, keyText);
				
		for (Event event=getWorld().getEvents(); event != null; event=event.next()) {
			int eventTypeNumber = event.getEventTypeNumber();
			if (eventTypeNumber != EventType.SYSTEM_KEYBOARD && eventTypeNumber != EventType.USER_KEYBOARD)
				continue;
			if (eventTypeNumber == EventType.USER_KEYBOARD) {
				String optionString = event.getOptionString();
				if (optionString.equals(keyText) == false)
					continue;
			}
			getCtbIDE().updateDiagramFrames(event);
		}
	}

}	
