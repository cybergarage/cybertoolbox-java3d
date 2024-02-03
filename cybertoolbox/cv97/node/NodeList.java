/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : NodeList.java
*
******************************************************************/

package cv97.node;

import cv97.util.*;

public class NodeList extends LinkedList {

	public NodeList() {
		RootNode rootNode = new RootNode();
		setRootNode(rootNode);
	}

	public void removeNodes() {
		LinkedListNode rootNode = getRootNode();
		while (rootNode.getNextNode() != null) {
			Node nextNode = (Node)rootNode.getNextNode();
			nextNode.remove();
		}
	}
}