/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	NumericXor.java
*
----------------------------------------------------------------*/

public class NumericXor extends Module {

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

		int inValue1[] = inNode[0].getIntegerValues();
		int inValue2[] = inNode[1].getIntegerValues();
		
		if (inValue1 != null && inValue2 != null) {
			if (inValue1.length == inValue2.length) {
				StringBuffer outValue = new StringBuffer();
				for (int n=0; n<inValue1.length; n++) {
					int value = inValue1[n] ^ inValue2[n];
					outValue.append(value);
					if (n < (inValue1.length - 1))
						outValue.append(',');
				}
				sendOutNodeValue(0, outValue.toString());
			}
			else
				sendOutNodeValue(0, Double.toString(Double.NaN));
		}
		else
			sendOutNodeValue(0, Double.toString(Double.NaN));
	}
	
}
