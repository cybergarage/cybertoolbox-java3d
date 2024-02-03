/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscGetTime.java
*
----------------------------------------------------------------*/

import java.util.*;

public class MiscGetTime extends Module {
	
	Calendar calendar = new GregorianCalendar();

	public void initialize() {
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		calendar.setTime(new Date());
		sendOutNodeValue(0, calendar.get(Calendar.HOUR_OF_DAY));
		sendOutNodeValue(1, calendar.get(Calendar.MINUTE));
		sendOutNodeValue(2, calendar.get(Calendar.SECOND));
	}

}

