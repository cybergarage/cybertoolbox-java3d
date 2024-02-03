/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehaviorClock.java
*
******************************************************************/

import cv97.*;
import cv97.node.*;

public class EventBehaviorArea extends EventBehavior {
		
	private Event	areaEvent;
	private float	center[];
	private float	size[];
	private float	vpos[]	= new float[3];

	public EventBehaviorArea(Event event) {
		setEvent(event);
		areaEvent = event;

		double value[] = event.getOptionValues();
		if (value != null && value.length == 6) {
			center = new float[3];
			center[0] = (float)value[0];
			center[1] = (float)value[1];
			center[2] = (float)value[2];
			
			size = new float[3];
			size[0] = (float)value[3];
			size[1] = (float)value[4];
			size[2] = (float)value[5];
		}
		else {
			center = null;
			size = null;
		}
	}

	////////////////////////////////////////////////
	//	 inRegion
	////////////////////////////////////////////////
	
	private boolean mInRegion = false;
	
	public void setInRegion(boolean value) {
		mInRegion = value;
	}

	public boolean inRegion() {
		return mInRegion;
	} 

	public boolean isRegion(float vpos[], float center[], float size[]) {
		for (int n=0; n<3; n++) {
			if (vpos[n] < center[n] - size[n]/2.0f)
				return false;
			if (center[n] + size[n]/2.0f < vpos[n])
				return false;
		}
		return true;
	}

	////////////////////////////////////////////////
	//	 initialize / processStimulus
	////////////////////////////////////////////////
	
	public void initialize() {
		setInRegion(false);
	}
	
	public boolean processStimulus() {
		if (center == null || size == null)
			return false;

		SceneGraph sg = getSceneGraph();
		if (sg == null)
			return false;

		ViewpointNode view = sg.getViewpointNode();
		if (view == null)
			view = sg.getDefaultViewpointNode();

		view.getPosition(vpos);
		
		if (inRegion() == false) {
			if (isRegion(vpos, center, size) == true) {
				setInRegion(true);
				areaEvent.run("true");
			}
		}
		else {
			if (isRegion(vpos, center, size) == false) {
				setInRegion(false);
				areaEvent.run("false");
			}
		}
		
		//yield();
		try {
			getThread().sleep(200);
		} catch (InterruptedException e) {}
			
		return true;
	}
	
}