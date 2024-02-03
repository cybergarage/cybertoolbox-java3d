/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterOffset.java
*
----------------------------------------------------------------*/

public class FilterOffset extends Module {

	private double		offsetValue;
		
	public void initialize() {
		try {
			offsetValue = getDoubleValue();
		}
		catch (NumberFormatException nfe) {
			offsetValue = Double.NaN;
		}
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

		if (offsetValue == Double.NaN) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
		
		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			for (int n=0; n<value.length; n++)
				value[n] += offsetValue;
		}
		
		sendOutNodeValue(0, value);
	}

}
