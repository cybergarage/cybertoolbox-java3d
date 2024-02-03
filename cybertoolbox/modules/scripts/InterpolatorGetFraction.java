/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	InterpolatorGetFraction.java
*
----------------------------------------------------------------*/

import java.util.*;

import cv97.*;
import cv97.node.*;

public class InterpolatorGetFraction extends Module {
	
	private SceneGraph			sg;
	private EventBehaviorGroup	ebg;
	private InterpolatorNode	interpNode;
	private float					step;
	private float					fraction;
	
	public void initialize() {
		sg		= getSceneGraph();
		ebg	= getWorld().getEventBehaviorGroup();
				
		String interpName = getValue();
		if (interpName != null)
			interpNode = sg.findInterpolatorNode(interpName);
		else
			interpNode = null;
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (inNode[0].isConnected() == true) {
			String interpName = inNode[0].getStringValue();
			if (interpName != null) {
				interpNode = sg.findInterpolatorNode(interpName);
				if (interpNode != null) 
					setValue(interpName);
				else
					setValue("");
			}
			else {
				interpNode = null;
				setValue("");
			}
		}

		if (interpNode != null) 
			sendOutNodeValue(0, interpNode.getFraction());
		else
			sendOutNodeValue(0, "No Interpolator");
		
	}
}

