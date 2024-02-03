/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : BillboardNodeObject.java
*
******************************************************************/

package cv97.j3d;

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.*;
import cv97.node.*;
import cv97.field.*;
import cv97.util.*;

public class BillboardNodeObject extends TransformGroup implements NodeObject {

	public BillboardNodeObject(BillboardNode node) {
		setCapability(ALLOW_CHILDREN_READ);
		setCapability(ALLOW_CHILDREN_WRITE);
		setCapability(ALLOW_TRANSFORM_READ);
		setCapability(ALLOW_TRANSFORM_WRITE);
		initialize(node);
	}
	
	public boolean initialize(cv97.node.Node node) {
		update(node);
		return true;
	}

	public boolean uninitialize(cv97.node.Node node) {
		return true;
	}

	private float[] getViewerToBillboardVector(BillboardNode bbNode) {
		SceneGraph sg = bbNode.getSceneGraph();
		if (sg == null) 
			return null;
		
		ViewpointNode view = sg.getViewpointNode();
		if (view == null)
			view = sg.getDefaultViewpointNode();

		float viewPos[] = new float[3];
		view.getPosition(viewPos);

		float bboardPos[] = {0.0f, 0.0f, 0.0f};
	
		cv97.node.Node parentNode = bbNode.getParentNode();
		if (parentNode != null) {
			SFMatrix	mx = new SFMatrix();
			parentNode.getTransformMatrix(mx);
			mx.multi(bboardPos);
		}

		SFVec3f	resultVector = new SFVec3f(bboardPos);
		resultVector.sub(viewPos);
		resultVector.normalize();

		float vector[] = new float[3];
		resultVector.getValue(vector);
		
		return vector;
	}

	private float[] getBillboardToViewerVector(BillboardNode bbNode) {
		float vector[] = getViewerToBillboardVector(bbNode);
		Geometry3D.inverse(vector);
		return vector;
	}

	private float getRotationAngleOfZAxis(BillboardNode bbNode) {
		float	axisOfRotation[] = new float[3];
		bbNode.getAxisOfRotation(axisOfRotation);
		
		float	viewer2bboardVector[] = getViewerToBillboardVector(bbNode);
	
		float	planeVector[]			= Geometry3D.getCross(axisOfRotation, viewer2bboardVector);
		float zAxisVectorOnPlane[]	= Geometry3D.getCross(axisOfRotation, planeVector);
	
		float	zAxisVector[] = {0.0f, 0.0f, 1.0f};
		return Geometry3D.getAngle(zAxisVector, zAxisVectorOnPlane);
	}

	private void getRotationZAxisRotation(BillboardNode bbNode, float roationValue[]) {

		SceneGraph sg = bbNode.getSceneGraph();
		if (sg == null) 
			return;

		float	bboardYAxisVector[] = {0.0f, 1.0f, 0.0f};
		float	bboardZAxisVector[] = {0.0f, 0.0f, 1.0f};

		float bboard2viewerVector[] = getBillboardToViewerVector(bbNode);
		float planeVector[] = Geometry3D.getCross(bboardZAxisVector, bboard2viewerVector);
		float bboardZAxisRotationAngle = Geometry3D.getAngle(bboardZAxisVector, bboard2viewerVector);
	
		SFRotation	zAxisRotation = new SFRotation();
		zAxisRotation.setValue(planeVector, bboardZAxisRotationAngle);
		zAxisRotation.multi(bboardYAxisVector);

		ViewpointNode view = sg.getViewpointNode();
		if (view == null)
			view = sg.getDefaultViewpointNode();

		float viewFrame[][] = new float[3][3];
		
		view.getFrame(viewFrame);
		float bboardYAxisRotationAngle = Geometry3D.getAngle(viewFrame[1], bboardYAxisVector);

		if (viewFrame[1][0] > 0.0f)
			bboardYAxisRotationAngle = (float)Math.PI*2.0f - bboardYAxisRotationAngle;

		SFRotation	yAxisRotation = new SFRotation();
		yAxisRotation.setValue(viewFrame[2], bboardYAxisRotationAngle);

		SFRotation	rotation = new SFRotation();
		rotation.add(yAxisRotation);
		rotation.add(zAxisRotation);
		rotation.getValue(roationValue);
	}
	
	private void getRotation(BillboardNode bbNode, float rotation[]) {
		if (Geometry3D.length(rotation) > 0.0f) {
			bbNode.getAxisOfRotation(rotation);
			rotation[3] = -getRotationAngleOfZAxis(bbNode);
		}
		else 
			getRotationZAxisRotation(bbNode, rotation);
	}
	
	public boolean update(cv97.node.Node node) {
		BillboardNode bbNode = (BillboardNode)node;
		
		float rotation[] = new float[4];
		getRotation(bbNode, rotation);
		
		Transform3D trans3D = new Transform3D();

		getTransform(trans3D);
		 		 
		AxisAngle4f axisAngle = new AxisAngle4f(rotation);
		trans3D.setRotation(axisAngle);
		
		setTransform(trans3D);
	
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
