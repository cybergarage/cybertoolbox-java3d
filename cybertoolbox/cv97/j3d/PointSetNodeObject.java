/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : PointSetNodeObject.java
*
******************************************************************/

package cv97.j3d;

import javax.media.j3d.*;
import javax.vecmath.*;
import cv97.node.*;
import cv97.field.*;

public class PointSetNodeObject extends IndexedPointArray implements NodeObject {
	
	public PointSetNodeObject(PointSetNode node) {
		super(getVertexCount(node), getVertexFormat(node), getVertexCount(node));
		setCapability(ALLOW_INTERSECT);
		initialize(node);
	}

	static public int getVertexCount(PointSetNode node) {
		return (node.getCoordinateNodes() != null ? node.getCoordinateNodes().getNPoints() : 0);
	}
	
	static public int getVertexFormat(PointSetNode node) {
		int vertexFormat = COORDINATES;// | NORMALS;
		if (node.getColorNodes() != null)
			vertexFormat |= COLOR_3;
		return vertexFormat;
	}
	
	private void disableLightInMaterialNode(cv97.node.Node node) {
		cv97.node.Node parentNode = node.getParentNode();
		if (parentNode != null) {
			if (parentNode.isShapeNode() == true) {
				NodeObject parentNodeObject  = parentNode.getObject();
				if (parentNodeObject != null) {
					Shape3D parentShape3DNode = (Shape3D)parentNodeObject;
					Appearance app = parentShape3DNode.getAppearance();
					if (app != null) {
						Material material = app.getMaterial();
						if (material != null) {
							material.setLightingEnable(false);
						}
					}
				}
			}
		}
	}
	
	public boolean initialize(cv97.node.Node node) {
		PointSetNode pointSetNode = (PointSetNode)node;
		
		/**** Coordinate *********************************************/
		CoordinateNode coordNode = pointSetNode.getCoordinateNodes();
		if (coordNode != null) {
			int	nCoordinatePoints = coordNode.getNPoints();	
			float point[] = new float[3];
			for (int n=0; n<nCoordinatePoints; n++) {
				coordNode.getPoint(n, point);
				setCoordinate(n, point);
			}
			for (int n=0; n<nCoordinatePoints; n++)
				setCoordinateIndex(n, n);
		}
		
		/**** Color *********************************************/
		ColorNode colorNode = pointSetNode.getColorNodes();
		if (colorNode != null) {
			int	nColors = colorNode.getNColors();	
			float color[] = new float[3];
			for (int n=0; n<nColors; n++) {
				colorNode.getColor(n, color);
				setColor(n, color);
			}
			for (int n=0; n<nColors; n++)
				setColorIndex(n, n);
		}
		
		disableLightInMaterialNode(node);
		
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
					parentShape3DNode.setGeometry(this);
					disableLightInMaterialNode(node);
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
					parentShape3DNode.setGeometry(null);
				}
			}
		}
		
		return true;
	}
}
