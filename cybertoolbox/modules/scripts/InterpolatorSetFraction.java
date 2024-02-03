/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	InterpolatorSetFraction.java
*
----------------------------------------------------------------*/

import java.util.*;

import cv97.*;
import cv97.node.*;

public class InterpolatorSetFraction extends Module {
	
	private SceneGraph			sg;
	private EventBehaviorGroup	ebg;
	private InterpolatorNode	interpNode;
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
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

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

		fraction = Float.NaN;
		if (inNode[1].isConnected() == true) {
			try {
				fraction = inNode[1].getFloatValue();
			}
			catch (NumberFormatException nfe) {}
		}

		if (interpNode == null)
			return;
		
		if (fraction == Float.NaN)
			return;
		
		if (1.0f < fraction)
			fraction = 1.0f;
		
		if (fraction < 0.0f)
			fraction = 0.0f;
				
		interpNode.setFraction(fraction);
		interpNode.update();		
	}

}

