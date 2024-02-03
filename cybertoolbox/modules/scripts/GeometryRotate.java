/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryRotate.java
*
----------------------------------------------------------------*/

import javax.vecmath.*;

public class GeometryRotate extends Module {

	private double			vector[];
	private double			rotation[];
	private Vector3d		vec3d = new Vector3d();
	private AxisAngle4d	axisAngle = new AxisAngle4d();
	private Matrix3d		matrix3d = new Matrix3d();
	
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		vector = inNode[0].getDoubleValues();
		rotation = inNode[1].getDoubleValues();
		
		if (vector == null || rotation == null) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}

		if (rotation.length != 4) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
						
		if (vector.length != 3) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		vec3d.x = vector[0];
		vec3d.y = vector[1];
		vec3d.z = vector[2];
		
		axisAngle.set(rotation);
		matrix3d.setIdentity();
		matrix3d.set(axisAngle);
		
		matrix3d.transform(vec3d);
		
		vector[0] = vec3d.x;
		vector[1] = vec3d.y;
		vector[2] = vec3d.z;
		
		sendOutNodeValue(0, vector);
	}				
}
