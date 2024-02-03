/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	NumericMulti.java
*
----------------------------------------------------------------*/

import javax.vecmath.*;

public class NumericMul extends Module {

	private AxisAngle4d	axisAngle[]		= {new AxisAngle4d(), new AxisAngle4d()};
	private Matrix3d		matrix[]			= {new Matrix3d(), new Matrix3d()};
	private Vector3d		vector			= new Vector3d();
	private double			rotVector[]		= new double[3];
	private double			mulRotation[]	= new double[4];
		
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) {
				sendOutNodeValue(0, inNode[0].getStringValue());
				return;
			}
		}

		double inValue1[] = inNode[0].getDoubleValues();
		double inValue2[] = inNode[1].getDoubleValues();

		if (inValue1 == null || inValue1 == null) {	
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
		
		if (inValue1.length < inValue2.length) {
			double tempValue[] = inValue1;
			inValue1 = inValue2;
			inValue2 = tempValue;
		}
		
		if (inValue1.length == 4 && inValue2.length == 4) {
			axisAngle[0].set(inValue1);
			axisAngle[1].set(inValue2);
			matrix[0].set(axisAngle[0]);
			matrix[1].set(axisAngle[1]);
			matrix[0].mul(matrix[1]);
			axisAngle[0].set(matrix[0]);
			axisAngle[0].get(mulRotation);
			sendOutNodeValue(0, mulRotation);
		}
		else if (inValue1.length == 4 && inValue2.length == 3) {
			axisAngle[0].set(inValue1);
			vector.set(inValue2);
			matrix[0].set(axisAngle[0]);
			matrix[0].transform(vector);
			vector.get(rotVector);
			sendOutNodeValue(0, rotVector);
		}
		else if (inValue2.length == 1) {
			StringBuffer outValue = new StringBuffer();
			for (int n=0; n<inValue1.length; n++) {
				double value = inValue1[n] * inValue2[0];
				outValue.append(value);
				if (n < (inValue1.length - 1))
					outValue.append(',');
			}
			sendOutNodeValue(0, outValue.toString());
		}
		else
			sendOutNodeValue(0, Double.toString(Double.NaN));
			
	}
	
}
