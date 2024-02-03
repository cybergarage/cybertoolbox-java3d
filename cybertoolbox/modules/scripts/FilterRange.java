/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterRange.java
*
----------------------------------------------------------------*/

public class FilterRange extends Module {

	private double		rangeValue[];
		
	public void initialize() {
		try {
			rangeValue = getDoubleValues();
			if (rangeValue != null) {
				if (rangeValue.length != 2)
					rangeValue = null;
			}
		}
		catch (NumberFormatException nfe) {
			rangeValue = null;
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

		if (rangeValue == null) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			for (int n=0; n<value.length; n++) {
				if (value[n] < rangeValue[0])
					value[n] = rangeValue[0];
				else if (rangeValue[1] < value[n])
					value[n] = rangeValue[1];
			}
		}
		
		sendOutNodeValue(0, value);
	}

}
