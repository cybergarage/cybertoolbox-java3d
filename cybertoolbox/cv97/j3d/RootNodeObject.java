/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : RootNodeObject.java
*
******************************************************************/

package cv97.j3d;

import javax.media.j3d.*;

import cv97.node.*;

public class RootNodeObject extends BranchGroup implements NodeObject {

	public RootNodeObject(RootNode node) {
		setCapability(ALLOW_BOUNDS_READ);
		setCapability(ALLOW_CHILDREN_READ);
		setCapability(ALLOW_CHILDREN_WRITE);
		setCapability(ALLOW_CHILDREN_EXTEND);
		setCapability(ALLOW_DETACH);
	}
	
	public boolean initialize(cv97.node.Node node) {
		return true;
	}
	
	public boolean uninitialize(cv97.node.Node node) {
		return true;
	}
			
	public boolean update(cv97.node.Node node) {
		return true;
	}
	
	public boolean add(cv97.node.Node node) {
		return true;
	}

	public boolean remove(cv97.node.Node node) {
		return true;
	}
}
