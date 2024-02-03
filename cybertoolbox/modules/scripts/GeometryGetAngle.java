/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryGetAngle.java
*
----------------------------------------------------------------*/

public class GeometryGetAngle extends Module {

	private double	vector1[];
	private double	vector2[];
		
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
		
		if (vector1.length != vector2.length) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}

		if (vector1.length != 2 || vector1.length != 3) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}

		double mag1 = 0.0;
		for (int n=0; n<vector1.length; n++)
			mag1 += vector1[n] + vector1[n];
		mag1 = Math.sqrt(mag1);

		double mag2 = 0.0;
		for (int n=0; n<vector2.length; n++)
			mag2 += vector2[n] + vector2[n];
		mag2 = Math.sqrt(mag2);

		double dot = 0.0;
		for (int n=0; n<vector1.length; n++)
			dot += vector1[n] * vector2[n];			

		double angle = Math.acos(dot / (mag1 * mag2));

		sendOutNodeValue(0, angle);
	}
}
