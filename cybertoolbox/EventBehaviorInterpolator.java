/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehaviorInterpolator.java
*
******************************************************************/

import cv97.*;
import cv97.node.*;

public class EventBehaviorInterpolator extends EventBehavior {
		
	private SceneGraph			sg;
	private Module					interpModule;
	
	private InterpolatorNode	interpNode;
	private float					startPos;
	private float					playStep;
	private long					playInterval;
	private boolean				isCotinuous;
	private boolean				isOscillate;
	
	private float					fractionOffset;
	private boolean				pose;
	
	public EventBehaviorInterpolator(Module module) {
		initializeMember(); 
		interpModule = module;
		sg = module.getSceneGraph();
//		try {
//			String clockTimeString = event.getOptionString();
//			intervalTime = (long)(Double.parseDouble(clockTimeString) * 1000.0);
//		} catch (NumberFormatException nfe) {};
	}

	public InterpolatorNode getInterpolatorNode() {
		return interpNode;
	}
	
	public void initializeMember() {
		interpNode		= null;
		startPos			= 0;
		playStep		= 0;
		playInterval	= -1;
		isCotinuous		= false;
		isOscillate		= false;
	}
	
	public void initialize() {
		initializeMember();
		
		if (interpModule != null) {
			String moduleValue[] = interpModule.getStringValues();
			if (moduleValue != null && moduleValue.length == 6) {
				try {
					interpNode		= sg.findInterpolatorNode(moduleValue[0]);
					startPos			= Float.parseFloat(moduleValue[1]);
					playStep		= Float.parseFloat(moduleValue[2]);
					playInterval	= (long)(Double.parseDouble(moduleValue[3]) * 1000.0);
					isCotinuous		= moduleValue[4].equalsIgnoreCase("true");
					isOscillate		= moduleValue[5].equalsIgnoreCase("true");
				}
				catch (NumberFormatException  nfe) {
					initializeMember();
				}
			}
		}
		
		if (interpNode != null)
			interpNode.setFraction(startPos);
		
		Debug.message("EventBehaviorInterpolator.initialize()");
		Debug.message("\tinterpNode   = " + interpNode);
		Debug.message("\tstartPos     = " + startPos);
		Debug.message("\tplayStep    = " + playStep);
		Debug.message("\tplayInterval = " + playInterval);
		Debug.message("\tisCotinuous  = " + isCotinuous);
		Debug.message("\tisOscillate  = " + isOscillate);
		
		fractionOffset = playStep;
			
		pose = false;
	}

	public void pose() {
		pose = true;
	}

	public boolean isPlaying() {
		return !pose;
	}

	public void replay() {
		pose = false;
	}
	
	public void rewind() {
		if (interpNode != null) {
			interpNode.setFraction(startPos);
			interpNode.update();
		}
	}
		
	private float fraction;
	
	public boolean processStimulus() {
		if (interpNode == null || playInterval < 0)
			return false;
	
		if (pose == false) {
		
			interpNode.update();

			fraction = interpNode.getFraction();
			fraction += fractionOffset;
		
			if (isCotinuous == false) {
				if (fraction < 0.0f || 1.0f < fraction)
					return false;
			}
		
			if (fraction < 0.0) {
				fraction = fraction % 1.0f;
				if (isOscillate == true) {
					fraction = -fraction;
					fractionOffset = -fractionOffset;
				}
				else
					fraction = 1.0f + fraction;
			}
		
			if (1.0 < fraction) {
				fraction = fraction % 1.0f;
				if (isOscillate == true) {
					fraction = 1.0f - fraction;
					fractionOffset = -fractionOffset;
				}
			}
						
			interpNode.setFraction(fraction);
		}
		
		//yield();
		try {
			getThread().sleep(playInterval);
		} catch (InterruptedException e) {}

		return true;
	}
	
}