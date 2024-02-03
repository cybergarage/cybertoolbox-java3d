/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEvent.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class DialogEvent extends Dialog {
	
	private DialogEventPanel mEventPanel = null;
	
	public DialogEvent(Frame parentFrame, Event event) {
		super(parentFrame, event.getName());
		World world = event.getWorld();
		switch (event.getEventTypeNumber()) {
		case EventType.USER_CLOCK:
			mEventPanel = new DialogEventClockPanel(world, event);
			break;
		case EventType.USER_TIMER:
			mEventPanel = new DialogEventTimerPanel(world, event);
			break;
		case EventType.USER_PICKUP:
			mEventPanel = new DialogEventPickupPanel(world, event);
			break;
		case EventType.USER_KEYBOARD:
			mEventPanel = new DialogEventKeyboardPanel(world, event);
			break;
		case EventType.USER_AREA:
			mEventPanel = new DialogEventAreaPanel(world, event);
			break;
		case EventType.USER_COLLISION:
			mEventPanel = new DialogEventCollisionPanel(world, event);
			break;
		case EventType.USER_USER:
			mEventPanel = new DialogEventUserPanel(world, event);
			break;
		default:
			Debug.warning("DialogEvent.<init>");
			Debug.warning("\tInvalidate event type (" + event + ")");
			break;
		}
		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mEventPanel;
		setComponents(dialogComponent);
	}
	
	public DialogEventPanel getEventPanel() {
		return mEventPanel;
	}
	
	public String getOptionString() {
		return getEventPanel().getOptionString();
	}
}
		
