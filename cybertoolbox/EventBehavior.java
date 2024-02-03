/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehavior.java
*
******************************************************************/

import java.awt.*;

import cv97.*;
import cv97.util.*;

public abstract class EventBehavior extends LinkedListNode implements Runnable {

	private Thread 	mThread;
	private World		mWorld;
//	private Component	mUpdateComponent[] = new Component[0];
	private Event		mEvent;
		
	public EventBehavior() {
		setHeaderFlag(false);
		setThread(null);
		setWorld(null);
//		setUpdateComponents(new Component[0]); 
		setEvent(null); 
	}

	public void setWorld(World world) {
		mWorld = world;
	}
	
	public World getWorld() {
		return mWorld;
	}

/*
	public void setUpdateComponents(Component comp[]) {
		synchronized (mUpdateComponent) {
			if (comp != null)
				mUpdateComponent = comp;
			else 
				mUpdateComponent = new Component[0];
		}
	}

	public void addUpdateComponent(Component comp) {
		synchronized (mUpdateComponent) {
			int nUpdateComponents = 0;
			if (mUpdateComponent != null)
				nUpdateComponents = mUpdateComponent.length;
	
			Component newUpdateComponent[] = new Component[nUpdateComponents+1];
			for (int n=0; n<nUpdateComponents; n++)
				newUpdateComponent[n] = mUpdateComponent[n];
			newUpdateComponent[(nUpdateComponents+1)-1] = comp;
		
			mUpdateComponent = newUpdateComponent;
		}
	}
	
	public Component[] getUpdateComponents() {
		Component comp[] = null;
		synchronized (mUpdateComponent) {
			comp = mUpdateComponent;
		}
		return comp;
	}
*/

	public void setEvent(Event event) {
		mEvent = event;
	}
	
	public Event getEvent() {
		return mEvent;
	}

	public SceneGraph getSceneGraph() {
		return mWorld.getSceneGraph();
	}

	public EventBehavior next () {
		return (EventBehavior)getNextNode();
	}

	public abstract void initialize();
	
	public abstract boolean processStimulus();
	
	public void run() {
		while (getWorld().isSimulationRunning() == true) {
			boolean exit = processStimulus();
			/*
			Component comp[] = getUpdateComponents();
			for (int n=0; n< comp.length; n++) {
				if (comp[n] != null)
					comp[n].repaint();				
			}
			*/
			if (exit == false)
				break;
		}
		remove();
	}

	public void setThread(Thread thread) {
		mThread = thread;
	}

	public Thread getThread() {
		return mThread;
	}

	public void start() {
		Thread thread = getThread();
		if (thread != null) 
			thread.start();
	}
	
	public void stop() {
		Thread thread = getThread();
		if (thread != null) {
			thread.interrupt();
			thread.stop();
			setThread(null);
		}
	}
}