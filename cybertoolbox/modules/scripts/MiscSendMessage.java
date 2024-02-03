/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscSendMessage.java
*
----------------------------------------------------------------*/

public class MiscSendMessage extends Module {

	private Event				event;

	public void initialize() {
		event = getWorld().getUserEvent(getStringValue());
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}
		
		if (inNode[0].isConnected() == true) {
			String eventName = inNode[0].getStringValue();
			if (eventName != null) {
				event = getWorld().getUserEvent(eventName);
				if (event != null) 
					setValue(eventName);
				else
					setValue("");
			}
			else {
				event = null;
				setValue("");
			}
		}

		String eventMsg = inNode[1].getStringValue();
		
		if (event != null)
			event.run(eventMsg);
	}
}
