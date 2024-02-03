/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : InlineNodeObject.java
*
******************************************************************/

package cv97.j3d;

import java.io.*;
import java.net.*;

import javax.media.j3d.*;

import cv97.*;
import cv97.node.*;
import cv97.field.*;
import cv97.util.Debug;

public class InlineNodeObject extends GroupNodeObject implements NodeObject {

	public InlineNodeObject(InlineNode node) {
		initialize(node);
	}
	
	public boolean initialize(cv97.node.Node node) {
	
		Debug.message("InlineNodeObject.initialize");
	
		removeAllChildren();
		
		SceneGraph sg = node.getSceneGraph();
		if (sg == null)
			return false;
		
		SceneGraphJ3dObject sgObject = (SceneGraphJ3dObject)sg.getObject();
		if (sgObject == null)
			return false;
			
		InlineNode inlineNode = (InlineNode)node;
		
		if (inlineNode.getNURLs() <= 0)
			return false;
			
		String urlName = inlineNode.getURL(0);
		
		if (urlName == null)
			return true;
			
		SceneGraph sgLoad = new SceneGraph();
		SceneGraphJ3dObject sgObjectLoad = new SceneGraphJ3dObject(sgLoad);
		sgLoad.setObject(sgObjectLoad);
		
		if (sgLoad.load(urlName) == false) {
			URL baseURL = sg.getBaseURL();
			if (baseURL != null) {
				try {
					if (sgLoad.load(new URL(baseURL.toString() + urlName)) == false) {
						Debug.message("\tLoading is Bad !!");
						return false;
					}
				} catch (MalformedURLException mue) {
					return false;
				}
			}
		}
		
		Debug.message("\tLoading is OK !!");
		
		sgLoad.initialize();
		inlineNode.setBoundingBoxCenter(sgLoad.getBoundingBoxCenter());
		inlineNode.setBoundingBoxSize(sgLoad.getBoundingBoxSize());
		
		int nLoadNodes = sgLoad.getNNodes();
		Debug.message("\tLoadNodes = " + nLoadNodes);
		if (nLoadNodes <= 0)
			return true;
			
		BranchGroup branchGroup = sgObjectLoad.getBranchGroup();
		Debug.message("\taddChild = " + branchGroup);
		addChild(branchGroup);

		return true;
	}

	public boolean uninitialize(cv97.node.Node node) {
		return true;
	}
			
	public boolean update(cv97.node.Node node) {
		return true;
	}
}
