/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	BooleanNotEqual.java
*
----------------------------------------------------------------*/

public class BooleanNotEqual extends Module {

	private boolean	result;

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		String inValueString1 = inNode[0].getStringValue();
		String inValueString2 = inNode[1].getStringValue();

		if (inValueString1 == null && inValueString2 == null) { 
			sendOutNodeValue(0, false);
			return;
		}

		if (inValueString1 == null || inValueString2 == null) { 
			sendOutNodeValue(0, true);
			return;
		}
		
		if (inValueString1.equals(inValueString2) == true) {
			sendOutNodeValue(0, false);
			return;
		}
		
		double inValue1[] = inNode[0].getDoubleValues();
		double inValue2[] = inNode[1].getDoubleValues();
			
		if (inValue1 != null && inValue2 != null) {
			if (inValue1.length == inValue2.length) {
				result = true;
				for (int n=0; n<inValue1.length; n++) {
					if (inValue1[n] != inValue2[n]) {
						result = false;
						break;
					}
				}
			}
			else
				result = false;
		}
		else {
			if (inValue1 == null && inValue2 == null)
				result = true;
			else
				result = false;
		}
		
		sendOutNodeValue(0, result);
	}
}
