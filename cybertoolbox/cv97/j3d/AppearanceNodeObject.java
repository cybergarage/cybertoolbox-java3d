/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : AppearanceNodeObject.java
*
******************************************************************/

package cv97.j3d;

import javax.media.j3d.*;
import javax.vecmath.*;
import cv97.node.*;
import cv97.field.*;

public class AppearanceNodeObject extends Appearance implements NodeObject {
	
	public AppearanceNodeObject(AppearanceNode node) {
		setCapability(ALLOW_POLYGON_ATTRIBUTES_READ);
		setCapability(ALLOW_POLYGON_ATTRIBUTES_WRITE);
		setCapability(ALLOW_MATERIAL_READ);
		setCapability(ALLOW_MATERIAL_WRITE);
		setCapability(ALLOW_TEXTURE_READ);
		setCapability(ALLOW_TEXTURE_WRITE);
		initialize(node);
	}

	public boolean initialize(cv97.node.Node node) {
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCapability(PolygonAttributes.ALLOW_MODE_READ);
		polyAttr.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);
		setPolygonAttributes(polyAttr);
		return true;
	}
	
	public boolean uninitialize(cv97.node.Node node) {
		return true;
	}
			
	public boolean update(cv97.node.Node node) {
		return true;
	}
	
	public boolean add(cv97.node.Node node) {

		cv97.node.Node parentNode = node.getParentNode();
		if (parentNode != null) {
			if (parentNode.isShapeNode() == true) {
				NodeObject parentNodeObject  = parentNode.getObject();
				if (parentNodeObject != null) {
					Shape3D parentShape3DNode = (Shape3D)parentNodeObject;
					parentShape3DNode.setAppearance(this);
				}
			}
		}
		
		return true;
	}

	public boolean remove(cv97.node.Node node) {
	
		cv97.node.Node parentNode = node.getParentNode();
		if (parentNode != null) {
			if (parentNode.isShapeNode() == true) {
				NodeObject parentNodeObject  = parentNode.getObject();
				if (parentNodeObject != null) {
					Shape3D parentShape3DNode = (Shape3D)parentNodeObject;
					parentShape3DNode.setAppearance(new NullAppearanceObject());
				}
			}
		}
		
		return true;
	}
}
