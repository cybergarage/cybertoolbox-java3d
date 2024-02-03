/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehaviorGroup.java
*
******************************************************************/

import cv97.node.*;
import cv97.util.*;

public class EventBehaviorGroup extends LinkedList {
	
	private World 			mWorld;
	
	public EventBehaviorGroup(World world) {
		setWorld(world);
	}

	public void setWorld(World world) {
		mWorld = world;
	}
	
	public World getWorld() {
		return mWorld;
	}
	
	public void addEventBehavior(EventBehavior behavior) {
		Thread thread = new Thread(behavior);
		behavior.setThread(thread);
		behavior.setWorld(getWorld());
		addNode(behavior);
	}
	
	public EventBehavior getEventBehaviors() {
		return (EventBehavior)getNodes();
	}

	public EventBehavior getEventBehavior(Event event) {
		for (EventBehavior behavior=getEventBehaviors(); behavior!=null; behavior=behavior.next()) {
			if (behavior.getEvent() == event)
				return behavior;
		}
		return null;
	}

	public EventBehaviorInterpolator getEventBehavior(InterpolatorNode interpNode) {
		for (EventBehavior eb=getEventBehaviors(); eb != null; eb=eb.next()) {
			if (eb instanceof EventBehaviorInterpolator) {
				EventBehaviorInterpolator ebi = (EventBehaviorInterpolator)eb;
				if (ebi.getInterpolatorNode() == interpNode)
					return ebi;
			}
		}
		return null;
	}

	public EventBehavior getEventBehavior(int n) {
		return (EventBehavior)getNode(n);
	}

	public int getNEventBehaviors() {
		return getNNodes();
	}

	public void initializeEventBehavior(EventBehavior behavior) {
		behavior.initialize();
	}

	public void startEventBehavior(EventBehavior behavior) {
		behavior.start();
	}
	
	public void stopEventBehavior(EventBehavior behavior) {
		behavior.stop();
	}

	public void removeEventBehavior(EventBehavior behavior) {
		behavior.stop();
		behavior.remove();
	}
	
	public void initialize() {
		for (EventBehavior behavior=getEventBehaviors(); behavior!=null; behavior=behavior.next()) 
			initializeEventBehavior(behavior);
	}
	
	public void start() {
		for (EventBehavior behavior=getEventBehaviors(); behavior!=null; behavior=behavior.next()) 
			startEventBehavior(behavior);
	}
	
	public void stop() {
		for (EventBehavior behavior=getEventBehaviors(); behavior!=null; behavior=behavior.next()) 
			stopEventBehavior(behavior);
	}

	public void clear() {
		for (EventBehavior behavior=getEventBehaviors(); behavior!=null; behavior=behavior.next()) 
			removeEventBehavior(behavior);
	}
	
}