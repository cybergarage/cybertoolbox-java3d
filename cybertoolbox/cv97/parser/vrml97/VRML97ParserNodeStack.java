/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : VRML97ParserNodeStack.java
*
******************************************************************/

package cv97.parser.vrml97;

import cv97.*;
import cv97.node.*;
import cv97.util.*;

public class VRML97ParserNodeStack extends LinkedListNode {
	private Node	mNode;
	private int		mType;

	public VRML97ParserNodeStack(Node node, int type) { 
		setHeaderFlag(false); 
		mNode = node; 
		mType = type;
	}
	
	Node getObject() { 
		return mNode; 
	}
	
	int getType() { 
		return mType; 
	}
};
