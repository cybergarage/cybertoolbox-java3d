/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterHigh.java
*
----------------------------------------------------------------*/

public class FilterHigh extends Module {

	private double		highValue;
		
	public void initialize() {
		try {
			highValue = getDoubleValue();
		}
		catch (NumberFormatException nfe) {
			highValue = Double.NaN;
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

		if (highValue == Double.NaN) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			for (int n=0; n<value.length; n++) {
				if (highValue < value[n])
					value[n] = highValue;
			}
		}
		
		sendOutNodeValue(0, value);
	}
}
