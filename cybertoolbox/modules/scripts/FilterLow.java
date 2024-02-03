/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterLow.java
*
----------------------------------------------------------------*/

public class FilterLow extends Module {

	private double		lowValue;
		
	public void initialize() {
		try {
			lowValue = getDoubleValue();
		}
		catch (NumberFormatException nfe) {
			lowValue = Double.NaN;
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

		if (lowValue == Double.NaN) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			for (int n=0; n<value.length; n++) {
				if (value[n] < lowValue)
					value[n] = lowValue;
			}
		}
		
		sendOutNodeValue(0, value);
	}

}
