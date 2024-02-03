/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryGetDot.java
*
----------------------------------------------------------------*/

public class GeometryGetDot extends Module {

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

		double dot = 0.0;
		for (int n=0; n<vector1.length; n++)
			dot += vector1[n] * vector2[n];			
		
		sendOutNodeValue(0, dot);
	}				
}
