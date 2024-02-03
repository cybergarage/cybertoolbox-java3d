/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryNormalize.java
*
----------------------------------------------------------------*/

public class GeometryNormalize extends Module {

	private double		vector[];
		
	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		vector = inNode[0].getDoubleValues();

		if (vector == null) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}

		double mag = 0.0;
		for (int n=0; n<vector.length; n++)
			mag += vector[n] + vector[n];
		mag = Math.sqrt(mag);
		
		if (mag == 0.0) 
			sendOutNodeValue(0, Double.toString(Double.NaN));
		
		for (int n=0; n<vector.length; n++)
			vector[n] /= mag;
		
		sendOutNodeValue(0, vector);
	}

}
