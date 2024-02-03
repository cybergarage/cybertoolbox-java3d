/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventClockPanel.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.field.*;

public class DialogEventClockPanel extends DialogEventPanel {
	
	private DialogValueFieldComponent mValueFieldComponent = null;
	
	public DialogEventClockPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mValueFieldComponent = new DialogValueFieldComponent("Interval time (sec)");
		mValueFieldComponent.setText("1");
		add(mValueFieldComponent);
	}

	public DialogEventClockPanel(World world, Event event) {
		this(world);
		try {
			setIntervalTime(event.getOptionValue());
		}
		catch (NumberFormatException nfe) {}
	}
	
	public void setIntervalTime(double time) {
		mValueFieldComponent.setValue(time);
	}
		
	public double getIntervalTime() {
		try {
			return Double.parseDouble(mValueFieldComponent.getText());
		}
		catch (NumberFormatException e) {
			return -1;
		}
	}

	public String getOptionString() {
		double time = getIntervalTime();
		if (time < 0) 
			return null;
		return Double.toString(time);
	}
}
		
