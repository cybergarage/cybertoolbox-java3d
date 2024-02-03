/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryInverse.java
*
----------------------------------------------------------------*/

public class GeometryInverse extends Module {

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
		
		for (int n=0; n<vector.length; n++)
			vector[n] = -vector[n];
		
		sendOutNodeValue(0, vector);
	}

}
