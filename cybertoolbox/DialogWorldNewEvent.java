/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogWorldNewEvent.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogWorldNewEvent extends Dialog {

	private	JTabbedPane	mTabbedPane;
	private	JPanel		mEventPanel[]	= new JPanel[7];
	
	public DialogWorldNewEvent(Frame parentFrame, World world) {
		super(parentFrame, "New Event");
	
		mTabbedPane = new JTabbedPane();
		
		mEventPanel[0] = new DialogEventTimerPanel(world);
		mEventPanel[1] = new DialogEventClockPanel(world);
		mEventPanel[2] = new DialogEventKeyboardPanel(world);
		mEventPanel[3] = new DialogEventPickupPanel(world);
		mEventPanel[4] = new DialogEventCollisionPanel(world);
		mEventPanel[5] = new DialogEventAreaPanel(world);
		mEventPanel[6] = new DialogEventUserPanel(world);

		mTabbedPane.addTab("Timer",		mEventPanel[0]);
		mTabbedPane.addTab("Clock",		mEventPanel[1]);
		mTabbedPane.addTab("Keyboard",	mEventPanel[2]);
		mTabbedPane.addTab("Pickup",		mEventPanel[3]);
		mTabbedPane.addTab("Collision",	mEventPanel[4]);
		mTabbedPane.addTab("Area",			mEventPanel[5]);
		mTabbedPane.addTab("User",			mEventPanel[6]);

		JComponent dialogComponent[] = new JComponent[1];
		dialogComponent[0] = mTabbedPane;
		setComponents(dialogComponent);
		
		setPreferredSize(new Dimension(360, 280));
	}
	
	public int getSelectedEventType() {
		int index = mTabbedPane.getSelectedIndex();
		switch (index) {
		case 0: return EventType.USER_TIMER;
		case 1: return EventType.USER_CLOCK;
		case 2: return EventType.USER_KEYBOARD;
		case 3: return EventType.USER_PICKUP;
		case 4: return EventType.USER_COLLISION;
		case 5: return EventType.USER_AREA;
		case 6: return EventType.USER_USER;
		}
		return -1;
	}
	
	public DialogEventPanel getSelectedEventPanel() {
		return (DialogEventPanel)mTabbedPane.getSelectedComponent();
	}

	public DialogEventPanel getEventPanel(int eventType) {
		switch (eventType) {
		case EventType.USER_TIMER:			return (DialogEventPanel)mEventPanel[0];
		case EventType.USER_CLOCK:			return (DialogEventPanel)mEventPanel[1];
		case EventType.USER_KEYBOARD:		return (DialogEventPanel)mEventPanel[2];
		case EventType.USER_PICKUP:		return (DialogEventPanel)mEventPanel[3];
		case EventType.USER_COLLISION:	return (DialogEventPanel)mEventPanel[4];
		case EventType.USER_AREA:			return (DialogEventPanel)mEventPanel[5];
		case EventType.USER_USER:			return (DialogEventPanel)mEventPanel[6];
		}
		return null;
	}
}
