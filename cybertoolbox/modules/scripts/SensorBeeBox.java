/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SensorMouse.java
*
----------------------------------------------------------------*/

public class SensorBeeBox extends Module {

	BeeBox			beeBox;
	int				port;

	float				stick[] = new float[2];			 	
	int				beeSwitch;
				 	
	public void initialize() {
		beeBox = null;
		port = -1;
		
		String portName = getStringValue();
		if (portName != null) {
			if (portName.equalsIgnoreCase("SERIAL1") == true)
				port = BeeBox.SERIALPORT1;
			if (portName.equalsIgnoreCase("SERIAL2") == true)
				port = BeeBox.SERIALPORT2;
			if (portName.equalsIgnoreCase("SERIAL3") == true)
				port = BeeBox.SERIALPORT3;
			if (portName.equalsIgnoreCase("SERIAL4") == true)
				port = BeeBox.SERIALPORT4;
		}
		
		if (0 <= port) 
			beeBox = new BeeBox(port);
	}

	public void shutdown() {
		if (beeBox == null)
			return;
			
		beeBox = null;
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (beeBox == null)
			return;
			
		beeSwitch = beeBox.getSwitches();
		if ((beeSwitch & BeeBox.SWITCH1) != 0)
			sendOutNodeValue(0, "SWITCH1");
		else if ((beeSwitch & BeeBox.SWITCH2) != 0)
			sendOutNodeValue(0, "SWITCH2");
		else if ((beeSwitch & BeeBox.SWITCH7) != 0)
			sendOutNodeValue(0, "SWITCH7");
		else if ((beeSwitch & BeeBox.SWITCH8) != 0)
			sendOutNodeValue(0, "SWITCH8");
		else
			sendOutNodeValue(0, "NONE");
		
		stick[0] = beeBox.getX();
		stick[1] = beeBox.getY();
		sendOutNodeValue(1, stick);

		sendOutNodeValue(2, beeBox.getLever());
	}

}
