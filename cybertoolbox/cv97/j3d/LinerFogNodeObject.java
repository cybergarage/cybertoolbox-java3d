/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : LinerFogNodeObject.java
*
******************************************************************/

package cv97.j3d;

import javax.media.j3d.*;
import javax.vecmath.*;
import cv97.node.*;
import cv97.field.*;

public class LinerFogNodeObject extends LinearFog implements NodeObject {

	private static BoundingSphere mInfiniteBounds	= new BoundingSphere(new Point3d(), Double.MAX_VALUE);
	private static BoundingSphere mZeroBounds			= new BoundingSphere(new Point3d(), -1.0);

	public LinerFogNodeObject(FogNode node) {
		setCapability(ALLOW_COLOR_READ);
		setCapability(ALLOW_COLOR_WRITE);
		setCapability(ALLOW_INFLUENCING_BOUNDS_READ);
		setCapability(ALLOW_INFLUENCING_BOUNDS_WRITE);
		setCapability(ALLOW_SCOPE_READ);
		setCapability(ALLOW_SCOPE_WRITE);
		setCapability(ALLOW_DISTANCE_READ);
		setCapability(ALLOW_DISTANCE_WRITE);
		initialize(node);
	}
	
	public boolean initialize(cv97.node.Node node) {
		update(node);
		return true;
	}

	public boolean uninitialize(cv97.node.Node node) {
		return true;
	}
			
	public boolean update(cv97.node.Node node) {
		FogNode fogNode = (FogNode)node;
		
		float color[] = new float[3];
		fogNode.getColor(color);
		setColor(color[0], color[1], color[2]);
		
		float range = fogNode.getVisibilityRange();
		setBackDistance(range);
		setFrontDistance(range/10.0);
		
		if (range <= 0.0f)
			setInfluencingBounds(mZeroBounds);
		else
			setInfluencingBounds(mInfiniteBounds);
		 		 
		return true;
	}

	public Group getParentGroup(cv97.node.Node node) {
		cv97.node.Node	parentNode		= node.getParentNode();
		Group			parentGroupNode	= null;
		if (parentNode != null) {
			NodeObject parentNodeObject  = parentNode.getObject();
			if (parentNodeObject != null)
				parentGroupNode = (Group)parentNodeObject;
		}
		else {
			cv97.SceneGraph sg = node.getSceneGraph();
			if (sg != null) {
				SceneGraphJ3dObject	sgObject = (SceneGraphJ3dObject)sg.getObject();
				if (sgObject != null)
					parentGroupNode = sgObject.getRootNode();
			}
		}
		return parentGroupNode;
	}
	
	public boolean add(cv97.node.Node node) {
		if (getParent() == null) {
			Group parentGroupNode = getParentGroup(node);
			if (parentGroupNode != null) {
					parentGroupNode.addChild(this);
			}
		}
		return true;
	}

	public boolean remove(cv97.node.Node node) {
		Group parentGroupNode = getParentGroup(node);
		if (parentGroupNode != null) {
			for (int n=0; n<parentGroupNode.numChildren(); n++) {
				if (parentGroupNode.getChild(n) == this) {
					parentGroupNode.removeChild(n);
					return true;
				}
			}
		}
		return false;
	}
}
