/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterScale.java
*
----------------------------------------------------------------*/

public class FilterMul extends Module {

	private double		scaleValue;
		
	public void initialize() {
		try {
			scaleValue = getDoubleValue();
		}
		catch (NumberFormatException nfe) {
			scaleValue = Double.NaN;
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

		if (scaleValue == Double.NaN) {
			sendOutNodeValue(0, Double.toString(Double.NaN));
			return;
		}
			
		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			for (int n=0; n<value.length; n++)
				value[n] *= scaleValue;
		}
		
		sendOutNodeValue(0, value);
	}
}
