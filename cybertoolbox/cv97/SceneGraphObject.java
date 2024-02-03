/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : SceneGraphObject.java
*
******************************************************************/

package cv97;

import cv97.node.*;

public interface SceneGraphObject 
{
	public boolean		initialize(SceneGraph sg);
	public boolean		uninitialize(SceneGraph sg);

	public NodeObject	createNodeObject(SceneGraph sg, cv97.node.Node node);
	public boolean		addNode(SceneGraph sg, Node node);
	public boolean		removeNode(SceneGraph sg, Node node);
	
	public boolean		update(SceneGraph sg);
	public boolean		remove(SceneGraph sg);

	public boolean		start(SceneGraph sg);
	public boolean		stop(SceneGraph sg);
	
	public boolean		setRenderingMode(SceneGraph sg, int mode);
}
