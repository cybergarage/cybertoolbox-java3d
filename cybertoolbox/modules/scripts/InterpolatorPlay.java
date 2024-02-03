/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	InterpolatorPlay.java
*
----------------------------------------------------------------*/

import java.util.*;

import cv97.*;
import cv97.node.*;

public class InterpolatorPlay extends Module {
	
	private SceneGraph			sg;
	private EventBehaviorGroup	ebg;
	private InterpolatorNode	interpNode;

	public void initialize() {
		Debug.message("InterpolatorPlay.initialize");
		
		sg		= getSceneGraph();
		ebg	= getWorld().getEventBehaviorGroup();
				
		String interpName = getNodeName();
		if (interpName != null)
			interpNode = sg.findInterpolatorNode(interpName);
		else
			interpNode = null;
			
		Debug.message("\tmoduleValue = " + getValue());
		Debug.message("\tinterpName  = " + interpName);
		Debug.message("\tinterpNode  = " + interpNode);
	}

	public void shutdown() {
	}

	public void setNodeName(String nodeName) {
		String moduleValue[] = getStringValues();
		if (moduleValue != null && moduleValue.length == 6) {
			moduleValue[0] = nodeName;
			setValue(moduleValue);
		}
		else
			setValue("");
	}

	public String getNodeName() {
		String moduleValue[] = getStringValues();
		if (moduleValue == null || moduleValue.length != 6) 
			return null;
		return moduleValue[0];
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
					setNodeName(interpName);
				else
					setNodeName("");
			}
			else {
				interpNode = null;
				setNodeName("");
			}
		}

		if (interpNode == null)
			return;
			
		EventBehaviorInterpolator eb = ebg.getEventBehavior(interpNode);
		
		if (eb != null) {
			eb.replay();
			return;
		}
			
		eb = new EventBehaviorInterpolator(this);
		ebg.addEventBehavior(eb);
		eb.initialize();
		eb.start();
	}
}


