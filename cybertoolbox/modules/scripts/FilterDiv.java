/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterScale.java
*
----------------------------------------------------------------*/

public class FilterDiv extends Module {

	private double		divValue;
		
	public void initialize() {
		try {
			divValue = getDoubleValue();
		}
		catch (NumberFormatException nfe) {
			divValue = Double.NaN;
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

		if (divValue == Double.NaN) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			for (int n=0; n<value.length; n++)
				value[n] /= divValue;
		}
		
		sendOutNodeValue(0, value);
	}
}
