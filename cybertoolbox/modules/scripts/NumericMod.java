/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	NumericMod.java
*
----------------------------------------------------------------*/

public class NumericMod extends Module {

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

		double inValue1[] = inNode[0].getDoubleValues();
		double inValue2;
		try {
			inValue2 = inNode[1].getDoubleValue();
		}
		catch (NumberFormatException nfe) {
			inValue2 = 0.0f;
		}

		if (inValue1 != null && inValue2 != 0.0) {
			StringBuffer outValue = new StringBuffer();
			for (int n=0; n<inValue1.length; n++) {
				double value = inValue1[n] % inValue2;
				outValue.append(value);
				if (n < (inValue1.length - 1))
					outValue.append(',');
			}
			sendOutNodeValue(0, outValue.toString());
		}
		else
			sendOutNodeValue(0, Double.toString(Double.NaN));
	}
	
}
