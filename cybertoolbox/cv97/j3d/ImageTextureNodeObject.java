/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ImageTextureNodeObject.java
*
******************************************************************/

package cv97.j3d;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.node.*;
import cv97.field.*;

public class ImageTextureNodeObject extends Texture2D implements NodeObject {
	
	public ImageTextureNodeObject(ImageTextureLoader imgTexLoader) {
		super(BASE_LEVEL, RGBA, imgTexLoader.getWidth(), imgTexLoader.getHeight());

		setCapability(ALLOW_IMAGE_READ);
		setCapability(ALLOW_ENABLE_READ);
		setCapability(ALLOW_ENABLE_WRITE);
		
		setMinFilter(BASE_LEVEL_LINEAR);
		setMagFilter(BASE_LEVEL_LINEAR);
		
		setImage(0, imgTexLoader.getImageComponent());
		setEnable(true);
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

		cv97.node.Node parentNode = node.getParentNode();
		if (parentNode != null) {
			if (parentNode.isAppearanceNode() == true) {
				NodeObject parentNodeObject  = parentNode.getObject();
				if (parentNodeObject != null) {
					Appearance parentAppearanceNode = (Appearance)parentNodeObject;
					parentAppearanceNode.setTexture(this);
				}
			}
		}
		
		return true;
	}

	public boolean remove(cv97.node.Node node) {
	
		cv97.node.Node parentNode = node.getParentNode();
		if (parentNode != null) {
			if (parentNode.isAppearanceNode() == true) {
				NodeObject parentNodeObject  = parentNode.getObject();
				if (parentNodeObject != null) {
					Appearance parentAppearanceNode = (Appearance)parentNodeObject;
					parentAppearanceNode.setTexture(null);
				}
			}
		}
		
		return true;
	}
}
