/*----------------------------------------------------------------
*
*	CyberToolBox
*
*	Copyright (C) Satoshi Konno 1996-1997
*
*	File:	FilterCeil.java
*
----------------------------------------------------------------*/

public class FilterCeil extends Module {

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

		double value[] = inNode[0].getDoubleValues();

		if (value != null) {
			int ivalue[] = new int[value.length];
			for (int n=0; n<value.length; n++)
				ivalue[n] = (int)Math.ceil(value[n]);
			sendOutNodeValue(0, ivalue);
		}
	}
}
