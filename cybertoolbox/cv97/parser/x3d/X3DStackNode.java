/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : X3DStackNode.java
*
******************************************************************/

package cv97.parser.x3d;

import cv97.*;
import cv97.node.*;
import cv97.util.*;

public class X3DStackNode extends LinkedListNode {
	private Node	mNode;

	public X3DStackNode(Node node) { 
		setHeaderFlag(false); 
		mNode = node; 
	}
	
	Node getObject() { 
		return mNode; 
	}
};
