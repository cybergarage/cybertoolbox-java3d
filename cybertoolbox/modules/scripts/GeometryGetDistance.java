/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryGetDistance.java
*
----------------------------------------------------------------*/

public class GeometryGetDistance extends Module {

	private double	point1[];
	private double	point2[];
		
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		point1 = inNode[0].getDoubleValues();
		point2 = inNode[1].getDoubleValues();
		
		if (point1 == null || point2 == null) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		if (point1.length != point2.length) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}

		double vector[] = new double[point1.length];
		
		for (int n=0; n<point1.length; n++)
			vector[n] = point2[n] - point1[n];			

		double mag = 0.0;
		for (int n=0; n<vector.length; n++)
			mag += vector[n] * vector[n];			
		mag = Math.sqrt(mag);
		
		sendOutNodeValue(0, mag);
	}				
}
