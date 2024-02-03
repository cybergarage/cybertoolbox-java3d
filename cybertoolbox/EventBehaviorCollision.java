/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehaviorCollision.java
*
******************************************************************/

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.*;
import cv97.node.*;
import cv97.j3d.*;
import cv97.util.*;
import cv97.node.Node;

public class EventBehaviorCollision extends EventBehavior {
		
	private Event		collisionEvent;
	private ShapeNode	shapeNode[] = new ShapeNode[2];
	private long		intervalTime;
	private boolean 	isIntersected;

	public EventBehaviorCollision(Event event) {
		setEvent(event);
		collisionEvent = event;
	}
	
	public void initialize() {
		SceneGraph sg = getSceneGraph();
		
		String option[] = collisionEvent.getOptionStrings();
		if (option.length == 3) {
			shapeNode[0] = sg.findShapeNode(option[0]);
			shapeNode[1] = sg.findShapeNode(option[1]);
			try {
				intervalTime = (long)(Double.parseDouble(option[2]) * 1000.0);
			}
			catch (NumberFormatException e) {
				intervalTime = -1;
			}
		}
		else {
			shapeNode[0] = null;
			shapeNode[1] = null;
			intervalTime = -1;
		}
		
		isIntersected = false;
		
		Debug.message("EventBehaviorCollision.initialize");
		Debug.message("\toption[0]    = " +  option[0]);
		Debug.message("\toption[1]    = " +  option[1]);
		Debug.message("\toption[2]    = " +  option[2]);
		Debug.message("\tshapeNode[0] = " +  shapeNode[0]);
		Debug.message("\tshapeNode[1] = " +  shapeNode[1]);
		Debug.message("\tintervalTime = " +  intervalTime);
	}

	private TransformNodeObject	transGroup		= new TransformNodeObject();
	private Transform3D				trans3d			= new Transform3D();
	private Matrix4f					trans3dMatrix	= new Matrix4f();
	private Matrix4f					matrix			= new Matrix4f();
	
	private void getShapeTransform3D(ShapeNode node, Transform3D nodeTrans3d) {
		matrix.setIdentity();
		
		Node parentNode = node.getParentNode();
		while (parentNode != null) {
			if (parentNode.isTransformNode() == true) {
				transGroup.update(parentNode);
				transGroup.getTransform(trans3d);
				trans3d.get(trans3dMatrix);
				
				if (true) {
					trans3dMatrix.mul(matrix);
					matrix.set(trans3dMatrix);
				}
				else {
					matrix.mul(trans3dMatrix);
				}
			}
			parentNode = parentNode.getParentNode();
		}
		
		nodeTrans3d.set(matrix);
	}

	private Bounds getShapeBounds(ShapeNode node) {
		Shape3D shape3d = (Shape3D)node.getObject();
		return shape3d.getBounds();
	}
		
	private Transform3D	shapeTrans3d[]	= {new Transform3D(), new Transform3D()};
	private Bounds			shapeBounds[]	= new Bounds[2];
	
	public boolean processStimulus() {
		//Debug.message("EventBehaviorCollision.processStimulus");
		
		//Debug.message("\tcollisionEvent = " + collisionEvent);
		if (collisionEvent == null)
			return false;

		//Debug.message("\tshapeNode[0] = " + shapeNode[0]);
		//Debug.message("\tshapeNode[1] = " + shapeNode[1]);
		if (shapeNode[0] == null || shapeNode[1] == null)
			return false;
		
		//Debug.message("\tintervalTime = " + intervalTime);
		if (intervalTime < 0)
			return false;
	
		for (int n=0; n<2; n++) {
			getShapeTransform3D(shapeNode[n], shapeTrans3d[n]);
			shapeBounds[n] = getShapeBounds(shapeNode[n]);
			shapeBounds[n].transform(shapeTrans3d[n]);
		}
		
		boolean intersect = shapeBounds[0].intersect(shapeBounds[1]);

		//Debug.message("\tintersect     = " +  intersect);
		//Debug.message("\tisIntersected = " +  isIntersected);
		
		if (isIntersected == false) {
			if (intersect == true) {
				isIntersected = true;
				collisionEvent.run("true");
			}
		}
		else {
			if (intersect == false) {
				isIntersected = false;
				collisionEvent.run("false");
			}
		}
		
		//yield();
		try {
			getThread().sleep(intervalTime);
		} catch (InterruptedException e) {}
		
		
		return true;
	}
	
}