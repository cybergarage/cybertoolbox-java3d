/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	GeometryGetLength.java
*
----------------------------------------------------------------*/

public class GeometryGetLength extends Module {

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

		double len = 0.0;
		for (int n=0; n<vector.length; n++)
			len += vector[n] * vector[n];
		len = Math.sqrt(len);
		
		sendOutNodeValue(0, len);
	}

}
