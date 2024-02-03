/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : RootNode.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import cv97.*;
import cv97.util.*;

public class RootNode extends Node {

	public RootNode() {
		setHeaderFlag(true);
		setType(rootTypeName);
		setSceneGraph(null);
	}
	
	public RootNode(SceneGraph sg) {
		setHeaderFlag(true);
		setType(rootTypeName);
		setSceneGraph(sg);
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		if (node.isCommonNode() || node.isBindableNode() ||node.isInterpolatorNode() || node.isSensorNode() || node.isGroupingNode() || node.isSpecialGroupNode())
			return true;
		else
			return false;
	}

	////////////////////////////////////////////////
	//	Add children 
	////////////////////////////////////////////////
	
	public void addChildNode(Node node) {
		moveChildNode(node);
		node.initialize();
	}

	public void addChildNodeAtFirst(Node node) {
		moveChildNodeAtFirst(node);
		node.initialize();
	}

	////////////////////////////////////////////////
	//	Move children 
	////////////////////////////////////////////////

	public void moveChildNode(Node node) {
		super.moveChildNode(node);
		node.setParentNode(null);
	}

	public void moveChildNodeAtFirst(Node node) {
		super.moveChildNodeAtFirst(node);
		node.setParentNode(null);
	}

	////////////////////////////////////////////////
	//	abstruct functions 
	////////////////////////////////////////////////
	
	public void initialize() {
	}

	public void uninitialize() {
	}

	public void update() {
	}

	////////////////////////////////////////////////
	//	infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
	}
}
