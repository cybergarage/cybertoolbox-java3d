/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventAreaPanel.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.field.*;

public class DialogEventAreaPanel extends DialogEventPanel {
	
	private DialogVectorFieldComponent mCenterFieldComponent = null;
	private DialogVectorFieldComponent mSizeFieldComponent = null;
	
	public DialogEventAreaPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		mCenterFieldComponent = new DialogVectorFieldComponent("Center");
		mSizeFieldComponent = new DialogVectorFieldComponent("Size");
		add(mCenterFieldComponent);
		add(mSizeFieldComponent);
	}

	public DialogEventAreaPanel(World world, Event event) {
		this(world);
		double value[] = event.getOptionValues();
		if (value.length == 6) {
			setAreaCenter(value[0], value[1], value[2]);
			setAreaSize(value[3], value[4], value[5]);
		}
	}

	public void setAreaCenter(double x, double y, double z) {
		mCenterFieldComponent.setXValue(x);
		mCenterFieldComponent.setYValue(y);
		mCenterFieldComponent.setZValue(z);
	}
	
	public void setAreaCenter(double center[]) {
		setAreaCenter(center[0], center[1], center[2]);
	}
	
	public void setAreaSize(double x, double y, double z) {
		mSizeFieldComponent.setXValue(x);
		mSizeFieldComponent.setYValue(y);
		mSizeFieldComponent.setZValue(z);
	}
	
	public void setAreaSize(double center[]) {
		setAreaSize(center[0], center[1], center[2]);
	}

	public double[] getAreaCenter() {
		double center[] = new double[3];
		try {
			Double x = new Double(mCenterFieldComponent.getXValue());
			Double y = new Double(mCenterFieldComponent.getYValue());
			Double z = new Double(mCenterFieldComponent.getZValue());
			center[0] = x.doubleValue();
			center[1] = y.doubleValue();
			center[2] = z.doubleValue();
		}
		catch (NumberFormatException e) {
			return null;
		}
		return center;
	}

	public double[] getAreaSize() {
		double size[] = new double[3];
		try {
			Double x = new Double(mSizeFieldComponent.getXValue());
			Double y = new Double(mSizeFieldComponent.getYValue());
			Double z = new Double(mSizeFieldComponent.getZValue());
			size[0] = x.doubleValue();
			size[1] = y.doubleValue();
			size[2] = z.doubleValue();
		}
		catch (NumberFormatException e) {
			return null;
		}
		return size;
	}

	public String getOptionString() {
		double center[] = getAreaCenter();
		double size[]   = getAreaSize();
		if (center == null || size == null)
			return null;
		if (size[0] == 0 || size[1] == 0 || size[2] == 0)
			return null;
		return center[0] + ":" + center[1] + ":" + center[2] + ":" + size[0] + ":" + size[1] + ":" + size[2];
	}
	
}
		
