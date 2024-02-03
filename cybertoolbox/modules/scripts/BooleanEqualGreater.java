/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	BooleanEqualGreater.java
*
----------------------------------------------------------------*/

public class BooleanEqualGreater extends Module {

	private boolean	result;

	public void initialize() {
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		String inValueString1 = inNode[0].getStringValue();
		String inValueString2 = inNode[1].getStringValue();
		
		if (inValueString1 == null || inValueString2 == null) {
			if (inValueString1 == null && inValueString2 == null) 
				sendOutNodeValue(0, true);
			else
				sendOutNodeValue(0, false);
			return;
		}
			
		try {
			double value1 = inNode[0].getDoubleValue();
			double value2 = inNode[1].getDoubleValue();
			if (value1 <= value2)
				result = true;
			else 
				result = false;
		}
		catch (NumberFormatException exception) {
			if (inValueString2.compareTo(inValueString1) >= 0) 
				result = true;
			else
				result = false;
		}

		if (result == true) 
			sendOutNodeValue(0, true);
		else
			sendOutNodeValue(0, false);
	}
}
