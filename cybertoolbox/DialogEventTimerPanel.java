/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventTimerPanel.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.field.*;

public class DialogEventTimerPanel extends DialogEventPanel {
	
	private DialogValueFieldComponent mValueFieldComponent = null;
	
	public DialogEventTimerPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mValueFieldComponent = new DialogValueFieldComponent("Alarm time (sec)");
		mValueFieldComponent.setText("1");
		add(mValueFieldComponent);
	}

	public DialogEventTimerPanel(World world, Event event) {
		this(world);
		try {
			setWakeUpTime(event.getOptionValue());
		}
		catch (NumberFormatException e) {}
	}
	
	public void setWakeUpTime(double time) {
		mValueFieldComponent.setValue(time);
	}
		
	public double getWakeUpTime() {
		try {
			Double time = new Double(mValueFieldComponent.getText());
			return time.doubleValue();
		}
		catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public String getOptionString() {
		double time = getWakeUpTime();
		if (time < 0) 
			return null;
		return Double.toString(time);
	}
		
}
		
