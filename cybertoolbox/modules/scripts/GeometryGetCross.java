/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryGetCross.java
*
----------------------------------------------------------------*/

public class GeometryGetCross extends Module {

	private double	vector1[];
	private double	vector2[];
	private double cross[] = new double[3];
		
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		vector1 = inNode[0].getDoubleValues();
		vector2 = inNode[1].getDoubleValues();

		if (vector1 == null || vector2 == null) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		if (vector1.length != 3 || vector2.length != 3) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}

		cross[0] = vector1[1]*vector2[2] - vector1[2]*vector2[1];
		cross[1] = vector1[2]*vector2[0] - vector1[0]*vector2[2];
		cross[2] = vector1[0]*vector2[1] - vector1[1]*vector2[0];

		sendOutNodeValue(0, cross);
	}

}
