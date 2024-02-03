/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MathPow.java
*
----------------------------------------------------------------*/

public class MathPow extends Module {

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

		try {
			double value1 = inNode[0].getDoubleValue();
			double value2 = inNode[1].getDoubleValue();
			sendOutNodeValue(0, Math.pow(value1, value2));
		}
		catch (NumberFormatException nfe) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
		}
		
	}

}
