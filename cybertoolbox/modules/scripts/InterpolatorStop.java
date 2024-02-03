/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	InterpolatorStop.java
*
----------------------------------------------------------------*/

import java.util.*;

import cv97.*;
import cv97.node.*;

public class InterpolatorStop extends Module {
	
	private SceneGraph			sg;
	private EventBehaviorGroup	ebg;
	private InterpolatorNode	interpNode;

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
		else
			return;

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

		if (interpNode == null)
			return;
			
		EventBehaviorInterpolator eb = ebg.getEventBehavior(interpNode);
		
		if (eb == null)
			return;
			
		eb.stop();
		eb.rewind();
		eb.remove();
	}
}

